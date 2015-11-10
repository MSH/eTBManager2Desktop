package org.msh.etbm.custom.bd.entities.enums;

public enum SideEffectAction {
	NONE,
	DISCONTINUED, 
	REDUCED,
	SWITCH,
	RE_CHALLENGE,
	OTHER;

	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
