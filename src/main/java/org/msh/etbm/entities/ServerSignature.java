/**
 * 
 */
package org.msh.etbm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Store information about the server that will be serialized to the client version.
 * This information will be necessary for the client to communicate with the correct
 * server when synchronizing data (avoiding to send data to the wrong server)
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="serversignature")
public class ServerSignature {

	public static final int SERVER_ID = 1;
	
	@Id
	private Integer id;
	
	private String systemURL;
	private String pageRootURL;
	private String countryCode;
	private String adminMail;
	private String serverUrl;
	private boolean initialized;
	private Date lastSyncDate;

	/**
	 * @return the systemURL
	 */
	public String getSystemURL() {
		return systemURL;
	}
	/**
	 * @param systemURL the systemURL to set
	 */
	public void setSystemURL(String systemURL) {
		this.systemURL = systemURL;
	}
	/**
	 * @return the pageRootURL
	 */
	public String getPageRootURL() {
		return pageRootURL;
	}
	/**
	 * @param pageRootURL the pageRootURL to set
	 */
	public void setPageRootURL(String pageRootURL) {
		this.pageRootURL = pageRootURL;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the adminMail
	 */
	public String getAdminMail() {
		return adminMail;
	}
	/**
	 * @param adminMail the adminMail to set
	 */
	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Return the URL of the synchronization server
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}
	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
}
