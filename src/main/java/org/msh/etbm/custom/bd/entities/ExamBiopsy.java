package org.msh.etbm.custom.bd.entities;

import org.msh.etbm.custom.bd.entities.enums.BiopsyResult;
import org.msh.etbm.entities.LaboratoryExamResult;
import org.msh.etbm.entities.enums.SampleType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="exambiopsy")
public class ExamBiopsy extends LaboratoryExamResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1704814081123568208L;

	private BiopsyResult result;
	
	private SampleType sampleType;

	public BiopsyResult getResult() {
		return result;
	}

	public void setResult(BiopsyResult result) {
		this.result = result;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}
}
