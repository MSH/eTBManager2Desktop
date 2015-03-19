package org.msh.etbm.entities.enums;

public enum DotBy {
	HCW, 
	H,
	CHW,
	ND,
	//Cambodia
	HOSPITAL,
	AMBULATORY,
	HOMECARE,
	COMMUNITY,
	NONDOT;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
