package org.msh.etbm.desktop.app;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import javax.swing.JOptionPane;

import org.msh.etbm.sync.ServerException;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * Proxy class that handles the current event queue to implement an specific
 * application error handling.
 * <p/>
 * This error handling simply displays a message to the user about the error generated.
 * 
 * @author Ricardo Memoria
 *
 */
public class EventQueueProxy extends EventQueue {

	/** {@inheritDoc}
	 */
	@Override
	protected void dispatchEvent(AWTEvent event) {
		try {
			super.dispatchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: Implement a better error handling saving the error trace in a log file in the application working dir 
			String message;

			if (e instanceof CannotCreateTransactionException) {
				message = "Error trying to connect to the database. Probably there is another application accessing the database";
			}
			else {
                if (e instanceof ServerException) {
                    message = e.getMessage();
                }
                else {
                    message = e.getMessage();
                    if ((message == null) || (message.isEmpty()))
                        message = "Error: " + e.getClass();
                }
			}

			// display message to the user about the error
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);

            if (e instanceof AppInitializationException) {
                System.exit(1);
            }
		}
	}

}
