package org.msh.etbm.test.desktop;

import java.io.File;

/**
 * Created by rmemoria on 19/3/15.
 */
public class TestData {

    private static final TestData __instance = new TestData();

    private String token;
    private File downloadedFile;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public File getDownloadedFile() {
        return downloadedFile;
    }

    public void setDownloadedFile(File downloadedFile) {
        this.downloadedFile = downloadedFile;
    }

    public static TestData instance() {
        return __instance;
    }
}
