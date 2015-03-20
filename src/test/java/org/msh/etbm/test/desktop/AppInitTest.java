package org.msh.etbm.test.desktop;

import static org.junit.Assert.*;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.databases.DatabaseManager;
import org.msh.etbm.sync.DownloadProgressListener;
import org.msh.etbm.sync.IniFileImporter;
import org.msh.etbm.sync.ServerServices;
import org.msh.etbm.sync.WorkspaceInfo;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * Created by rmemoria on 19/3/15.
 */
public class AppInitTest {

    private static final String ETBM_SERVER = "http://dev.msh.org/etbmanager";
    private static final String ETBM_USER = "autotest123";
    private static final String ETBM_PASSWORD = "autotest123";


    @BeforeTest
    public void createDatabase() {
        System.out.println("1. Create database");

        DatabaseManager.instance().setDatabasePath("target");
        DatabaseManager.instance().setForceCreation(true);
        App.initialize();
    }

    @BeforeTest(dependsOnMethods = {"createDatabase"})
    public void testWorkspaceList() {
        System.out.println("2. Workspace list");
        ServerServices srv = App.getComponent(ServerServices.class);
        List<WorkspaceInfo> lst = srv.getWorkspaces(ETBM_SERVER, ETBM_USER, ETBM_PASSWORD);
        Assert.assertTrue(lst.size() > 0);

        boolean found = false;
        for (WorkspaceInfo item: lst) {
            if (item.getId() == 1) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @BeforeTest(dependsOnMethods = {"testWorkspaceList"})
    public void getAuthenticationToken() {
        System.out.println("2. get Authentication token");

        ServerServices srv = App.getComponent(ServerServices.class);
        String token = srv.login(ETBM_SERVER, 1, ETBM_USER, ETBM_PASSWORD);
        assertNotNull(token);
        System.out.println("Authentication token = " + token);
        TestData.instance().setToken(token);
    }

    /**
     * Download the initialization file
     */
    @BeforeTest(dependsOnMethods = {"getAuthenticationToken"})
    public void downloadIniFile() {
        System.out.println("3. Download ini file");

        ServerServices srv = App.getComponent(ServerServices.class);
        TestData data = TestData.instance();
        String token = data.getToken();
        File downloadFile = srv.downloadIniFile(ETBM_SERVER, token, new DownloadProgressListener() {
            private int prog = 0;
            @Override
            public void onUpdateProgress(double perc) {
                perc = Math.round(perc / 10);
                if (perc > prog) {
                    prog = (int)perc;
                    System.out.println((prog * 10) + '%');
                }
            }

            @Override
            public void onInitDownload(File file) {
                System.out.println("Downloading " + file);
            }
        });

        data.setDownloadedFile(downloadFile);
    }

    /**
     * Test the importing of the initialization file
     */
    @BeforeTest(dependsOnMethods = {"downloadIniFile"})
    public void importIniFile() {
        System.out.println("4. Import ini file");

        File downloadFile = TestData.instance().getDownloadedFile();

        if (downloadFile == null) {
            throw new RuntimeException("Download file not defined");
        }

        IniFileImporter importer = new IniFileImporter();
        if (!downloadFile.exists()) {
            throw new RuntimeException("File does not exist: " + downloadFile.getName());
        }
        importer.start(downloadFile, null, true);
    }
}
