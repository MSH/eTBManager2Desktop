/**
 * 
 */
package org.msh.etbm.services.cases.prevtreat;

import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.enums.PrevTBTreatmentOutcome;
import org.msh.utils.date.Month;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Santos
 *
 */
public class PrevTBTreatmentData {

	private Month month;
	private int year;
	private PrevTBTreatmentOutcome outcome;
	private List<SubstanceOption> substances = new ArrayList<SubstanceOption>();
	private Month outcomeMonth;
	private Integer outcomeYear;

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public PrevTBTreatmentOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(PrevTBTreatmentOutcome outcome) {
		this.outcome = outcome;
	}

	public List<SubstanceOption> getSubstances() {
		return substances;
	}

	public void setSubstances(List<SubstanceOption> substances) {
		this.substances = substances;
	}

	public SubstanceOption getSubstance0() {
		return substances.get(0);
	}

	public SubstanceOption getSubstance1() {
		return substances.get(1);
	}

	public SubstanceOption getSubstance2() {
		return substances.get(2);
	}

	public SubstanceOption getSubstance3() {
		return substances.get(3);
	}

	public SubstanceOption getSubstance4() {
		return substances.get(4);
	}

	public SubstanceOption getSubstance5() {
		return substances.get(5);
	}

	public SubstanceOption getSubstance6() {
		return substances.get(6);
	}

	public SubstanceOption getSubstance7() {
		return substances.get(7);
	}

	public SubstanceOption getSubstance8() {
		return substances.get(8);
	}

	public SubstanceOption getSubstance9() {
		return substances.get(9);
	}

	public SubstanceOption getSubstance10() {
		return substances.get(10);
	}

	public SubstanceOption getSubstance11() {
		return substances.get(11);
	}

	public SubstanceOption getSubstance12() {
		return substances.get(12);
	}

	public SubstanceOption getSubstance13() {
		return substances.get(13);
	}

	public SubstanceOption getSubstance14() {
		return substances.get(14);
	}

	public SubstanceOption getSubstance15() {
		return substances.get(15);
	}

	public SubstanceOption getSubstance16() {
		return substances.get(16);
	}

	public SubstanceOption getSubstance17() {
		return substances.get(17);
	}

	public SubstanceOption getSubstance18() {
		return substances.get(18);
	}

	public SubstanceOption getSubstance19() {
		return substances.get(19);
	}

	public SubstanceOption getSubstance20() {
		return substances.get(20);
	}

	public Month getOutcomeMonth() {
		return outcomeMonth;
	}

	public void setOutcomeMonth(Month outcomeMonth) {
		this.outcomeMonth = outcomeMonth;
	}

	public Integer getOutcomeYear() {
		return outcomeYear;
	}

	public void setOutcomeYear(Integer outcomeYear) {
		this.outcomeYear = outcomeYear;
	}

	public List<Substance> getSelectedSubstances(){
		List<Substance> ret = null;
		for(SubstanceOption o : substances){
			if(o.isSelected()){
				if(ret == null)
					ret = new ArrayList<Substance>();
				ret.add(o.getSubstance());
			}
		}

		return ret;
	}
}
