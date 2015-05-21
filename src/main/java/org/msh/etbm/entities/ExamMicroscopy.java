package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.msh.etbm.entities.enums.MicroscopyResult;
import org.msh.etbm.entities.enums.SampleType;
import org.msh.etbm.entities.enums.VisualAppearance;

@Entity
@Table(name="exammicroscopy")
public class ExamMicroscopy extends LaboratoryExamResult implements Serializable {
	private static final long serialVersionUID = 1514632458011926044L;

	private MicroscopyResult result;
	
	private Integer numberOfAFB;


	private SampleType sampleType;

	private VisualAppearance visualAppearance;
	
	public MicroscopyResult getResult() {
		return result;
	}

	public void setResult(MicroscopyResult result) {
		this.result = result;
	}

	/**
	 * @param numberOfAFB the numberOfAFB to set
	 */
	public void setNumberOfAFB(Integer numberOfAFB) {
		this.numberOfAFB = numberOfAFB;
	}

	/**
	 * @return the numberOfAFB
	 */
	public Integer getNumberOfAFB() {
		return numberOfAFB;
	}

	/**
	 * @return the sampleType
	 */
	public SampleType getSampleType() {
		return sampleType;
	}

	/**
	 * @param sampleType the sampleType to set
	 */
	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

    public VisualAppearance getVisualAppearance() {
        return visualAppearance;
    }

    public void setVisualAppearance(VisualAppearance visualAppearance) {
        this.visualAppearance = visualAppearance;
    }
}
