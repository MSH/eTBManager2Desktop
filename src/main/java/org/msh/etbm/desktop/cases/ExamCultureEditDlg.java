package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.services.cases.ExamCultureServices;

/**
 * Dialog window to display the culture exam result form for editing
 * of a result of a case or inserting a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ExamCultureEditDlg extends CaseDataEditDlg<ExamCulture> {
	private static final long serialVersionUID = -2013446328026909137L;

	public ExamCultureEditDlg() {
		super("examculture", "examculture", ExamCultureServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.examculture");
	}

}
