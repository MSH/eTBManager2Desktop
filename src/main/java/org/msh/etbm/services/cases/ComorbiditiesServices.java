package org.msh.etbm.services.cases;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.CaseComorbidity;
import org.msh.etbm.entities.FieldValue;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.misc.FieldsOptions;
import org.msh.utils.ItemSelectList;
import org.springframework.stereotype.Component;

@Component
public class ComorbiditiesServices {

	/**
	 * Initialize the list of case comorbidities
	 * @param tbcase
	 */
	public ItemSelectList<CaseComorbidity> getListForEditing(TbCase tbcase) {
		// make sure it's created
		FieldsOptions options = App.getComponent(FieldsOptions.class);
		List<FieldValue> lst = options.getComorbidities();

		ItemSelectList<CaseComorbidity> comorbidities = new ItemSelectList<CaseComorbidity>();
		
		for (FieldValue fld: lst) {
			// search in the case
			CaseComorbidity caseCom = null;
			for (CaseComorbidity cc: tbcase.getComorbidities())
				if (cc.getComorbidity().equals(fld)) {
					caseCom = cc;
					break;
				}

			if (caseCom == null) {
				caseCom = new CaseComorbidity();
				caseCom.setComorbidity(fld);
			}
			
			comorbidities.add(caseCom, caseCom.getTbcase() != null);
		}
		return comorbidities;
	}


	/**
	 * Update the list of comorbidities in a TB case
	 * @param tbcase instance of {@link TbCase}
	 * @param comorbidities list of {@link CaseComorbidity} instances
	 */
	public void saveComorbidities(TbCase tbcase, ItemSelectList<CaseComorbidity> comorbidities) {
		// remove items not in case
		int i = 0;
		while (i < tbcase.getComorbidities().size()) {
			CaseComorbidity cc = tbcase.getComorbidities().get(i);
			if (!comorbidities.isSelected(cc)) {
				tbcase.getComorbidities().remove(cc);
				App.getEntityManager().remove(cc);
			}
			else i++;
		}
		
		// include new items
		for (CaseComorbidity item: comorbidities.getSelectedItems()) {
			if (!tbcase.getComorbidities().contains(item)) {
				tbcase.getComorbidities().add(item);
				item.setTbcase(tbcase);
				App.getEntityManager().persist(item);
			}
		}

		//Set edited itens to be sync
		for (CaseComorbidity item: comorbidities.getSelectedItems()) {
			if (tbcase.getComorbidities().contains(item)) {
				item.getSyncData().setChanged(true);
				App.getEntityManager().merge(item);
			}
		}
	}

}
