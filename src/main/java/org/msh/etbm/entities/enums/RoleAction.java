package org.msh.etbm.entities.enums;

/**
 * Actions available for each role
 * @author Ricardo Memoria
 *
 */
public enum RoleAction {

	VIEW,
	NEW,
	EDIT,
	DELETE,
	EXEC;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
