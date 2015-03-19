/**
 * 
 */
package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Contain information about the synchronization status of the entity.
 * The entity may be in the following states:<p/>
 * * New entity (changed = true, serverId = null);<br/>
 * * Entity changed (changed = true, ServerId != null);<br/>
 * * Entity synchronized with server (changed = false, ServerId != null);
 * @author Ricardo Memoria
 *
 */
@Embeddable
public class SynchronizationData {

	@Column(name="sync_changed")
	private boolean changed;
	
	@Column(name="sync_serverId")
	private Integer serverId;

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}
	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
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

	
}
