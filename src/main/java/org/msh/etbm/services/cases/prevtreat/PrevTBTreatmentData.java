/**
 * 
 */
package org.msh.etbm.services.cases.prevtreat;

import org.msh.etbm.entities.PrevTBTreatment;

/**
 * @author Ricardo Memoria
 *
 */
public class PrevTBTreatmentData {

	private PrevTBTreatment prevTBTreatment;
	private boolean[] columns;

	/**
	 * @return the prevTBTreatment
	 */
	public PrevTBTreatment getPrevTBTreatment() {
		return prevTBTreatment;
	}
	/**
	 * @param prevTBTreatment the prevTBTreatment to set
	 */
	public void setPrevTBTreatment(PrevTBTreatment prevTBTreatment) {
		this.prevTBTreatment = prevTBTreatment;
	}
	/**
	 * @return the columns
	 */
	public boolean[] getColumns() {
		return columns;
	}
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(boolean[] columns) {
		this.columns = columns;
	}
}
