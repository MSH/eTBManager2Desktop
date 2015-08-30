package org.msh.etbm.desktop.cases;

import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.services.cases.MedicalExaminationServices;

import java.awt.*;

/**
 * Dialog window to display the medical examination form for editing an existing
 * medical examination from a case or entering a new one 
 * 
 * @author Ricardo Memoria
 *
 */
public class MedExamEditDlg extends CaseDataEditDlg<MedicalExamination> {
	private static final long serialVersionUID = 6017604432430346403L;

	/**
	 * Default constructor
	 */
	public MedExamEditDlg() {
		super("medexam", "medicalexamination", MedicalExaminationServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(650, 550);
		return d;
	}

}
