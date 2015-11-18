/**
 * 
 */
package org.msh.etbm.services.cases.treatment;

import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TreatmentMonitoring;
import org.msh.eventbus.EventBusService;
import org.springframework.stereotype.Component;

/**
 * Contain services to handle treatment follow up, which is basically
 * get information about the days of treatment and update its information
 *  
 * @author Ricardo Memoria
 *
 */
@Component
public class TreatmentFollowupServices {

	/**
	 * Load data about the treatment follow-up
	 * @param tbcase TB case to retrieve treatment data from
	 * @return instance of {@link TreatmentFollowupData}
	 */
	public TreatmentFollowupData loadTreatmentFollowup(TbCase tbcase) {
		EntityManager em = App.getEntityManager();
		List<TreatmentMonitoring> lst = em.createQuery("from TreatmentMonitoring c  " +
				"where c.tbcase.id = :id ")
				.setParameter("id", tbcase.getId())
				.getResultList();
		tbcase.getPrescribedMedicines();
		return new TreatmentFollowupData(tbcase, lst, tbcase.getPatient().getWorkspace());
	}
	
	/**
	 * Save treatment follow-up data for an specific case
	 * @param data instance of {@link TreatmentFollowupData} containing data about
	 * the case and its treatment data to be saved
	 */
	public void saveTreatmentFollowup(TreatmentFollowupData data, TbCase tbcase) {
		List<TreatmentMonitoring> lst = data.getUpdatedData();

		if (lst == null)
			return;

		EntityManager em = App.getEntityManager();
		for (TreatmentMonitoring tm: lst) {
			tm = em.merge(tm);
			em.persist(tm);
			data.replaceTreatmentMonitoringMonth(tm);
		}
		em.flush();

		TbCase c = em.find(TbCase.class, tbcase.getId());
		c.getSyncData().setChanged(true);
		em.persist(c);
		em.flush();

		data.getUpdatedData().clear();

		EventBusService.raiseEvent(AppEvent.CASES_REFRESH);
	}
}
