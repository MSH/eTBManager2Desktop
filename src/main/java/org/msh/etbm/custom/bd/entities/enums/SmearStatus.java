package org.msh.etbm.custom.bd.entities.enums;

public enum SmearStatus {
	SMEAR_POSITIVE,
	SMEAR_NEGATIVE,
	NOT_EVALUATED;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
