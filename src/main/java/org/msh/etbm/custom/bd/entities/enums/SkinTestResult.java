package org.msh.etbm.custom.bd.entities.enums;

public enum SkinTestResult {
	POSITIVE,
	NEGATIVE,
	NOT_DONE;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
