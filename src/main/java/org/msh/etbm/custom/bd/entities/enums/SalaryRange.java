package org.msh.etbm.custom.bd.entities.enums;

public enum SalaryRange {
	LOWER, 
	LOWER_MIDDLE,
	MIDDLE,
	UPPER_MIDDLE,
	HIGHER;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
