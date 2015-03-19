package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.services.SubstanceServices;
import org.msh.etbm.services.cases.SideEffectServices;


/**
 * Dialog window to display the adverse reaction form, allowing user to edit an existing
 * adverse reaction or entering a new one for an specific case/suspect
 * 
 * @author Ricardo Memoria
 *
 */
public class SideEffectEditDlg extends CaseDataEditDlg {
	private static final long serialVersionUID = 5852839667217977418L;

	/**
	 * Default constructor that defines the form name and variable to use
	 */
	public SideEffectEditDlg() {
		super("sideeffect_edt", "sideeffect", SideEffectServices.class);
		getForm().getDataModel().setValue("substances", SubstanceServices.getSubstances());
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.comorbidities");
	}

	/** {@inheritDoc}
	 */
/*	@Override
	protected Dimension getFormDimension() {
		return new Dimension(600, 400);
	}
*/
}
