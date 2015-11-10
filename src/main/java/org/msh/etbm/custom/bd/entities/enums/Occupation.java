package org.msh.etbm.custom.bd.entities.enums;

public enum Occupation {
	STD,
	DLB,
	HWF,
	SVC,
	BSN,
	RTD,
	UEM,
	FMR,
	GMW,
	DRV,
	OTR;

	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
