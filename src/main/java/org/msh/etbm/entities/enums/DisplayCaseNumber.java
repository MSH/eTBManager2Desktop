package org.msh.etbm.entities.enums;

public enum DisplayCaseNumber {

	CASE_ID,
	VALIDATION_NUMBER,
	USER_DEFINED;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
