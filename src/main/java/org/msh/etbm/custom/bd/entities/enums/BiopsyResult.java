package org.msh.etbm.custom.bd.entities.enums;

public enum BiopsyResult {
	TUBERCULUS,
	NON_TUBERCULUS,
	NOT_DONE;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
