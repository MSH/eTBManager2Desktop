package org.msh.etbm.entities;

import org.msh.etbm.entities.enums.DstResult;
import org.msh.etbm.entities.enums.ExamStatus;
import org.msh.etbm.sync.SyncClear;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="examdst")
public class ExamDST extends LaboratoryExamResult implements Serializable {
	private static final long serialVersionUID = -1911463378908689952L;

    public ExamDST() {
        super();
    }

        
        
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="exam")
	@SyncClear
	private List<ExamDSTResult> results = new ArrayList<ExamDSTResult>();

	private int numResistant;
	private int numSusceptible;
	private int numContaminated;

	/**
	 * Search for a result by the substance 
	 * @param sub - Substance to be used to search result
	 * @return - Susceptibility result
	 */
	public ExamDSTResult findResultBySubstance(Substance sub) {
		for (ExamDSTResult res: results) {
			if (res.getSubstance().equals(sub)) {
				return res;
			}
		}
		return null;
	}

	public List<ExamDSTResult> getResults() {
		return results;
	}

	public void setResults(List<ExamDSTResult> results) {
		this.results = results;
	}

	/**
	 * @param numResistant the numResistant to set
	 */
	public void setNumResistant(int numResistant) {
		this.numResistant = numResistant;
	}

	/**
	 * @return the numResistant
	 */
	public int getNumResistant() {
		return numResistant;
	}

	/**
	 * @return the numSusceptible
	 */
	public int getNumSusceptible() {
		return numSusceptible;
	}

	/**
	 * @param numSusceptible the numSusceptible to set
	 */
	public void setNumSusceptible(int numSusceptible) {
		this.numSusceptible = numSusceptible;
	}

	/**
	 * @return the numContaminated
	 */
	public int getNumContaminated() {
		return numContaminated;
	}

	/**
	 * @param numContaminated the numContaminated to set
	 */
	public void setNumContaminated(int numContaminated) {
		this.numContaminated = numContaminated;
	}

	/**
	 * true is exam has result or should not be validated this way.
	 * @return
	 */
	public boolean hasResult(){
		// check if there is any result
	 	if(ExamStatus.PERFORMED.equals(this.getStatus())) {
			for (ExamDSTResult res : this.getResults()){
				if (res.getResult() != DstResult.NOTDONE) {
					return true;
				}
			}

			return false;
		}

		return true;
	}
}
