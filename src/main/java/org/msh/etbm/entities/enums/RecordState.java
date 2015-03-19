package org.msh.etbm.entities.enums;

public enum RecordState {
	CHANGED,
	SYNCHED;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
