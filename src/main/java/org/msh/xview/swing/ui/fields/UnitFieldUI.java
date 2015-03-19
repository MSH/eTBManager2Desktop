/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.util.List;

import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.services.units.TBUnitFilter;
import org.msh.etbm.services.units.TBUnitUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class UnitFieldUI extends BaseAdminUnitFieldUI {


	/** {@inheritDoc}
	 * @see org.msh.xview.swing_old.editors.AbstractEditor#getDisplayString(org.msh.xview.swing_old.ui.FieldUI)
	 */
	@Override
	public String getDisplayText() {
		Object val = getValue();
		
		if (val == null)
			return "";

		Tbunit unit = (Tbunit)val;
		
		return unit.getName().toString() + '\n' + unit.getAdminUnit().getFullDisplayName();
	}


	/** {@inheritDoc}
	 */
	@Override
	protected List getItems(AdministrativeUnit selectedAU) {
		List<Tbunit> lst = TBUnitUtils.getUnits(selectedAU, TBUnitFilter.HEALTH_UNITS, true, true);
		return lst;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected AdministrativeUnit getAdminUnit(Object value) {
		return ((Tbunit)value).getAdminUnit();
	}


}
