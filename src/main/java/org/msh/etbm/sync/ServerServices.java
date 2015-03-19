/**
 * 
 */
package org.msh.etbm.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.axis.AxisFault;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.services.login.ServerSignatureServices;
import org.msh.etbm.webservices.authenticator.AuthenticatorServiceProxy;
import org.msh.etbm.webservices.authenticator.Response;
import org.msh.utils.DataStreamUtils;
import org.springframework.stereotype.Component;

import com.rmemoria.datastream.DataUnmarshaller;
import org.xml.sax.SAXException;

/**
 * Contain a set of common tasks to make it easier to work with eTB Manager Web server
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class ServerServices {

	private static final int BUFFER_SIZE = 65535;

	
	/**
	 * Return the server URL in use registered in the server signature entity
	 * @return String value containing the server URL
	 */
	public String getServerUrl() {
		ServerSignatureServices srv = App.getComponent(ServerSignatureServices.class);
		String url = srv.getServerSignature().getServerUrl();
		return checkServerAddress(url);
	}
	
	
	/**
	 * Handle the error message in the response from the server. If there is an error,
	 * an exception is thrown with the correct message to display
	 * @param resp instance of {@link Response} class
	 */
	public void handleErrorMessage(Response resp) {
		if (resp.getErrorno() == 0) {
			return;
		}
		
		if (resp.getErrormsg() != null) {
			throw new ServerException("Unexpected error from the server:\n" + resp.getErrormsg());
		}
		
		if (resp.getErrorno() == Response.RESP_AUTHENTICATION_FAIL) {
			throw new ServerException(Messages.getString("org.jboss.seam.loginFailed"));
		}
		
		if (resp.getErrorno() == Response.RESP_INVALID_SESSION) {
			throw new ServerException("The system is using an invalid user session. Logout and try again");
		}
		
		if (resp.getErrorno() == Response.RESP_VALIDATION_ERROR) {
			throw new ServerException("Validation error");
		}
	}
	
	
	/**
	 * Return the list of workspaces available for the user
	 * @param user
	 * @param password
	 * @param server
	 * @return
	 */
	public List<WorkspaceInfo> getWorkspaces(String server, String user, String password) {
		server = checkServerAddress(server);

		// call web service to return list of workspaces
		AuthenticatorServiceProxy auth = new AuthenticatorServiceProxy(server + "/services/authentication");
		try {
			Response resp = auth.getUserWorkspaces(user, password);
			handleErrorMessage(resp);

			DataUnmarshaller du = DataStreamUtils.createXMLUnmarshaller("webservice-response-schema.xml");
			Object res = du.unmarshall( DataStreamUtils.createStringInputStream(resp.getResult()) );
			return (List<WorkspaceInfo>)res;

		} catch (Exception e) {
            if (e instanceof AxisFault) {
                throw new RuntimeException("Connection error");
            }
            if (e instanceof ServerException) {
                throw (ServerException)e;
            }
            throw new RuntimeException(e);
		}
	}


	/**
	 * Check if the server address is incomplete. Append the name 'etbmanager'
	 * at the end of address and the protocol 'http://', if missing
	 *  
	 * @param url is the address of eTB Manager web version
	 * @return the address with complements
	 */
	protected String checkServerAddress(String url) {
		String server = url;
		// try to fill gaps in the composition of the server address
		if (!server.startsWith("http")) {
			server = "http://" + server;
		}
		
		if ((!server.endsWith("etbmanager")) && (!server.endsWith("sitetb"))) {
			if ((!server.endsWith("/")) || (!server.endsWith("\\"))) {
				server += "/";
			}
			server += "etbmanager";
		}
		return server;
	}
	
	/**
	 * Log into the web server and return a token to be used in future requests
	 * @param server the URL of the eTB Manager web server
	 * @param workspaceid the ID of the workspace to connect to
	 * @param user the login of the user
	 * @param password the password of the user
	 * @return String containing token for future connections
	 */
	public String login(String server, Integer workspaceid, String user, String password) {
		server = checkServerAddress(server);
		
		AuthenticatorServiceProxy auth = new AuthenticatorServiceProxy(server + "/services/authentication");
		try {
			Response resp = auth.login(user, password, workspaceid);
			handleErrorMessage(resp);

			return resp.getResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	/**
	 * Download the desktop initialization file from the server using the given user token
	 * @param server
	 * @param userToken
	 */
	public void downloadIniFile(String server, String userToken, DownloadProgressListener listener) {
        File file;
		try {
			URL url = new URL(checkServerAddress(server) + "/sync/downloadinifile.seam?tk=" + userToken);
			file = downloadFromURL(url, listener);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

        if (file == null) {
            throw new RuntimeException("There was an error in e-TB Manager web server. " +
                    "Please inform your system administrator");
        }
	}

	
	/**
	 * Download the answer file produced by the server from a file synchronization operation 
	 * @param server the URL of the eTB Manager server
	 * @param fileToken the file token given by the web server at the beginning of the synchronization process
	 * @param listener an optional listener to receive information about the file download progress
	 */
	public File downloadAnswerFile(String server, String fileToken, DownloadProgressListener listener) {
        File file;
		try {
			URL url = new URL(checkServerAddress(server) + "/sync/downloadanswerfile.seam?tk=" + fileToken);
            file = downloadFromURL(url, listener);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

        if (file == null) {
            throw new RuntimeException("There was an error in e-TB Manager web server when generating the answer. " +
                    "Please inform your system administrator");
        }

        return file;
	}
	
	/**
	 * Download a file from an URL using a listener to get information about the download progress
	 * @param url is the URL to download the file from
	 * @param listener instance of {@link DownloadProgressListener} to get information about download progress
	 * @return instance of {@link File} pointing to the downloaded file 
	 * @throws Exception
	 */
	protected File downloadFromURL(URL url, DownloadProgressListener listener) throws Exception  {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		try {
			int responseCode = conn.getResponseCode();
			
			if (responseCode != HttpURLConnection.HTTP_OK) 
				return null;
			
			String disposition = conn.getHeaderField("Content-Disposition");
			String contentType = conn.getContentType();
			Integer contentLength = conn.getContentLength();

			String filename = null;
			if (disposition != null) {
				int index = disposition.indexOf("filename=");
				if (index > 0) {
                    filename = disposition.substring(index + 10,
                            disposition.length() - 1);
				}
			}
			System.out.println(contentType);
			System.out.println(contentLength);
			System.out.println(filename);
			
			InputStream in = conn.getInputStream();
			File file = new File(App.getWorkingDirectory().getPath(), filename);
			if (listener != null)
				listener.onInitDownload(file);

			FileOutputStream fout = new FileOutputStream(file);
			
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			
			while ((bytesRead = in.read(buffer)) != -1) {
                fout.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
    			if (listener != null) {
    				double perc = (double)totalBytesRead * 100/(double)contentLength;
    				listener.onUpdateProgress(perc);
    			}
            }
			
			fout.close();
			return file;
			
		} finally {
			conn.disconnect();
		}
	}
	
	
	/**
	 * Upload a file to a specific URL sending an HTTP POST method
	 * @param url the address to send the file
	 * @param file the file to be sent
	 * @return the {@link HttpResponse} of the connection in order to get information about 
	 * the response of the server
	 */
	public HttpResponse uploadFileToURL(String url, File file)  {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			MultipartEntity me = new MultipartEntity();
			me.addPart("fileDescriptor", new StringBody( file.getName() ));
			me.addPart("fileName", new StringBody(file.getName()));
			
			FileBody fileBody = new FileBody(file, "application/octect-stream");
			me.addPart("attachement", fileBody);
			
			post.setEntity(me);
			
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Add a page to the URL string, checking if the URL string finishes with a slash
	 * before including the page
	 * @param serverUrl the string containing the URL (ex.: http://www.etbmanager.org)
	 * @param page the page to add to the URL (ex.: login.seam)
	 * @return the URL concatenated with the page (ex.: http://www.etbmanager.org/login.seam)
	 */
	public String addPageToUrl(String serverUrl, String page) {
		String addr = serverUrl;
		if ((!addr.endsWith("/")) && (!addr.endsWith("\\"))) {
			addr += "/";
		}
		addr += page;
		return addr;
	}
	
	/**
	 * Return the content of an URL in a string format
	 * @param url
	 * @return
	 */
	public String getServerContent(String url) {
		try {
			HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();

			int responseCode = conn.getResponseCode();
			
			if (responseCode != HttpURLConnection.HTTP_OK) 
				return null;
			
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (builder.length() > 0)
					builder.append('\n');
				builder.append(line);
			}
			
			reader.close();
			return builder.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
