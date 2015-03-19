/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msh.etbm.entities;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author usrivastava
 */
@MappedSuperclass
public abstract class SynchronizableEntity implements Synchronizable {
    private static final long serialVersionUID = 1L;

    @Embedded
    private SynchronizationData syncData = new SynchronizationData();

	/**
	 * @return the synchronizationData
	 */
	public SynchronizationData getSyncData() {
		return syncData;
	}

	/**
	 * @param synchronizationData the synchronizationData to set
	 */
	public void setSyncData(SynchronizationData synchronizationData) {
		this.syncData = synchronizationData;
	}
}
