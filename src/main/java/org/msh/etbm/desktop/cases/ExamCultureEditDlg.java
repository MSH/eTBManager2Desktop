package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.services.cases.ExamCultureServices;

import java.awt.*;

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

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(870, 550);
		return d;
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Integer getPreferredWidth() {
		return 800;
	}

}
