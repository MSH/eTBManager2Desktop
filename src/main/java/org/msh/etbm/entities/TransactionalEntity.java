/**
 * 
 */
package org.msh.etbm.entities;

import org.msh.etbm.entities.enums.RecordState;

/**
 * Interface that all entities must implement in order to store
 * information about its synchronization state with the server
 * @author Ricardo Memoria
 *
 */
public interface TransactionalEntity {

	Integer getServerId();
	void setServerId(Integer serverId);

	RecordState getEntityState();
	void setEntityState(RecordState state);
}
