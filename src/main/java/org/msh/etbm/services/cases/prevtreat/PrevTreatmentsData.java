/**
 * 
 */
package org.msh.etbm.services.cases.prevtreat;

import java.util.ArrayList;
import java.util.List;

import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.TbCase;

/**
 * @author Ricardo Memoria
 *
 */
public class PrevTreatmentsData {

	private TbCase tbcase;
	private List<Substance> substances;
	private List<PrevTBTreatmentData> treatments = new ArrayList<PrevTBTreatmentData>();

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}
	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
	/**
	 * @return the substances
	 */
	public List<Substance> getSubstances() {
		return substances;
	}
	/**
	 * @param substances the substances to set
	 */
	public void setSubstances(List<Substance> substances) {
		this.substances = substances;
	}
	/**
	 * @return the treatments
	 */
	public List<PrevTBTreatmentData> getTreatments() {
		return treatments;
	}
	/**
	 * @param treatments the treatments to set
	 */
	public void setTreatments(List<PrevTBTreatmentData> treatments) {
		this.treatments = treatments;
	}
}
