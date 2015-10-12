package org.msh.etbm.services.cases;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.entities.LaboratoryExamResult;
import org.msh.etbm.services.core.EntityServicesImpl;
import org.msh.eventbus.EventBusService;

public class LaboratoryExamServices<E extends LaboratoryExamResult> extends EntityServicesImpl<E>{

	/**
	 * Return the list of objects from the case 
	 * @param caseId
	 * @return
	 */
	public List<E> getList(Integer caseId) {
		String hql = "from " + getEntityClass().getName() + " where tbcase.id = :id order by dateCollected desc";

		return App.getEntityManager()
				.createQuery(hql)
				.setParameter("id", caseId)
				.getResultList();
	}

	/*
    * (non-Javadoc)
    *
    * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
	*/
	@Override
	public void save(E entity) {
		EventBusService.raiseEvent(AppEvent.EXAMS_MODIFIED, entity);
		super.save(entity);
	}

}
