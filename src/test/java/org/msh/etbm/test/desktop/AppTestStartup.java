/**
 * 
 */
package org.msh.etbm.test.desktop;

import java.io.File;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.databases.DatabaseManager;
import org.msh.etbm.services.login.Authenticator;
import org.msh.etbm.sync.IniFileImporter;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * Abstract class used for testing of the application. It configures the database,
 * log into the application and call a method to be tested in transaction
 *  
 * @author Ricardo Memoria
 *
 */
public class AppTestStartup {

	private static final AppTestStartup _instance = new AppTestStartup();
	
	private boolean initialized;
	
	/**
	 * Initialize the application - create database connection, import ini file 
	 * and authenticate user
	 */
	public void initialize() {
		if (initialized) {
			return;
		}

		initDatabase();
		runSQLScript();
		restoreIniFile();
		authenticate();

		initialized = true;
	}

	
	/**
	 * Create the testing database
	 */
	protected void initDatabase() {
		DatabaseManager.instance().setDatabasePath("target");
		DatabaseManager.instance().setForceCreation(true);
		App.initialize();
	}
	
	
	/**
	 * Populate the database from an initialization file
	 */
	protected void restoreIniFile() {
		IniFileImporter importer = new IniFileImporter();
		String fname = "src/test/resources/test-ini-file.xml";
		File file = new File(fname);
		if (!file.exists()) {
			throw new RuntimeException("File does not exist: " + fname);
		}
		importer.start(file, null, false);
	}


	/**
	 * Authenticate the user
	 */
	protected void authenticate() {
		Authenticator auth = App.getComponent(Authenticator.class);
		if (!auth.login("RICARDO", "password", 1)) {
			throw new RuntimeException("Authentication error");
		}
	}
		

	/**
	 * Create temporary table for testing
	 */
	protected void runSQLScript() {
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				String sql = "CREATE MEMORY TABLE PUBLIC.TBCASEXX(ID INTEGER NOT NULL PRIMARY KEY,"
						+ "EMAIL VARCHAR(100) NOT NULL, REGISTRATIONREQUIRED BIT(1), AGE INTEGER, "
						+ "CONSTRAINT FK_TBCASEXX FOREIGN KEY(ID) REFERENCES PUBLIC.TBCASE(ID) ON DELETE CASCADE)";
				App.getEntityManager().createNativeQuery(sql).executeUpdate();
			}
		});
	}
	
	
	/**
	 * Return the singleton instance of the {@link AppTestStartup}
	 * @return instance of {@link AppTestStartup}
	 */
	public static AppTestStartup instance() {
		return _instance;
	}

}
