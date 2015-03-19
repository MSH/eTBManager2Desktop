/**
 * 
 */
package org.msh.etbm.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.Regimen;
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
	 * Return the list of regimens of the current workspace
	 * @return list of {@link Regimen} instances
	 */
	public List<Regimen> getRegimens() {
		return entityManager
				.createQuery("from Regimen where workspace.id=#{defaultWorkspace.id} order by name")
				.getResultList();
	}
}
