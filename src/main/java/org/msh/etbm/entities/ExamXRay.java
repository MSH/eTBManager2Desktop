package org.msh.etbm.entities;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.msh.etbm.entities.enums.XRayBaseline;
import org.msh.etbm.entities.enums.XRayEvolution;
import org.msh.etbm.entities.enums.XRayResult;

/**
 * Stores information about an X-Ray exam of a case
 * @author Ricardo Lima
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("gen")
@Table(name="examxray")
public class ExamXRay extends CaseData {
	private static final long serialVersionUID = 6467262326586421605L;

	private XRayResult result;
	
	private XRayEvolution evolution;
	
	private XRayBaseline baseline;
	
	private Boolean destruction;
	
	@ManyToOne
	@JoinColumn(name="PRESENTATION_ID")
	private FieldValue presentation;

	/**
	 * @return the baseline
	 */
	public XRayBaseline getBaseline() {
		return baseline;
	}

	/**
	 * @param baseline the baseline to set
	 */
	public void setBaseline(XRayBaseline baseline) {
		this.baseline = baseline;
	}

	/**
	 * Return the X-Ray result
	 * @return the result
	 */
	public XRayResult getResult() {
		return result;
	}

	/**
	 * Set the X-Ray result
	 * @param result the result to set
	 */
	public void setResult(XRayResult result) {
		this.result = result;
	}

	/**
	 * @return the evolution
	 */
	public XRayEvolution getEvolution() {
		return evolution;
	}

	/**
	 * @param evolution the evolution to set
	 */
	public void setEvolution(XRayEvolution evolution) {
		this.evolution = evolution;
	}

	/**
	 * @param presentation the presentation to set
	 */
	public void setPresentation(FieldValue presentation) {
		this.presentation = presentation;
	}

	/**
	 * @return the presentation
	 */
	public FieldValue getPresentation() {
		return presentation;
	}

	/**
	 * @return the destruction
	 */
	public Boolean getDestruction() {
		return destruction;
	}

	/**
	 * @param destruction the destruction to set
	 */
	public void setDestruction(Boolean destruction) {
		this.destruction = destruction;
	}
}
