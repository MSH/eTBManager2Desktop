/**
 * 
 */
package org.msh.etbm.test.desktop;

import java.io.File;
import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.databases.DatabaseManager;
import org.msh.etbm.services.login.Authenticator;
import org.msh.etbm.sync.DownloadProgressListener;
import org.msh.etbm.sync.IniFileImporter;
import org.msh.etbm.sync.ServerServices;
import org.msh.etbm.sync.WorkspaceInfo;
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

    private static final String ETBM_SERVER = "http://dev.msh.org/etbmanager";
    private static final String ETBM_USER = "autotest123";
    private static final String ETBM_PASSWORD = "autotest123";

	private boolean initialized;
	
	/**
	 * Initialize the application - create database connection, import ini file 
	 * and authenticate user
	 */
	public void initialize() {
		if (initialized) {
			return;
		}

        downloadIniFile();
		initDatabase();
		runSQLScript();
		restoreIniFile();
		authenticate();

		initialized = true;
	}


    protected void downloadIniFile() {
        ServerServices srv = App.getComponent(ServerServices.class);
        List<WorkspaceInfo> lst = srv.getWorkspaces(ETBM_SERVER, ETBM_USER, ETBM_PASSWORD);
        if (lst.size() == 0) {
            throw new RuntimeException("No workspace assigned to the user");
        }

        boolean found = false;
        for (WorkspaceInfo item: lst) {
            if (item.getId() == 1) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("MSH Demo workspace not found for user " + ETBM_USER + " in " + ETBM_SERVER);
        }

        String token = srv.login(ETBM_SERVER, 1, ETBM_USER, ETBM_PASSWORD);
        if (token == null) {
            throw new RuntimeException("Error trying to log into " + ETBM_SERVER + " with user " + ETBM_USER);
        }

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
		if (!"SUCCESS".equals(auth.login("RICARDO", "password", 1))) {
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
