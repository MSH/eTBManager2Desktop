package org.msh.etbm.entities.enums;

public enum MicroscopyResult {

	NEGATIVE	('-'),
	POSITIVE	('+'),
	PLUS		('+'),
	PLUS2		('+'),
	PLUS3		('+'),
	PLUS4		('+'),
	NOTDONE		('0'),
	PENDING     ('0');
	
	private char result;
	
	private MicroscopyResult(char result) {
		this.result = result;
	}
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
	
	/**
	 * Check if the value is positive result
	 * @return true if it's a positive result
	 */
	public boolean isPositive() {
		return (result == '+');
	}


	/**
	 * Check if the value is a negative result
	 * @return true if it's a negative result
	 */
	public boolean isNegative() {
		return (result == '-');
	}


	/**
	 * Return the list of values that are positive
	 * @return
	 */
	static public MicroscopyResult[] getPositiveResults() {
		int num = 0;
		for (MicroscopyResult res: values()) {
			if (res.isPositive())
				num++;
		}
		
		MicroscopyResult[] lst = new MicroscopyResult[num];
		int i = 0;
		for (MicroscopyResult res: values()) {
			if (res.isPositive()) {
				lst[i] = res;
				i++;
			}
		}
		return lst;
	}

	
	/**
	 * Return the list of values that are negative
	 * @return
	 */
	static public MicroscopyResult[] getNegativeResults() {
		int num = 0;
		for (MicroscopyResult res: values()) {
			if (res.isNegative())
				num++;
		}
		
		MicroscopyResult[] lst = new MicroscopyResult[num];
		int i = 0;
		for (MicroscopyResult res: values()) {
			if (res.isNegative()) {
				lst[i] = res;
				i++;
			}
		}
		return lst;
	}
}
