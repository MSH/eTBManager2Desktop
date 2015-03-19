/**
 * 
 */
package org.msh.etbm.desktop.databases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.ServerSignature;
import org.msh.etbm.services.login.ServerSignatureServices;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * Manage databases in the desktop application
 * 
 * @author Ricardo Memoria
 *
 */
public class DatabaseManager {

	// singleton object
	private final static DatabaseManager myinstance = new DatabaseManager();

	// target path of the database in the application path
	private final static String databaseFolder = "database";
	
	// files of the database
	private final static String[] databaseFiles = {
            "etbmanager.script",
            "etbmanager.properties"
	};
	
	// path of database files in the jar file
	private final static String databaseResources = "resources/database/";

	// path of the database. If none is specified, the working directory will be used
	private String databasePath;
	// if true, force database files to be recreated
	private boolean forceCreation;
	
	/**
	 * Return the directory of the database in the system
	 * @return {@link File} pointing to the database directory 
	 */
	public File getDatabaseDir() {
		File appdir = databasePath != null? new File(databasePath): App.getWorkingDirectory();
		return new File(appdir, databaseFolder);
	}

	
	/**
	 * Return true if the database was initialized with the data of a health unit
	 * @return boolean value
	 */
	public boolean isDatabaseInitialized() {
		if (!isDatabaseCreated()) {
			return false;
		}
		
		ServerSignatureServices sigService = App.getComponent(ServerSignatureServices.class);
		ServerSignature srvSig = sigService.getServerSignature();
		
		return srvSig.isInitialized();
	}

	
	/**
	 * Start the initialization of the database. Called when a file is about to be imported
	 * in the system
	 */
/*
	public void startInitialization() {
		ServerSignatureServices sigService = App.getComponent(ServerSignatureServices.class);
		ServerSignature srv = sigService.getServerSignature();
		srv.setInitialized(false);
		sigService.updateServerSignature(srv);
	}
*/

	/**
	 * Mark the database as initialized
	 */
	public void notifyDatabaseInitialized() {
		if (!isDatabaseCreated()) {
			throwErrorDatabaseNotInitialized("Files were not created");
		}

		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				ServerSignatureServices srv = App.getComponent(ServerSignatureServices.class);
				ServerSignature sig = srv.getServerSignature();
				if (sig == null) {
					throwErrorDatabaseNotInitialized(null);
				}
				
				sig.setInitialized(true);
				srv.updateServerSignature(sig);
			}
		});
	}

	/**
	 * Throw error of database not initialized
	 * @param error
	 */
	private void throwErrorDatabaseNotInitialized(String error) {
		throw new RuntimeException("Database is not initialized" + (error != null? ": " + error: ""));
	}
	
	
	/**
	 * Return true if the database directory exists and database files are available
	 * @return boolean value
	 */
	public boolean isDatabaseCreated() {
		if (forceCreation) {
			return false;
		}

		// check if database folder exists
		File dbdir = getDatabaseDir();
		if (!dbdir.exists()) {
			return false;
		}
		
		// check if database files exist
		for (String fname: databaseFiles) {
			File file = new File(dbdir, fname);
			if (!file.exists()) {
				return false;
			}
		}
		
		return true;
	}

	
	/**
	 * Create the database folder and its files in the application
	 * working directory
	 */
	public void createDatabase() {
		// create the database folder (or empty it if the folder exists)
		File dbdir = getDatabaseDir();
		// delete directory and all files inside it (clean it all)
		deleteFile(dbdir);
		// recreate it again
		if (!dbdir.mkdirs()) {
			throw new RuntimeException("Error trying to create directory " + dbdir.toString());
		}

		// copy the files to the database
		for (String fname: databaseFiles) {
			String resource = databaseResources + fname;
			File target = new File(dbdir, fname);
			copyResourceToTargetDir(resource, target);
		}
		
		forceCreation = false;
	}

	
	/**
	 * Delete all files in the directory
	 * @param dir
	 */
	private void deleteFile(File dir) {
		// check if directory exists
		if (!dir.exists()) {
			return;
		}

		if (dir.isDirectory()) {
			for (File f: dir.listFiles()) {
				deleteFile(f);
			}
		}
		
		if (!dir.delete()) {
			throw new RuntimeException("Unable to delete all files in " + dir);
		}
	}
	
	/**
	 * Copy a resource from the jar to the file system
	 * @param resource the full name of the resource in the JAR file
	 * @param targetFile the file name that the resource will be copied to
	 */
	protected void copyResourceToTargetDir(String resource, File targetFile) {
		InputStream stream = DatabaseManager.class.getClassLoader().getResourceAsStream(resource);
		
		if (stream == null) {
			throw new RuntimeException("Resource not found " + resource);
		}

		OutputStream out = null;
		try {
			try {
				int readBytes;
				byte[] buffer = new byte[4096];
				out = new FileOutputStream(targetFile);
				while ((readBytes = stream.read(buffer)) > 0) {
					out.write(buffer, 0, readBytes);
				}
			}
			finally {
				stream.close();
				if (out != null) {
					out.close();
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	

	
	/**
	 * Return the singleton instance of {@link DatabaseManager}
	 * @return instance of {@link DatabaseManager}
	 */
	public static DatabaseManager instance() {
		return myinstance;
	}


	/**
	 * @return the databasePath
	 */
	public String getDatabasePath() {
		return databasePath;
	}


	/**
	 * @param databasePath the databasePath to set
	 */
	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}


	/**
	 * @return the forceCreation
	 */
	public boolean isForceCreation() {
		return forceCreation;
	}


	/**
	 * @param forceCreation the forceCreation to set
	 */
	public void setForceCreation(boolean forceCreation) {
		this.forceCreation = forceCreation;
	}
}
