/**
 * 
 */
package org.msh.etbm.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * List of available and stateless services to medicines entities 
 * @author Ricardo Memoria
 *
 */
@Component
public class MedicineServices {

	@Autowired EntityManager em;
	
	/**
	 * Return the list of medicines available for the current workspace
	 * @return list of {@link Medicine} objects
	 */
	public List<Medicine> getMedicines() {
		return em
			.createQuery("from Medicine where workspace.id = #{defaultWorkspace.id} order by genericName.name1")
			.getResultList();
	}
}
