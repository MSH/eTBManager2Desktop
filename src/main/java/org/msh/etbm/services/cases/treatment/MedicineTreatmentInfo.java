/**
 * 
 */
package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.Source;

/**
 * Information about a medicine prescribed in the treatment. Used in the service
 * to start a new treatment
 *  
 * @author Ricardo Memoria
 *
 */
public class MedicineTreatmentInfo {

	private Medicine medicine;
	private int months;
	private int doseUnit;
	private int frequency;
	private Source source;

	/**
	 * @return the medicine
	 */
	public Medicine getMedicine() {
		return medicine;
	}
	/**
	 * @param medicine the medicine to set
	 */
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}
	/**
	 * @return the months
	 */
	public int getMonths() {
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(int months) {
		this.months = months;
	}
	/**
	 * @return the doseUnit
	 */
	public int getDoseUnit() {
		return doseUnit;
	}
	/**
	 * @param doseUnit the doseUnit to set
	 */
	public void setDoseUnit(int doseUnit) {
		this.doseUnit = doseUnit;
	}
	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}
}
