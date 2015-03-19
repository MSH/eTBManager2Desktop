/**
 * 
 */
package org.msh.etbm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Store information about the last version ID received from the server.
 * This information will be necessary when synchronizing with the server,
 * where the client inform the server the last ID received in the last 
 * synchronization
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="entitylastversion")
public class EntityLastVersion extends WSObject {
	private static final long serialVersionUID = 8112117582712448513L;

	@Id
	private String entityClass;
	private Integer lastVersion;

	/**
	 * @return the entityClass
	 */
	public String getEntityClass() {
		return entityClass;
	}
	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}
	/**
	 * @return the lastVersion
	 */
	public Integer getLastVersion() {
		return lastVersion;
	}
	/**
	 * @param lastVersion the lastVersion to set
	 */
	public void setLastVersion(Integer lastVersion) {
		this.lastVersion = lastVersion;
	}
}
