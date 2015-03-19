/**
 * 
 */
package org.msh.etbm.desktop.startup;

/**
 * Information about the credentials to log into the server
 * @author Ricardo Memoria
 *
 */
public class ServerCredentials {

	private String server;
	private String username;
	private String password;
	private Integer workspaceId;
	private String token;

	public ServerCredentials() {
		super();
	}

	public ServerCredentials(String server, Integer workspaceId, String username, String password, String token) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
		this.workspaceId = workspaceId;
		this.token = token;
	}
	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}
	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the workspaceId
	 */
	public Integer getWorkspaceId() {
		return workspaceId;
	}
	/**
	 * @param workspaceId the workspaceId to set
	 */
	public void setWorkspaceId(Integer workspaceId) {
		this.workspaceId = workspaceId;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
}
