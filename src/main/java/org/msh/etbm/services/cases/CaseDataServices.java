package org.msh.etbm.services.cases;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.CaseData;
import org.msh.etbm.services.core.EntityServicesImpl;

/**
 * Base abstract service to entities that are related to a TB case
 * 
 * @author Ricardo Memoria
 *
 * @param <E> is the class that extends {@link CaseData}
 */
public class CaseDataServices<E extends CaseData> extends EntityServicesImpl<E> {

	/**
	 * Return the list of objects from the case 
	 * @param caseId is the ID of the case
	 * @return List of object of {@link CaseData} class
	 */
	public List<E> getList(Integer caseId) {
		String hql = "from " + getEntityClass().getName() + " where tbcase.id = :id order by date desc";

		return App.getEntityManager()
				.createQuery(hql)
				.setParameter("id", caseId)
				.getResultList();
	}
}
