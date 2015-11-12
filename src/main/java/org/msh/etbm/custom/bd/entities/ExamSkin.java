package org.msh.etbm.custom.bd.entities;

import org.msh.etbm.custom.bd.entities.enums.SkinTestResult;
import org.msh.etbm.entities.LaboratoryExamResult;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="examskin")
public class ExamSkin extends LaboratoryExamResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5916032251188255799L;
	private SkinTestResult result;

	public SkinTestResult getResult() {
		return result;
	}

	public void setResult(SkinTestResult result) {
		this.result = result;
	}
}
