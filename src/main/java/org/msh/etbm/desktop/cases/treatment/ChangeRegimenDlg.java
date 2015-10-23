/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import org.msh.etbm.controller.StartTreatmentController;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.eventbus.EventBusService;
import org.msh.xview.FormDataModel;

import java.awt.*;

/**
 * Dialog window to change treatment regimen of a case.
 * 
 * @author Mauricio Santos
 *
 */
public class ChangeRegimenDlg extends GenericFormDialog {

	private static final long serialVersionUID = 4846236456428415013L;

	private Integer caseId;

	/**
	 * Default constructor
	 */
	public ChangeRegimenDlg() {
		// set the form name
		super("change_regimen");
		// set the title of the dialog
		setTitle(App.getMessage("cases.regimens.change"));
		// set the size of the form
		getForm().getFormUI().setWidth(650);
		// whole form is executed inside a transaction (avoid lazy initialization exception in Hibernate)
//		setInTransaction(true);
	}

	/**
	 * Open the form and if case is closed, return true
	 * @return
	 */
	public static boolean execute(Integer caseId) {
		ChangeRegimenDlg dlg = new ChangeRegimenDlg();
		return dlg.openForm(caseId);
	}


	/**
	 * Initialize the case and show the form to be edited
	 * @param caseid
	 * @return
	 */
	protected boolean openForm(Integer caseId) {
		this.caseId = caseId;
		return showModal();
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		dataModel.setValue("tbcase", CaseServices.instance().findEntity(caseId));
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		// change regimen
		//ChangeRegimenController controller = getForm().getDataModel().getVariable(ChangeRegimenController.class);
		StartTreatmentController controller = getForm().getDataModel().getVariable(StartTreatmentController.class);
		TbCase tbcase = getForm().getDataModel().getVariable(TbCase.class);
		controller.changeRegimen(tbcase);
		EventBusService.raiseEvent(AppEvent.TREATMENT_STARTED, tbcase);
		return true;
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		return new Dimension(700, 520);
	}

}
