package org.msh.etbm.services;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Laboratory;
import org.msh.etbm.entities.UserWorkspace;

public class LabServices {

	/**
	 * Return a list of {@link Laboratory} entities using different criterias
	 * @param adminUnit
	 * @param filter
	 * @param applyUserRestrictions
	 * @param applyHealthSystemRestrictions
	 * @return
	 */
	public static List<Laboratory> getLaboratories(AdministrativeUnit adminUnit, boolean applyUserRestrictions, boolean applyHealthSystemRestrictions) {
		// add dynamic condition by health system
		Integer healthSystemID = null;
		if (applyHealthSystemRestrictions) {
			UserWorkspace userWorkspace = (UserWorkspace)App.getComponent("userWorkspace");
			if (userWorkspace.getHealthSystem() != null)
				healthSystemID = userWorkspace.getHealthSystem().getId();
		}

		String cond;
		if (healthSystemID != null)
			 cond = "and u.healthSystem.id = " + healthSystemID.toString();
		else cond = null;
		
		String hql = "select u from Laboratory u " +
				"where u.adminUnit.code like :code " +
				"and u.workspace.id = #{defaultWorkspace.id} " +
//				"and u.active = :active " +
				(cond != null? cond: ""); 

//		if ((restriction != null) && (restriction.length() > 0))
//			hql = hql.concat(" and " + restriction);
		
		hql = hql + " order by u.name";

		EntityManager em = App.getEntityManager();

		List<Laboratory> labs = em.createQuery(hql)
				.setParameter("code", adminUnit.getCode() + "%")
//				.setParameter("active", true)
				.getResultList();
		
		return labs;
	}

}
