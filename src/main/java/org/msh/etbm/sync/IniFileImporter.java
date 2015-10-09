/**
 * 
 */
package org.msh.etbm.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.databases.DatabaseManager;
import org.msh.etbm.desktop.databases.TBUnitLinks;
import org.msh.etbm.entities.*;
import org.msh.etbm.services.login.ServerSignatureServices;
import org.msh.springframework.persistence.TransactionController;
import org.msh.utils.DataStreamUtils;

import com.rmemoria.datastream.DataConverter;
import com.rmemoria.datastream.DataInterceptor;
import com.rmemoria.datastream.DataUnmarshaller;
import com.rmemoria.datastream.ObjectConsumer;
import com.rmemoria.datastream.StreamContext;

/**
 * @author Ricardo Memoria
 *
 */
/**
 * @author Ricardo Memoria
 *
 */
public class IniFileImporter {

	private static final int BUFFER_SIZE = 65535;
	
	FileInputStream fstream;
	private Workspace workspace;
	private TransactionController txController;
	private ImportProgressListener progressListener;
	private String errorMessage;
	// list of cases that had its tags updated
	private List<Integer> caseTagsUpdated = new ArrayList<Integer>();

	/**
	 * Data interceptor to load entities
	 */
	private DataInterceptor interceptor = new DataInterceptor() {
		@Override
		public Object newObject(Class objectType, Map<String, Object> params) {
			if (params != null)
				 return createNewObject(objectType, params);
			else return null;
		}
		
		@Override
		public Class getObjectClass(Object obj) {
			return null;
		}
	};
	
