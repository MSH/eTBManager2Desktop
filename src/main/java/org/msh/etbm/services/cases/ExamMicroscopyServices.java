package org.msh.etbm.services.cases;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.ExamMicroscopy;
import org.msh.etbm.entities.SynchronizableEntity;
import org.msh.etbm.entities.WSObject;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.ExamStatus;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class ExamMicroscopyServices extends LaboratoryExamServices<ExamMicroscopy> {

    /*
    * (non-Javadoc)
    *
    * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
    */
    @Override
    public void save(ExamMicroscopy entity) {
        //If status is not performed, clear results
        if(!entity.getStatus().equals(ExamStatus.PERFORMED)){
            entity.setDateRelease(null);
            entity.setResult(null);
            entity.setNumberOfAFB(null);
            entity.setMethod(null);
            entity.setComments(null);
        }

        super.save(entity);
    }

}
