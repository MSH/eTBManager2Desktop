package org.msh.etbm.services.cases;

import java.util.ArrayList;
import java.util.List;

import org.msh.etbm.entities.CaseComorbidity;
import org.msh.etbm.entities.TbCase;

/**
 * Contain a list of comorbidities to be edited by the user
 * 
 * @author Ricardo Memoria
 *
 */
public class Comorbidities {
	private List<CaseComorbidity> items = new ArrayList<CaseComorbidity>();
	private TbCase tbcase;

	/**
	 * @return the items
	 */
	public List<CaseComorbidity> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<CaseComorbidity> items) {
		this.items = items;
	}

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
}