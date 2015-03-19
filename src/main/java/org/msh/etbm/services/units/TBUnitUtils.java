package org.msh.etbm.services.units;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import javax.persistence.EntityManager;

import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.UserWorkspace;

public class TBUnitUtils {

	/**
	 * Return a list of {@link Tbunit} entities using different criterias
	 * @param adminUnit
	 * @param filter
	 * @param applyUserRestrictions
	 * @param applyHealthSystemRestrictions
	 * @return
	 */
	public static List<Tbunit> getUnits(AdministrativeUnit adminUnit, TBUnitFilter filter, boolean applyUserRestrictions, boolean applyHealthSystemRestrictions) {
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
		
		String hql = "select u from Tbunit u " +
				"where u.adminUnit.code like :code " +
				"and u.workspace.id = #{defaultWorkspace.id} " +
				"and u.active = :active " +
				(cond != null? cond: ""); 

//		if ((restriction != null) && (restriction.length() > 0))
//			hql = hql.concat(" and " + restriction);
		
		hql = hql + " order by u.name.name1";

		EntityManager em = App.getEntityManager();

		List<Tbunit> units = em.createQuery(hql)
				.setParameter("code", adminUnit.getCode() + "%")
				.setParameter("active", true)
				.getResultList();
		
		return units;
	}
}
