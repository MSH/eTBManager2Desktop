package org.msh.etbm.desktop.cases;

import java.awt.Dimension;
import java.util.Date;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.services.cases.CaseCloseService;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.xview.FormDataModel;

/**
 * Dialog window that display the case/suspect close form, allowing the user
 * to enter an outcome date and status of the case and close the case
 * 
 * @author Ricardo Memoria
 *
 */
public class CloseCaseDlg extends GenericFormDialog {
	private static final long serialVersionUID = -637623918570775853L;

	// the outcome date
	private Date date;
	// the outcome of the case
	private CaseState state;
	// the comment
	private String comment;
	// the id of the case
	private Integer caseId;
	// the tbcase being closed
	private TbCase tbcase;
	
	/**
	 * Create the dialog.
	 */
	public CloseCaseDlg() {
		super("caseclose");
		setTitle(Messages.getString("cases.close"));
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		tbcase = CaseServices.instance().findEntity(caseId);
		dataModel.setValue("controller", this);
		getForm().getDataModel().setValue("tbcase", tbcase);
		getForm().getFormUI().setPreferredWidth(400);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		CaseCloseService srv = App.getComponent(CaseCloseService.class);

		// get the variables
		TbCase tbcase = (TbCase)getForm().getValue("tbcase");
		
		tbcase = App.getEntityManager().merge(tbcase);

		// close the case
		srv.closeCase(tbcase, date, state, comment);
		return true;
	}

	/** {@inheritDoc}
	 */
	public boolean validateCloseDate() {
		if (tbcase.getTreatmentPeriod() != null && date.after(tbcase.getTreatmentPeriod().getEndDate())){
			return false;
		}
		return true;
	}


		/**
         * Open the form and if case is closed, return true
         * @return
         */
	public static boolean execute(Integer caseId) {
		CloseCaseDlg dlg = new CloseCaseDlg();
		return dlg.openForm(caseId);
	}


	/**
	 * Open the form to close a case with given ID
	 * @param caseId the ID of the case
	 * @return true if case was closed
	 */
	public boolean openForm(int caseId) {
		this.caseId = caseId;
		return showModal();
	}

/*	*//** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.GenericDialog#save()
	 *//*
	@Override
	public boolean save() {
		if (!getForm().validate())
			return false;

		CaseCloseService srv = App.getComponent(CaseCloseService.class);
		TbCase tbcase = (TbCase)getForm().getValue("tbcase");
		if (srv.validateClose(tbcase, date, getForm()))
			return false;

		// close the case inside a transaction
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				// get the variables
				TbCase tbcase = (TbCase)getForm().getValue("tbcase");

				// close the case
				CaseCloseService srv = App.getComponent(CaseCloseService.class);
				srv.closeCase(tbcase, date, state, comment);
			}
		});
		
		return true;
	}
*/
	/**
	 * Return the possible outcomes of the case
	 * @return array of {@link CaseState} enumerations
	 */
	public CaseState[] getOutcomes() {
		CaseCloseService srv = App.getComponent(CaseCloseService.class);
		return srv.getOutcomes(tbcase.getClassification());
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the state
	 */
	public CaseState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(CaseState state) {
		this.state = state;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(d.getWidth(), d.getHeight() + 30);
		return d;
	}
}
