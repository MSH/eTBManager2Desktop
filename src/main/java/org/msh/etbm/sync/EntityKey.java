/**
 * 
 */
package org.msh.etbm.sync;


/**
 * Store information about an entity key sent from the server to the client.
 * Objects of this class are instanced just as a bridge between the XML file (initialization
 * or answer) and the entity to be updated
 * 
 * @author Ricardo Memoria
 *
 */
public class EntityKey {

	public enum EntityKeyAction {	
		UPDATED,		// the entity changed in the client side was updated in the server side
		CLI_DELETED, 	// the entity deleted in the client side was deleted in the server side
		SRV_DELETED		// the entity was deleted in the server side, and must be deleted in the client side too 
	};
	private String entityName;
	private int clientId;
	private Integer serverId;
	private EntityKeyAction action;

	
	/**
	 * Search for the entity class corresponding to the entity name
	 * @return
	 */
	public Class findEntityClass() {
		try {
			String className = "org.msh.etbm.entities." +  entityName;
			return Class.forName(className);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}
	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the serverId
	 */
	public Integer getServerId() {
		return serverId;
	}
	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the action
	 */
	public EntityKeyAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(EntityKeyAction action) {
		this.action = action;
	}

}
