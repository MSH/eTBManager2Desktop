package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.msh.etbm.entities.enums.XpertResult;
import org.msh.etbm.entities.enums.XpertRifResult;


@Entity
@Table(name="examxpert")
public class ExamXpert extends LaboratoryExamResult implements Serializable{
	private static final long serialVersionUID = 7672681749376963359L;
	
	private XpertResult result;
	
	private XpertRifResult rifResult;

	public XpertResult getResult() {
		return result;
	}

	public void setResult(XpertResult result) {
		this.result = result;
	}

	/**
	 * @return the rifResult
	 */
	public XpertRifResult getRifResult() {
		return rifResult;
	}

	/**
	 * @param rifResult the rifResult to set
	 */
	public void setRifResult(XpertRifResult rifResult) {
		this.rifResult = rifResult;
	}
}
