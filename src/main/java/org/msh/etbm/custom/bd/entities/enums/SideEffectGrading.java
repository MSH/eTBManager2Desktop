package org.msh.etbm.custom.bd.entities.enums;

public enum SideEffectGrading {
	MILD,
	MODERATE, 
	SEVERE;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
