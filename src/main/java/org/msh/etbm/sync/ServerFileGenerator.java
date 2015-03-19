/**
 * 
 */
package org.msh.etbm.sync;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.EntityLastVersion;
import org.msh.etbm.entities.WeeklyFrequency;
import org.msh.utils.DataStreamUtils;

import com.rmemoria.datastream.DataConverter;
import com.rmemoria.datastream.DataInterceptor;
import com.rmemoria.datastream.DataMarshaller;
import com.rmemoria.datastream.ObjectProvider;
import com.rmemoria.datastream.StreamContext;

/**
 * Generate the file in the client computer to be sent to the server with the record changed
 * 
 * @author Ricardo Memoria
 *
 */
public class ServerFileGenerator implements ObjectProvider, DataInterceptor {

	// maximum number of records per query
	private int MAX_RESULTS = 200;

	private EntityManager em;
	private List<String> hqls;
	private StreamContext context;
	private int queryIndex;
	private int recordIndex;
	private int firstResult;
	private List list;
	
	// some statistics
	// Number of objects serialized
	private int objectCount;

	/**
	 * Default constructor
	 */
	public ServerFileGenerator() {
		hqls = new ArrayList<String>();

		// case data
		hqls.add("from TbCase a join fetch a.patient left join fetch a.regimen left join fetch a.notifAddress.adminUnit where a.syncData.changed=true");
		hqls.add("from PrescribedMedicine a join fetch a.tbcase join fetch a.medicine join fetch a.source where a.syncData.changed=true");
		hqls.add("from TreatmentHealthUnit a join fetch a.tbunit join fetch a.tbcase where a.syncData.changed=true");
		hqls.add("from ExamCulture a join fetch a.tbcase left join fetch a.method left join fetch a.laboratory where a.syncData.changed=true");
		hqls.add("from ExamMicroscopy a join fetch a.tbcase left join fetch a.method left join fetch a.laboratory where a.syncData.changed=true");
		hqls.add("from ExamXpert a join fetch a.tbcase left join fetch a.method left join fetch a.laboratory where a.syncData.changed=true");
		hqls.add("from MedicalExamination a join fetch a.tbcase where a.syncData.changed=true");
		hqls.add("from ExamHIV a join fetch a.tbcase where a.syncData.changed=true");
		hqls.add("from ExamXRay a join fetch a.tbcase left join fetch a.presentation where a.syncData.changed=true");
		hqls.add("from ExamDST a join fetch a.tbcase where a.syncData.changed=true");
		hqls.add("from ExamDSTResult a join fetch a.substance join fetch a.exam where a.syncData.changed=true");
		hqls.add("from TreatmentMonitoring a join fetch a.tbcase where a.syncData.changed=true");
		hqls.add("from TbContact a join fetch a.tbcase left join fetch a.contactType left join fetch a.conduct where a.syncData.changed=true");
		hqls.add("from CaseSideEffect a join fetch a.tbcase left join fetch a.substance left join fetch a.substance2 where a.syncData.changed=true");
		hqls.add("from CaseComorbidity a join fetch a.tbcase left join fetch a.comorbidity where a.syncData.changed=true");
		hqls.add("from EntityLastVersion");
		hqls.add("from DeletedEntity");
	}

	/**
	 * Generate the client initialization file
	 */
	public void generateFile(OutputStream out) {
		objectCount = 0;
		
		em = App.getEntityManager();

		context = DataStreamUtils.createContext("serverfile-schema.xml");
		context.addInterceptor(this);
		addConverter(context);
		DataMarshaller m = DataStreamUtils.createXMLMarshaller(context);

		try {
			// adjust name of the unit
			GZIPOutputStream outzip = new GZIPOutputStream(out);
			try{
				m.marshall(outzip, this);
			}
			finally {
				outzip.finish();
				outzip.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class getObjectClass(Object obj) {
		if (obj instanceof HibernateProxy)
			return Hibernate.getClass(obj);
		
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object newObject(Class arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getObjectToSerialize(int index) {
		return getObject();
	}


	/**
	 * Return the object from the list
	 */
	private Object getObject() {
		if (list == null) {
			queryIndex = 0;
			recordIndex = 0;
			firstResult = 0;
			list = em.createQuery(hqls.get(queryIndex)).getResultList();
		}
		
		if (recordIndex >= list.size()) {
			list = getNextList();
			recordIndex = 0;
			if (list == null)
				return null;
		}

		Object obj = list.get(recordIndex++);
		
		// increment the count of object to send (entitylastversion is always
		// included in the file, so it's not counted as a data to transmit)
		if (!(obj instanceof EntityLastVersion)) {
			objectCount++;
		}
		return obj;
	}

	/**
	 * Get the next list to be sent to XML
	 * @return
	 */
	private List getNextList() {
		// is the end of the list ?
		if (recordIndex < MAX_RESULTS) {
			queryIndex++;
			if (queryIndex >= hqls.size())
				return null;
			recordIndex = 0;
			firstResult = 0;
		}
		else {
			firstResult += MAX_RESULTS;
		}

		em.clear();
		list = em.createQuery(hqls.get(queryIndex))
				.setFirstResult(firstResult)
				.setMaxResults(MAX_RESULTS)
				.getResultList();

		// if there is nothing to return, move to the next list
		if (list.size() == 0) {
			recordIndex = 0;
			return getNextList();
		}
		
		return list;
	}

	/**
	 * Add the converters for the serialization/deserialization
	 * @param context
	 */
	protected void addConverter(StreamContext context) {
		DataConverter converter = new DataConverter() {
			@Override
			public String convertToString(Object obj) {
				if (obj == null)
					return null;

				int val = ((WeeklyFrequency)obj).getValue();
				return Integer.toString(val);
			}
			
			@Override
			public Object convertFromString(String data, Class classType) {
				int val = Integer.parseInt(data);
				return new WeeklyFrequency(val);
			}
		};
		context.setConverter(WeeklyFrequency.class, converter);
	}

	/**
	 * @return the objectCount
	 */
	public int getObjectCount() {
		return objectCount;
	}

}
