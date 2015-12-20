package org.msh.etbm.services.cases;

import org.msh.etbm.custom.bd.entities.CaseSideEffectBD;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.CaseSideEffect;
import org.msh.etbm.entities.SynchronizableEntity;
import org.msh.etbm.entities.WSObject;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.services.core.EntityServicesImpl;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class SideEffectServices extends EntityServicesImpl<CaseSideEffect> {
    /*
	 * (non-Javadoc)
	 *
	 * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
	 */
    @Override
    public void save(CaseSideEffect entity) {
        if(entity instanceof CaseSideEffectBD && entity != null){
            entity.setMonth(entity.getTbcase().getMonthTreatment(((CaseSideEffectBD) entity).getEffectSt()));
        }
        super.save(entity);
    }
}
