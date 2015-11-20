/**
 * 
 */
package org.msh.etbm.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.services.login.UserSession;
import org.msh.eventbus.EventBusService;

/**
 * Upload the file to the server and check server answer 
 * @author Ricardo Memoria
 *
 */
public class SyncFileDeliverer {

	private static final String SYNC_ONGOING = "ONGOING";
	
	public enum SyncFileDeliverEvent {
		CONNECTING, CONNECTED, TRANSMITTING, TRANSMITTED, WAITING_ANSWER, FINISHED;
	};
	
	
	private ServerServices conn;
	// the user token contains information to authenticate the user
	private String userToken;
	// the file token contains information about the file being processed
	private String fileToken;
	private String serverUrl;
	private String errorMessage;

	public SyncFileDeliverer() {
		conn = new ServerServices();
	}
	
	/**
	 * Send the file to the server and return the file token. The file token
	 * must be used to get status information about the uploaded file
	 * @param syncFile the file to upload
	 * @return String containing the file token
	 */
	public String sendToServer(File syncFile)  {
		notifyEvent(SyncFileDeliverEvent.CONNECTING);
		getUserToken();
		notifyEvent(SyncFileDeliverEvent.CONNECTED);

		notifyEvent(SyncFileDeliverEvent.TRANSMITTING);
		HttpResponse response = uploadFile(syncFile);
		notifyEvent(SyncFileDeliverEvent.TRANSMITTED);

		fileToken = readFileToken(response);
		return fileToken;
	}

	/**
	 * Notify about events that occurs during delivering
	 * @param event the event to notify
	 */
	private void notifyEvent(SyncFileDeliverEvent event) {
		EventBusService.raiseEvent(event);
	}
	
	/**
	 * Return the server URL in use
	 * @return String value
	 */
	public String getServerURL() {
		if (serverUrl == null) {
			ServerServices srv = App.getComponent(ServerServices.class);
			serverUrl = srv.getServerUrl();
		}
		return serverUrl;
	}
	
	/**
	 * Upload the file to the server
	 * @param syncFile instance of {@link File} to upload
	 */
	protected HttpResponse uploadFile(File syncFile) {
		String addr = conn.addPageToUrl(getServerURL(), "sync/fileupload.seam?tk=" + userToken);
		return conn.uploadFileToURL(addr, syncFile);
	}


	/**
	 * Authenticate in the server with the given user name and password and get the user token
	 */
	protected void getUserToken() {
		UserSession userSession = UserSession.instance();

		// check if there is any token in memory
		userToken = userSession.getLastToken();
		if (userToken != null)
			return;
		
		UserWorkspace uw = UserSession.getUserWorkspace();
		String pwd = userSession.getPassword();

		userToken = conn.login(getServerURL(), uw.getWorkspace().getId(), uw.getUser().getLogin(), pwd);

		userSession.setLastToken(userToken);
	}
	
	/**
	 * Get the file token to receive the answer from the server
	 */
	protected String readFileToken(HttpResponse response) {
		if (response == null)
			throw new RuntimeException("No response sent from the server");
		try {
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("token=")) {
					String[] s = line.split("=");
					return s[1];
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		throw new RuntimeException("File token not sent");
	}
	
	
	/**
	 * Wait for the server processing the answer file. An HTTP request is sent to the
	 * server while it receives an on-going message
	 * @return true if the file was successfully processed, or false if there was an error
	 */
	public boolean waitServerAnswer() {
		notifyEvent(SyncFileDeliverEvent.WAITING_ANSWER);

		String url = getServerURL();
		url = conn.addPageToUrl(url, "sync/syncstatus.seam?tk=" + fileToken);
		String s = SYNC_ONGOING;
		while (SYNC_ONGOING.equals(s)) {
			// wait 2 seconds to make another try
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}

			s = conn.getServerContent(url);
			System.out.println("Server response: " + s);
		}

		// check if server responded with an error
		if ((s == null) || (s.startsWith("ERROR:"))) {
			errorMessage = s.substring(6);
			return false;
		}
		
		notifyEvent(SyncFileDeliverEvent.FINISHED);
		return "SUCCESS".equals(s);
	}

	
	/**
	 * Connect to the server and download the answer file using the file token
	 * @return
	 */
	public File downloadAnswerFile(DownloadProgressListener listener) {
		return conn.downloadAnswerFile(getServerURL(), fileToken, listener);
	}
	
	/**
	 * @return the fileToken
	 */
	public String getFileToken() {
		return fileToken;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
