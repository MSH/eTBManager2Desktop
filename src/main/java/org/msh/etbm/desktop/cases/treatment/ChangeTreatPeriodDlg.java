/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import org.msh.etbm.controller.ChangeTreatPeriodController;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.TbCase;
import org.msh.xview.FormDataModel;

/**
 * Dialog window to change the treatment period of a TB case
 * 
 * @author Ricardo Memoria
 *
 */
public class ChangeTreatPeriodDlg extends GenericFormDialog {
	private static final long serialVersionUID = -4114240147218109304L;

	private int caseId;
	private ChangeTreatPeriodController controller;

	
	/**
	 * Default constructor
	 */
	public ChangeTreatPeriodDlg() {
		super("changeperiod");
		setTitle(Messages.getString("cases.regimens.changeperiod"));
		getForm().getFormUI().setWidth(500);

		controller = App.getComponent(ChangeTreatPeriodController.class);
	}


	/**
	 * Open the form and return true if the treatment period was successfully changed
	 * @param caseId is the ID of the case to edit the treatment period
	 * @return true if the period was successfully changed
	 */
	public static boolean execute(Integer caseId) {
		ChangeTreatPeriodDlg dlg = new ChangeTreatPeriodDlg();
		dlg.caseId = caseId;
		return dlg.showModal();
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		TbCase tbcase = App.getEntityManager().find(TbCase.class, caseId);
		controller.setTbcase(tbcase);

		controller.initialize();

		dataModel.setValue("controller", controller);
	}


	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		controller.changePeriod();
		return true;
	}

}
