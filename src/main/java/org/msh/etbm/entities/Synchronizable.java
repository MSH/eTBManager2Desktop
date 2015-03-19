/**
 * 
 */
package org.msh.etbm.entities;

import java.io.Serializable;

/**
 * Interface that all entity must implement in order to have its state
 * syncronized with the server
 * 
 * @author Ricardo Memoria
 *
 */
public interface Synchronizable extends Serializable {

	/**
	 * Return information about synchronization data with the server
	 * @return instance of {@link SynchronizationData}
	 */
	SynchronizationData getSyncData();
	
	/**
	 * Set the sync data to store
	 * @param syncData
	 */
	void setSyncData(SynchronizationData syncData);
}
