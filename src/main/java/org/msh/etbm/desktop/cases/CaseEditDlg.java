package org.msh.etbm.desktop.cases;

import java.awt.*;

import org.msh.etbm.services.cases.prevtreat.PrevTBTreatController;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericDialog;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.misc.ETB;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.swing.SwingFormContext;

/**
 * Dialog window to display the case/suspect general data form, allowing the user to
 * change the general data of the case.
 * 
 * @author Ricardo Memoria
 *
 */
public class CaseEditDlg extends GenericDialog {
	private static final long serialVersionUID = 7957443982626880044L;

	private SwingFormContext form;
	private PrevTBTreatController prevTBTreatController;
	
	public CaseEditDlg() {
		setTitle(Messages.getString("cases.edit")); //$NON-NLS-1$
		getClientContent().setLayout(new BorderLayout(0, 0));
		this.setResizable(true);
	}

	/**
	 * Create and open the form to edit a case
	 * @param caseid
	 * @return
	 */
	public static boolean execute(Integer caseid) {
		CaseEditDlg dlg = new CaseEditDlg();
		return dlg.showEdit(caseid);
	}

	/**
	 * Initialize the case and show the form to be edited
	 * @param caseid is the unique ID of the case
	 * @return true if the case was changed
	 */
	private boolean showEdit(Integer caseid) {
		form = GuiUtils.createForm("casenew");

		// do inside a transaction in order to avoid lazy initialization problems
		EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(caseid) {
			@Override
			public void execute(Integer caseid) {
                TbCase tbcase = CaseServices.instance().findEntity(caseid);
				prevTBTreatController = new PrevTBTreatController(tbcase);
				form.getDataModel().setValue("tbcase", tbcase);
//                form.getDataModel().setValue("patient", tbcase.getPatient());
				form.getDataModel().setValue("medicalexamination", ETB.newWorkspaceObject(MedicalExamination.class));
				form.getDataModel().setValue("prevTBTreatController", prevTBTreatController);
				form.getFormUI().setPreferredWidth(1360);
				form.getFormUI().update();
			}
			
		});

		getClientContent().add( form.getScrollPaneForm(), BorderLayout.CENTER );
		setBounds(100,100, 1400, 800);

		return showModal();
	}
	
	
	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.GenericDialog#commandOk()
	 */
	@Override
	public void commandOk() {
		if (!form.validate())
			return;

		// execute method inside a transaction
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				TbCase tbcase = (TbCase)form.getDataModel().getValue("tbcase");
				Patient patient = (Patient)form.getDataModel().getValue("patient");

                patient = App.getEntityManager().merge(patient);
				tbcase = App.getEntityManager().merge(tbcase);

				CaseServices.instance().save(tbcase);
				prevTBTreatController.save();
			}
			
		});
		super.commandOk();
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.GenericDialog#save()
	 */
	@Override
	public boolean save() {
		return true;
	}

}
