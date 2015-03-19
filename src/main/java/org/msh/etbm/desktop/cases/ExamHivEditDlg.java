package org.msh.etbm.desktop.cases;

import java.awt.Dimension;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamHIV;
import org.msh.etbm.services.cases.ExamHivServices;

/**
 * Dialog window to display the HIV exam result form for editing
 * of a result of a case or inserting a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ExamHivEditDlg extends CaseDataEditDlg<ExamHIV>{
	private static final long serialVersionUID = -4692150879801306323L;

	public ExamHivEditDlg() {
		super("examhiv", "examhiv", ExamHivServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.examhiv");
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(650, d.getHeight() + 50);
		return d;
	}


}
