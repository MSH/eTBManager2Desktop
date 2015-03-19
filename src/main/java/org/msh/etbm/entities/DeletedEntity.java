/**
 * 
 */
package org.msh.etbm.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Store temporary information about deleted entities in the system. These entities
 * will be sent in the next synchronization with the server
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="deletedentity")
public class DeletedEntity {
    
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private String entityName;
	
	private int entityId;

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
	 * @return the entityId
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
}
