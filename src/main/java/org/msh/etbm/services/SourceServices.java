/**
 * 
 */
package org.msh.etbm.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.Regimen;
import org.msh.etbm.entities.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Services exposed to handle a {@link Source} entity
 * @author Ricardo Memoria
 *
 */
@Component
public class SourceServices {

	@Autowired EntityManager entityManager;
	
	/**
	 * Return the list of sources of the current workspace
	 * @return list of {@link Regimen} instances
	 */
	public List<Source> getSources() {
		return entityManager
				.createQuery("from Source where workspace.id=#{defaultWorkspace.id}")
				.getResultList();
	}

}
