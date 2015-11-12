package org.msh.etbm.custom.bd;

import org.msh.etbm.custom.bd.entities.ExamSkin;
import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.entities.enums.ExamStatus;
import org.msh.etbm.services.cases.LaboratoryExamServices;
import org.springframework.stereotype.Component;

/**
 * Created by Mauricio on 12/11/2015.
 */
@Component
public class ExamSkinServices extends LaboratoryExamServices<ExamSkin> {

    /*
 * (non-Javadoc)
 *
 * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
 */
    @Override
    public void save(ExamSkin entity) {
        super.save(entity);
    }

}
