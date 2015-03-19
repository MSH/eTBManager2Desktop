package org.msh.etbm.desktop.app;

import org.msh.etbm.sync.ServerException;

/**
 * Exception that is thrown when the application is being initialized, i.e, trying to
 * initialize the database from server information
 *
 * Created by ricardo on 13/10/14.
 */
public class AppInitializationException extends ServerException {

    public AppInitializationException() {
    }

    public AppInitializationException(String message) {
        super(message);
    }

    public AppInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppInitializationException(Throwable cause) {
        super(cause);
    }

}
