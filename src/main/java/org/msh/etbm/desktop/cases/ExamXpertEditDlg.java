/**
 * 
 */
package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.ExamXpert;
import org.msh.etbm.services.cases.ExamXpertServices;

import java.awt.*;


/**
 * @author Ricardo Memoria
 *
 */
public class ExamXpertEditDlg  extends CaseDataEditDlg<ExamXpert> {
	private static final long serialVersionUID = 7810683147180633960L;

	/**
	 * @param formName
	 * @param entityVariableName
	 * @param entityServiceClass
	 */
	public ExamXpertEditDlg() {
		super("examxpert", "examxpert", ExamXpertServices.class);
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getEntityTitle() {
		return App.getMessage("cases.examxpert");
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(650, 510);
		return d;
	}

}
