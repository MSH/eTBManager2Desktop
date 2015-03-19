package org.msh.etbm.desktop.cases;

import java.awt.Dimension;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.CaseComorbidity;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.cases.ComorbiditiesServices;
import org.msh.utils.ItemSelectList;
import org.msh.xview.FormDataModel;


/**
 * Dialog window to display the list of comorbidities of a case for user editing
 * 
 * @author Ricardo Memoria
 *
 */
public class ComorbiditiesDlg extends GenericFormDialog {
	private static final long serialVersionUID = 5852839667217977418L;

	private ComorbiditiesServices srv;
	private Integer caseId;

	/**
	 * Default constructor
	 */
	public ComorbiditiesDlg() {
		super("comorbidities_edt");
		srv = App.getComponent(ComorbiditiesServices.class);
	}

	
	/**
	 * Open the comorbidities form for editing
	 * @param caseId is the ID of the case
	 * @return true if the data was successfully saved, otherwise return false
	 * if the user canceled the operation
	 */
	public boolean openForm(Integer caseId) {
		if (caseId == null)
			throw new IllegalArgumentException("Case ID cannot be null");

		this.caseId = caseId;
		return showModal();
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		CaseServices caseSrv = App.getComponent(CaseServices.class);
		TbCase tbcase = caseSrv.findEntity(caseId);
		getForm().setValue("tbcase", tbcase);
		getForm().setValue("comorbidities", srv.getListForEditing(tbcase));
		getForm().getFormUI().setPreferredWidth(650);
		setTitle(Messages.getString("cases.comorbidities"));
		setFormSize(new Dimension(670, 480));
	}


	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		TbCase tbcase = dataModel.getVariable(TbCase.class);
		tbcase = App.getEntityManager().merge(tbcase);
		ItemSelectList<CaseComorbidity> lst = (ItemSelectList<CaseComorbidity>)dataModel.getValue("comorbidities");
		srv.saveComorbidities(tbcase, lst);
		return true;
	}
	

}