	/**
	 * Start a new database (or use the current one) and read file data
	 * @param file instance of {@link File} that contains the server content
	 * @param listener implementation of {@link ImportProgressListener} that will receive notification
	 * 					about the file importing progress
	 * @param compressed true if the file is compressed (zip gzip format) or false if it is
	 * 					a simple text file
	 */
	public void start(File file, ImportProgressListener listener, boolean compressed) {
		progressListener = listener;

		try {
			File destfile;
			boolean deleteWhenFinished = compressed;
			if (compressed) {
				destfile = File.createTempFile("temp", "etbm");
				uncompressFile(file, destfile);
			}
			else {
				destfile = file;
			}

			fstream = new FileInputStream(destfile);
			try {
				importData(fstream);
			}
			finally {
				fstream.close();
				if (deleteWhenFinished) {
					destfile.delete();
				}
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			throw new RuntimeException(e);
		}
	}


	/**
	 * Create a new object based on its class and parameters from the XML data.
	 * This method is called by the data stream when it's necessary to restore an
	 * object by the parameters available
	 * @param objectType is the class of the object
	 * @param params is the list of parameters available in the XML
	 * @return instance of the object class
	 */
	protected Object createNewObject(Class objectType, Map<String, Object> params) {
		// is information about the last version used in each entity?
		if (objectType == EntityLastVersion.class) {
			String entityName = (String)params.get("entityClass");
			if (entityName == null)
				return null;
			
			List<EntityLastVersion> lst = App.getEntityManager()
				.createQuery("from " + objectType.getCanonicalName() + " where entityClass = :ent")
				.setParameter("ent", entityName)
				.getResultList();
			return lst.size() > 0? lst.get(0): null;
		}

        if (objectType == AdministrativeUnit.class) {
            System.out.println("Test");
        }

		// check if it's an object that will be synchronized with the server 
		if (Synchronizable.class.isAssignableFrom(objectType)) {
			Integer serverId = (Integer)params.get("syncData.serverId");
			if (serverId == null)
				return null;
			String hql = "from " + objectType.getCanonicalName() + " where syncData.serverId = :id";
			List lst = App.getEntityManager().createQuery(hql)
					.setParameter("id", serverId)
					.getResultList();
			return lst.size() > 0? lst.get(0): null;
		}
		
		Integer id = (Integer)params.get("id");
		System.out.println(objectType + " -> " + id);
		if (id == null)
			return null;

        Object entity = App.getEntityManager().find(objectType, id);
        if ((objectType == UserRole.class) && (entity == null)) {
            throw new RuntimeException("User role was not found = " + id);
        }

        return entity;
	}


	/**
	 * Uncompress a file compressed with {@link GZIPInputStream}
	 * @param gzipfile instance of {@link File} containing the compressed file
	 * @param destfile instance of {@link File} where uncompressed file will be written to
	 */
	protected void uncompressFile(File gzipfile, File destfile) {
		try {
			if (destfile.exists())
				destfile.delete();
			
			byte[] buffer = new byte[BUFFER_SIZE];

			GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(gzipfile));
			FileOutputStream out = new FileOutputStream(destfile);

			int noRead;
			while ((noRead = gzin.read(buffer)) != -1) {
			        out.write(buffer, 0, noRead);
			}
			gzin.close();
			out.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	
	/**
	 * Read the input stream provided and initialize the records in the database
	 * @param in instance of {@link InputStream}
	 */
	private void importData(InputStream in) {
		StreamContext context = DataStreamUtils.createContext("clientinifile-schema.xml");

		// add the interceptor
		context.addInterceptor(interceptor);

		// add converter to WeeklyFrequency object
		context.setConverter(WeeklyFrequency.class, new DataConverter() {
			@Override
			public String convertToString(Object obj) {
				return Integer.toString( ((WeeklyFrequency)obj).getValue() );
			}
			
			@Override
			public Object convertFromString(String s, Class clazz) {
				WeeklyFrequency wf = new WeeklyFrequency();
				wf.setValue( Integer.parseInt(s) );
				return wf;
			}
		});
		
		DataUnmarshaller um = DataStreamUtils.createXMLUnmarshaller(context);

		// prepare the transaction controller 
		txController = new TransactionController();
		try {
			// start reading the objects
			um.unmarshall(in, new ObjectConsumer() {
				@Override
				public void onNewObject(Object obj) {
					handleNewObject(obj);
					// check if there is an on-going transaction
					if (txController.isActive())
						txController.commitTransaction();
				}

				@Override
				public void startObjectReading(Class objectClass) {
					if (objectClass != ServerSignature.class)
						txController.startTransaction();
				}
			});
			
		} finally {
			// if it's active that's because there was an error
			if (txController.isActive())
				txController.rollbackTransaction();
		}

		// notify that database was initialized correctly
		DatabaseManager.instance().notifyDatabaseInitialized();
	}
	
	/**
	 * Called when a new object is just read from the input stream
	 * @param obj is the object read from the data stream
	 */
	protected void handleNewObject(Object obj) {
		if (obj instanceof ServerSignature) {
			handleServerSignature((ServerSignature)obj);
			return;
		}
		
		if (obj instanceof Workspace) {
			workspace = (Workspace)obj;
			saveEntity(obj);
			return;
		}

		saveEntity(obj);
	}

	/**
	 * Save entity. The method is under a transaction, so it's safe to persist and continue
	 * @param obj
	 */
	protected void saveEntity(Object obj) {
		// is information about the links between units
		if (obj instanceof TBUnitLinks) {
			updateTBUnitLinks((TBUnitLinks)obj);
			return;
		}
		
		if (obj instanceof EntityKey) {
			handleEntityKey((EntityKey)obj);
			return;
		}
		
		if (obj instanceof CaseTag) {
			updateCaseTag((CaseTag)obj);
			return;
		}
		
		EntityManager em = App.getEntityManager();

		if (obj instanceof WSObject) {
			workspace = em.merge(workspace); 
			((WSObject)obj).setWorkspace(workspace);
		}

		// handle exceptions in the UserWorkspace class
		if (obj instanceof UserWorkspace) {
			UserWorkspace uw = (UserWorkspace)obj;
			// set workspace in the object
			workspace = em.merge(workspace);
			uw.setWorkspace(workspace);
			// save the user
			em.persist(uw.getUser());
		}
		else 
		if (obj instanceof TbCase) {
			TbCase tbcase = (TbCase)obj;
			// save the patient
			Patient p = tbcase.getPatient();
			workspace = em.merge(workspace);
			p.setWorkspace(workspace);
			em.persist(p);
		}

		em.persist(obj);
		em.flush();
		em.clear();
		
		try {
			if (progressListener != null) {
				long pos = fstream.getChannel().position();
				long size = fstream.getChannel().size();
				double perc = (double)pos/(double)size * 100.0F;
				progressListener.onUpdateProgress(perc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Update the server key created by the server and contained in the given object
	 * @param key instance of {@link EntityKey}
	 */
	private void handleEntityKey(EntityKey key) {
		App.getEntityManager().createQuery("update " + key.getEntityName() + 
				" set syncData.changed = false, syncData.serverId = :serverid where id = :id")
				.setParameter("serverid", key.getServerId())
				.setParameter("id", key.getClientId())
				.executeUpdate();

/*		EntityManagerUtils.doInTransaction(new ActionCallback<EntityKey>(key) {
			@Override
			public void execute(EntityKey key) {
				EntityManager em = App.getEntityManager();

				// search for entity in the desktop version
				List lst = em.createQuery("from " + key.getEntityName() + " where id = :id")
						.setParameter("id", key.getClientId())
						.getResultList();
				
				// it was not found. Probably it was deleted
				if (lst.size() == 0) {
					throw new RuntimeException("Entity not found. Entity=" + key.getEntityName() + ", id=" + key.getClientId());
				}
				
				// update entity
				Object obj = lst.get(0);
				if (obj instanceof Synchronizable) {
					// update synchronization status
					SynchronizationData data = ((Synchronizable)obj).getSyncData();
					data.setServerId(key.getServerId());
					data.setChanged(false);
					
					// save entity
					em.persist(obj);
					em.flush();
				}
			}
		});
*/
	}


	/**
	 * Include a tag to the case. The method checks if it's the first time in this loop
	 * a tag is being assigned to the case. If so, delete all previous tags of the case
	 * @param caseTag
	 */
	private void updateCaseTag(CaseTag caseTag) {
		EntityManager em = App.getEntityManager();

		// check if it's the first tag being updated to the given case 
		if (!caseTagsUpdated.contains(caseTag.getCaseId())) {
			// delete the previous tags of the case
			App.getEntityManager().createNativeQuery("delete from tags_case where case_id = :id")
				.setParameter("id", caseTag.getCaseId())
				.executeUpdate();
			caseTagsUpdated.add(caseTag.getCaseId());
		}

		// return the tb case with the given server ID
		List<TbCase> lst = App.getEntityManager().createQuery("from TbCase a where a.syncData.serverId = :id")
				.setParameter("id", caseTag.getCaseId())
				.getResultList();
		
		if (lst.size() == 0) {
			throw new RuntimeException("Case not found. ID = " + caseTag.getCaseId());
		}
		
		TbCase tbcase = lst.get(0);
		// insert the tag to the case
		em.createNativeQuery("insert into tags_case (case_id, tag_id) values (:caseid, :tagid)")
			.setParameter("caseid", tbcase.getId())
			.setParameter("tagid", caseTag.getTagId())
			.executeUpdate();
	}
	
	/**
	 * Update information of the TB Unit about its links with other TB Units
	 * @param links contains information about these links
	 */
	private void updateTBUnitLinks(TBUnitLinks links) {
		EntityManager em = App.getEntityManager();

		em.createQuery("update Tbunit set authorizerUnit.id = :autid, "
				+ "firstLineSupplier.id = :flsid, secondLineSupplier.id = :slsid "
				+ "where id = :id")
				.setParameter("id", links.getUnitId())
				.setParameter("autid", links.getAuthorizedId())
				.setParameter("flsid", links.getFirstLineSupplierId())
				.setParameter("slsid", links.getSecondLineSupplierId())
				.executeUpdate();
	}

	/**
	 * Initialize the database in order to receive data
	 * @param serverSig server signature
	 */
	protected void handleServerSignature(ServerSignature serverSig) {		
/*		DatabaseManager dbman = DatabaseManager.instance();
		// database exists ?
		Database db = dbman.findDatabaseByName(serverSig.getPageRootURL());
		if (db == null) {
			// create new database
			db = dbman.createDatabase(App.instance().getServerUrl());
		}

		// select database in use
		dbman.setSelectedDatabase(db);
*/
		// save server signature
		txController.startTransaction();
		ServerSignatureServices sigService = App.getComponent(ServerSignatureServices.class);
		ServerSignature aux = sigService.getServerSignature();

		if (aux != null) {
			aux.setAdminMail(serverSig.getAdminMail());
			aux.setCountryCode(serverSig.getCountryCode());
			aux.setPageRootURL(serverSig.getPageRootURL());
			aux.setSystemURL(serverSig.getSystemURL());
			serverSig = aux;
		}
		sigService.updateServerSignature(serverSig);
		txController.commitTransaction();
	}

	
	/**
	 * Return true if an error occurred during importing
	 * @return boolean value
	 */
	public boolean isErrorOcurred() {
		return errorMessage != null;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
