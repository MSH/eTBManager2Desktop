package org.msh.etbm.services.cases;

import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.entities.ExamMicroscopy;
import org.msh.etbm.entities.enums.ExamStatus;
import org.springframework.stereotype.Component;

@Component
public class ExamCultureServices extends LaboratoryExamServices<ExamCulture> {

    /*
    * (non-Javadoc)
    *
    * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
    */
    @Override
    public void save(ExamCulture entity) {
        //If status is not performed, clear results
        if(!entity.getStatus().equals(ExamStatus.PERFORMED)){
            entity.setDateRelease(null);
            entity.setResult(null);
            entity.setMethod(null);
            entity.setComments(null);
            entity.setNumberOfColonies(null);
        }

        super.save(entity);
    }
}
