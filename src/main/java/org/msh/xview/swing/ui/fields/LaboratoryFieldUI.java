/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.util.List;

import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Laboratory;
import org.msh.etbm.services.LabServices;

/**
 * @author Ricardo Memoria
 *
 */
public class LaboratoryFieldUI extends BaseAdminUnitFieldUI {

	/** {@inheritDoc}
	 * @see org.msh.xview.swing_old.editors.AbstractEditor#getDisplayString(org.msh.xview.swing_old.ui.FieldUI)
	 */
	@Override
	public String getDisplayText() {
		Object val = getValue();
		
		if (val == null)
			return "";

		Laboratory lab = (Laboratory)val;
		
		return lab.getName().toString() + '\n' + lab.getAdminUnit().getFullDisplayName();
	}


	/** {@inheritDoc}
	 */
	@Override
	protected List getItems(AdministrativeUnit selectedAU) {
		return LabServices.getLaboratories(selectedAU, true, true);
	}


	/** {@inheritDoc}
	 */
	@Override
	protected AdministrativeUnit getAdminUnit(Object value) {
		return ((Laboratory)value).getAdminUnit();
	}


}
