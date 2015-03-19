package org.msh.etbm.services.cases;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.LaboratoryExamResult;
import org.msh.etbm.services.core.EntityServicesImpl;

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

}
