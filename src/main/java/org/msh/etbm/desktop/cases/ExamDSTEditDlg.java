package org.msh.etbm.desktop.cases;

import java.awt.*;
import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.ExamDST;
import org.msh.etbm.entities.ExamDSTResult;
import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.enums.DstResult;
import org.msh.etbm.services.SubstanceServices;
import org.msh.etbm.services.cases.CaseCloseService;
import org.msh.etbm.services.cases.ExamDSTServices;
import org.msh.xview.FormDataModel;

/**
 * Dialog window to display the DST exam result form for editing
 * of a result of a case or inserting a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ExamDSTEditDlg extends CaseDataEditDlg<ExamDST>{
	private static final long serialVersionUID = -8524044367230857521L;

	public ExamDSTEditDlg() {
		super("examdst", "examdst", ExamDSTServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 */
	@Override
	public String getEntityTitle() {
		return Messages.getString("cases.examdst");
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#openForm()
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		super.initFormData(dataModel);

		// update list of substances in order to include all substances available
		ExamDST exam = (ExamDST)dataModel.getValue("examdst");
		List<Substance> lst = SubstanceServices.getSubstancesDst();
		for (Substance sub: lst) {
			ExamDSTResult res = null;
			for (ExamDSTResult item: exam.getResults()) {
				if (item.getSubstance().equals(sub)) {
					res = item;
					break;
				}
			}
			
			if (res == null) {
				res = new ExamDSTResult();
				res.setSubstance(sub);
				res.setResult(DstResult.NOTDONE);
				res.setExam(exam);
				exam.getResults().add(res);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		ExamDSTServices srv = App.getComponent(ExamDSTServices.class);
		ExamDST exam = (ExamDST)dataModel.getValue("examdst");

		if (!srv.validate(exam, getForm()))
			return false;

		return super.saveFormData(dataModel);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getFormDimension()
	 */
	@Override
	protected Dimension getFormSize() {
		Dimension d = super.getFormSize();
		d.setSize(650, 580);
		return d;
	}


}
