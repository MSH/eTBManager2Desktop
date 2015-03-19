package org.msh.etbm.entities.enums;

/**
 * Classification of the cases
 * @author Ricardo Memoria
 *
 */
public enum CaseClassification {
	TB,
	DRTB,
	NTM;


	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
	public String getKeySuspect() {
		return getClass().getSimpleName().concat("." + name() + ".suspect");
	}
}
