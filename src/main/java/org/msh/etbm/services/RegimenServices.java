/**
 * 
 */
package org.msh.etbm.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.Regimen;
import org.msh.etbm.entities.enums.CaseClassification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ricardo Memoria
 *
 */
@Component
public class RegimenServices {

	@Autowired EntityManager entityManager;

	/**
	 * Return the list of regimens of the current workspace based on the classification passed as parameter
	 * @param c Case Classification of the regimens wanted
	 * @return list of {@link Regimen} instances
	 */
	public List<Regimen> getRegimens(CaseClassification c) {
		return entityManager
				.createQuery("from Regimen where workspace.id=#{defaultWorkspace.id} and caseClassification = :classification order by name")
				.setParameter("classification", c)
				.getResultList();
	}
}
