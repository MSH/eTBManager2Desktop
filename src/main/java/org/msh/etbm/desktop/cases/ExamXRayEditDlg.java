package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamXRay;
import org.msh.etbm.services.cases.ExamXRayServices;

/**
 * Dialog window to display the x-ray form for editing
 * of an x-ray of a case or inserting a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ExamXRayEditDlg extends CaseDataEditDlg<ExamXRay> {
	private static final long serialVersionUID = 7522114090077177852L;

	public ExamXRayEditDlg() {
		super("examxray", "examxray", ExamXRayServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.examxray");
	}


}
