package org.msh.etbm.services;

import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Substance;

/**
 * Common services related to substances (class {@link Substance} )
 * 
 * @author Ricardo Memoria
 *
 */
public class SubstanceServices {

	/**
	 * Return a list of {@link Substance} objects
	 * @return
	 */
	public static List<Substance> getSubstances() {
		return App.getEntityManager()
				.createQuery("from Substance where workspace.id = #{defaultWorkspace.id} " +
						"order by name.name1")
				.getResultList();
	}


	/**
	 * Return a list of {@link Substance} objects
	 * @return
	 */
	public static List<Substance> getSubstancesDst() {
		return App.getEntityManager()
				.createQuery("from Substance where workspace.id = #{defaultWorkspace.id} " +
						"and dstResultForm = :p1 " +
						"order by prevTreatmentOrder")
				.setParameter("p1", true)
				.getResultList();
	}

	/**
	 * Return a list of {@link Substance} objects
	 * @return
	 */
	public static List<Substance> getSubstancesPrevTreat() {
		return App.getEntityManager()
				.createQuery("from Substance where workspace.id = #{defaultWorkspace.id} " +
						"and prevTreatmentForm = :p1 " +
						"order by prevTreatmentOrder")
				.setParameter("p1", true)
				.getResultList();
	}

}
