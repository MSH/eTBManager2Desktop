package org.msh.etbm.services.core;

import org.msh.xview.FormContext;


/**
 * Home class responsible for creating instance of a variable, validate and persist it
 * @author Ricardo Memoria
 *
 */
public interface EntityServices<E> {

	/**
	 * Return the instance of the variable managed by the controller
	 * @return
	 */
	E findEntity(Object id);
	
	/**
	 * Change the instance of the variable managed by the controller
	 * @param instance
	 */
//	void setInstance(E instance);

	/**
	 * Get the id of the variable object being managed (if available)
	 * @return
	 */
//	Object getId();
	
	/**
	 * Set the id of the variable, if available
	 * @param id
	 */
//	void setId(Object id);
	
	/**
	 * Validate the values applied to the variable
	 * @param form
	 * @return
	 */
	boolean validate(E entity, FormContext form);
	
	/**
	 * Save the changes to an entity
	 * @param entity is the instance of the entity class
	 */
	void save(E entity);
	
	/**
	 * Update the instance with the entity manager
	 */
//	void refresh();
	
	/**
	 * Delete the instance managed by the controller
	 */
	void delete(Object id);
	
	/**
	 * Return the entity class being managed
	 * @return Class of the entity
	 */
	Class<E> getEntityClass();
	
	/**
	 * Create a new instance of the entity. It's the same as executing a new Class
	 * @return a new instance of the entity class
	 */
	E newEntity();
}
