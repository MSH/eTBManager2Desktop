package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamMicroscopy;
import org.msh.etbm.services.cases.ExamMicroscopyServices;

/**
 * Dialog window to display the microscopy exam result form for editing
 * of a result of a case or inserting a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ExamMicroscopyEditDlg extends CaseDataEditDlg<ExamMicroscopy> {
	private static final long serialVersionUID = 6563950563795605793L;

	public ExamMicroscopyEditDlg() {
		super("exammicroscopy", "exammicroscopy", ExamMicroscopyServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.exammicroscopy");
	}


}
