/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Dimension;
import java.util.List;

import org.msh.etbm.controller.StartTreatmentController;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.login.UserSession;
import org.msh.eventbus.EventBusService;
import org.msh.xview.FormDataModel;

/**
 * Dialog window to start a new standard treatment for a case.
 * 
 * @author Ricardo Memoria
 *
 */
public class StartIndividualizedTreatmentDlg extends GenericFormDialog {

	private static final long serialVersionUID = 4846236456428415013L;

	private Integer caseId;
	
	/**
	 * Default constructor
	 */
	public StartIndividualizedTreatmentDlg() {
		// set the form name
		super("start_individualized_treatment");
		// set the title of the dialog
		setTitle(App.getMessage("cases.details.starttreatment") + " - " + App.getMessage("regimens.individualized"));
		// set the size of the form
		getForm().getDataModel().setValue("form", this);
		getForm().getFormUI().setPreferredWidth(680);
		// whole form is executed inside a transaction (avoid lazy initialization exception in Hibernate)
//		setInTransaction(true);
	}

	/**
	 * Open the form and if case is closed, return true
	 * @return
	 */
	public static boolean execute(Integer caseId) {
		StartIndividualizedTreatmentDlg dlg = new StartIndividualizedTreatmentDlg();
		return dlg.openForm(caseId);
	}


	/**
	 * Initialize the case and show the form to be edited
	 * @param caseid
	 * @return
	 */
	protected boolean openForm(Integer caseId) {
		this.caseId = caseId;
/*		JButton btnIntensive = new JButton(App.getMessage("cases.regimens.addmed") +  "...");
		btnIntensive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectMedicinesToAdd(RegimenPhase.INTENSIVE);
			}
		});
		btnIntensive.setSize(btnIntensive.getPreferredSize());
		getForm().setComponent("btnIntensive", btnIntensive);

		JButton btnContinuous = new JButton(App.getMessage("cases.regimens.addmed") +  "...");
		btnContinuous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectMedicinesToAdd(RegimenPhase.CONTINUOUS);
			}
		});
		getForm().setComponent("btnContinuous", btnContinuous);
		btnContinuous.setSize(btnContinuous.getPreferredSize());
*/		return showModal();
	}

	
	/**
	 * Open dialog to select medicines to be added to the continuous phase
	 */
	public void selectMedicinesIntensivePhase() {
		selectMedicinesToAdd(RegimenPhase.INTENSIVE);
	}
	
	/**
	 * Open dialog to select medicines to be added to the continuous phase
	 */
	public void selectMedicinesContinuousPhase() {
		selectMedicinesToAdd(RegimenPhase.CONTINUOUS);
	}
	
	/**
	 * Show the dialog for medicines selection and include them in the corresponding phase 
	 * @param phase indicate in which phase of the treatment the medicines will be included
	 */
	protected void selectMedicinesToAdd(RegimenPhase phase) {
		List<Medicine> meds = MedicineSelectionDlg.execute(null);
		if (meds == null)
			return;

		StartTreatmentController controller = getForm().getDataModel().getVariable(StartTreatmentController.class);
		for (Medicine med: meds) {
			controller.addMedicine(med, phase);
		}
		getForm().getFormUI().update();
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
		// start the treatment
		StartTreatmentController controller = getForm().getDataModel().getVariable(StartTreatmentController.class);
		TbCase tbcase = getForm().getDataModel().getVariable(TbCase.class);
		controller.setTreatmentUnit(UserSession.getUserWorkspace().getTbunit());
		controller.startStandardTreatmentRegimen(tbcase);
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
