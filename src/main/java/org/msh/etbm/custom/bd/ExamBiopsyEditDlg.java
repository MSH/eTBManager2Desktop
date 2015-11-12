package org.msh.etbm.custom.bd;

import org.msh.etbm.custom.bd.entities.ExamBiopsy;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.cases.CaseDataEditDlg;

import java.awt.*;

/**
 * Created by Mauricio on 12/11/2015.
 */
public class ExamBiopsyEditDlg extends CaseDataEditDlg<ExamBiopsy> {
    private static final long serialVersionUID = -2013446328026909137L;

    public ExamBiopsyEditDlg() {
        super("exambiopsy", "exambiopsy", ExamBiopsyServices.class);
    }

    /** {@inheritDoc}
     * @see CaseDataEditDlg#getEntityName()
     */
    @Override
    public String getEntityTitle() {
        return Messages.getString("cases.exambiopsy");
    }

    /** {@inheritDoc}
     * @see CaseDataEditDlg#getFormDimension()
     */
    @Override
    protected Dimension getFormSize() {
        Dimension d = super.getFormSize();
        d.setSize(650, 550);
        return d;
    }
}
