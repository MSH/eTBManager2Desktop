/**
 * 
 */
package org.msh.etbm.services.cases;

import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.entities.ExamXpert;
import org.msh.etbm.entities.enums.ExamStatus;
import org.springframework.stereotype.Component;

/**
 * Extension of the laboratory services to the {@link ExamXpert} class
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class ExamXpertServices  extends LaboratoryExamServices<ExamXpert> {

    /*
    * (non-Javadoc)
    *
    * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
    */
    @Override
    public void save(ExamXpert entity) {
        //If status is not performed, clear results
        if(!entity.getStatus().equals(ExamStatus.PERFORMED)){
            entity.setDateRelease(null);
            entity.setResult(null);
            entity.setComments(null);
            entity.setRifResult(null);
        }

        super.save(entity);
    }

}
