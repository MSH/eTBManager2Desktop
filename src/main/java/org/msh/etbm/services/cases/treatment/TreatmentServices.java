package org.msh.etbm.services.cases.treatment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.*;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.MedicineLine;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services available to handle a case treatment, like start a new treatment,
 * finish a treatment, undo treatment, etc.
 * 
 * @author Ricardo Memoria
 *
 */
@Service
public class TreatmentServices {

	@Autowired EntityManager em;
	@Autowired TreatmentPeriodUtilities utils;

	
	/**
	 * Add a medicine to the treatment of a case. The medicine must be prescribed inside the treatment period
	 * @param tbcase instance of {@link TbCase} that medicine will be prescribed
	 * @param medicine instance of {@link Medicine} to be included in the treatment
	 * @param period period inside the treatment
	 * @param doseUnit the dose unit of the prescribed medicine
	 * @param frequency the weekly frequency of the prescribed medicine
	 * @param source the instance of {@link Source} for the medicine
	 * @param comments any comments related to this prescription (not required)
	 * @return instance of the {@link PrescribedMedicine} 
	 */
	public PrescribedMedicine addPrescribedMedicine(TbCase tbcase, Medicine medicine, Period period, int doseUnit, 
			int frequency, Source source, String comments) {
		if (!period.isInside( tbcase.getTreatmentPeriod() ))
			throw new IllegalArgumentException("The period must be inside the treatment period");

		// cut the period where the prescribed medicine is available
		utils.cutMedicinePeriod(tbcase, medicine, period, null);

		PrescribedMedicine pm = new PrescribedMedicine();
		pm.setTbcase(tbcase);
		pm.setMedicine(medicine);
		pm.setPeriod(period);
		pm.setDoseUnit(doseUnit);
		pm.setFrequency(frequency);
		pm.setSource(source);
		pm.setComments(comments);
		tbcase.getPrescribedMedicines().add(pm);
		tbcase.getSyncData().setChanged(true);
		
		em.persist(pm);
		em.flush();

		if(checkregimenMovedToIndivid(tbcase)){
			tbcase.setRegimen(null);
			em.persist(tbcase);
			em.flush();
		}

		return pm;
	}

	
	/**
	 * Remove a prescribed medicine from the treatment
	 * @param pm instance of the {@link PrescribedMedicine} class
	 */
	public void removePrescribedMedicine(PrescribedMedicine pm) {
		TbCase tbcase = pm.getTbcase();
		
		tbcase.getPrescribedMedicines().remove(pm);

		EntityManager em = App.getEntityManager();
		if (em.contains(pm))
			em.remove(pm);

		tbcase.setRegimen(null);
		updateTreatmentPeriod(tbcase);
		em.flush();

		if(checkregimenMovedToIndivid(tbcase)){
			tbcase.setRegimen(null);
			em.persist(tbcase);
			em.flush();
		}
	}


	/**
	 * Update treatment period based on the periods of the prescribed medicines
	 */
	private void updateTreatmentPeriod(TbCase tbcase) {
		Date dtini = null;
		Date dtend = null;
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			Period p = pm.getPeriod();
			
			if ((dtini == null) || (p.getIniDate().before(dtini)))
				dtini = p.getIniDate();
			if ((dtend == null) || (p.getEndDate().after(dtend)))
				dtend = p.getEndDate();
		}
		
		// update treatment health unit periods
		if ((dtini != null) && (dtend != null)) {
			tbcase.setTreatmentPeriod( new Period(dtini, dtend) );

			List<TreatmentHealthUnit> lst = tbcase.getSortedTreatmentHealthUnits();
			TreatmentHealthUnit firstHU = lst.get(0);
			TreatmentHealthUnit lastHU = lst.get( lst.size() - 1 );
			
			firstHU.getPeriod().setIniDate(dtini);
			lastHU.getPeriod().setEndDate(dtend);
		}
	}
	

	
	/**
	 * Undo the treatment, turning the case to the 'waiting for treatment' state again
	 */
	public void undoTreatment(TbCase tbcase) {
		if ((tbcase.getState() != CaseState.ONTREATMENT) && (tbcase.getState() != CaseState.TRANSFERRING))
			throw new IllegalAccessError("Case must be on treatment in order to undo the treatment");

		tbcase.setState(CaseState.WAITING_TREATMENT);
		tbcase.setTreatmentPeriod(null);
		tbcase.setOwnerUnit(null);
		tbcase.setIniContinuousPhase(null);
		tbcase.setRegimen(null);
		tbcase.setRegimenIni(null);
		tbcase.setInitialRegimenWithSecondLineDrugs(null);
		tbcase.setTreatmentCategory(null);

		EntityManager em = App.getEntityManager();

		List<PrescribedMedicine> pms = em.createQuery(" from PrescribedMedicine where tbcase.id = " + tbcase.getId().toString()).getResultList();
		for(PrescribedMedicine pm : pms) {
			if (pm.getSyncData()!= null && pm.getSyncData().getServerId() != null){
				DeletedEntity ent = new DeletedEntity();
				ent.setEntityId(pm.getSyncData().getServerId());
				ent.setEntityName(pm.getClass().getSimpleName());
				App.getEntityManager().persist(ent);
			}
		}
		em.createQuery("delete from PrescribedMedicine where tbcase.id = " + tbcase.getId().toString()).executeUpdate();

		List<TreatmentHealthUnit> thus = em.createQuery(" from TreatmentHealthUnit where tbcase.id = " + tbcase.getId().toString()).getResultList();
		for(TreatmentHealthUnit thu : thus){
			if (thu.getSyncData()!= null && thu.getSyncData().getServerId() != null) {
				DeletedEntity ent = new DeletedEntity();
				ent.setEntityId(thu.getSyncData().getServerId());
				ent.setEntityName(thu.getClass().getSimpleName());
				App.getEntityManager().persist(ent);
			}
		}
		em.createQuery("delete from TreatmentHealthUnit where tbcase.id = " + tbcase.getId().toString()).executeUpdate();
		
		tbcase.getHealthUnits().clear();
		tbcase.getPrescribedMedicines().clear();

		tbcase.setOwnerUnit(tbcase.getNotificationUnit());

		em.persist(tbcase);
		em.flush();

		em.createQuery("delete from TreatmentMonitoring tm where tm.tbcase.id = :caseId")
				.setParameter("caseId", tbcase.getId())
				.executeUpdate();
	}

	
	/**
	 * Change the treatment regimen of a case. All information about the previous regimen is erased and
	 * a new regimen is set to the case
	 * @param tbcase
	 * @param regimen
	 */
	public void changeTreatmentRegimen(TbCase tbcase, Regimen regimen, List<MedicineTreatmentInfo> medsIntPhase, 
			List<MedicineTreatmentInfo> medsContPhase) {
		if ((tbcase.getState() != CaseState.ONTREATMENT) && (tbcase.getState() != CaseState.TRANSFERRING))
			throw new IllegalArgumentException("Treatment cannot be change if case is not on treatment");

		Tbunit treatmentUnit = tbcase.getOwnerUnit();
		Date iniDate = tbcase.getTreatmentPeriod().getIniDate();
		Regimen iniRegimen = tbcase.getRegimen();

		undoTreatment(tbcase);

		StartTreatmentServices srv = App.getComponent(StartTreatmentServices.class);
		srv.startStandardRegimen(tbcase, treatmentUnit, iniDate, regimen, medsIntPhase, medsContPhase);

		tbcase.setRegimenIni(iniRegimen);
		CaseServices.instance().save(tbcase);
		em.flush();
	}


	/**
	 * Check if changes in the medicine turned the regimen to an individualized
	 */
	public boolean checkregimenMovedToIndivid(TbCase tbcase) {
		Regimen reg = tbcase.getRegimen();
		
		if (reg == null)
			return true;

		EntityManager entityManager = App.getEntityManager();

		// get list of medicines in the regimen
		List<Integer> lst = entityManager.createQuery("select s.substance.id from Regimen r, in(r.medicines) med, in(med.medicine.components) s " +
				"where r.id = :id and med.medicine.line <> :line")
				.setParameter("id", reg.getId())
				.setParameter("line", MedicineLine.OTHER)
				.getResultList();

		// get list of medicines prescribed
		List<Medicine> meds = new ArrayList<Medicine>();
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			if ((pm.getMedicine().getLine() != MedicineLine.OTHER) && (!meds.contains(pm.getMedicine()))) {
				meds.add(pm.getMedicine());
			}
		}

		if (meds.size() == 0)
			return false;
		
		// create query to select substances of the regimen
		String s = "";
		for (Medicine med: meds) {
			String sid = med.getId().toString();
			if (!s.contains(sid)) {
				if (!s.isEmpty())
					s += ",";
				s += med.getId().toString();
			}
		}
		s = "(" + s + ")";
		List<Integer> subs = entityManager.createQuery("select comp.substance.id from MedicineComponent comp where comp.medicine.id in " + s)
			.getResultList();

		for (Integer id: subs) {
			if (!lst.contains(id))
				return true;
		}
		
		for (Integer id: lst) {
			if (!subs.contains(id))
				return true;
		}
		
		return false;
	}

	
	/**
	 * Change the treatment period
	 * @param iniDate
	 * @param iniContinuousPhat
	 * @param endDate
	 */
	public void changeTreatmentPeriod(TbCase tbcase, Date iniDate, Date iniContinuousPhase, Date endDate) {
		if ((!iniDate.before(iniContinuousPhase)) && (!endDate.after(iniContinuousPhase))) {
			throw new RuntimeException("The initial continuous phase must be inside the treatment period");
		}

		Period p = new Period(iniDate, endDate);
		if ((p.equals(tbcase.getTreatmentPeriod())) && (iniContinuousPhase.equals(tbcase.getIniContinuousPhase()))) {
			// there is nothing to do, so exit
			return;
		}

		List<PrescribedMedicine> lst;
		if (tbcase.getIniContinuousPhase() != null) {
			Period intPeriod = new Period(iniDate, DateUtils.incDays(iniContinuousPhase, -1));
			Period conPeriod = new Period(iniContinuousPhase, endDate);

			utils.splitPeriod(tbcase, tbcase.getIniContinuousPhase());
			lst = utils.updatePeriod(tbcase, RegimenPhase.INTENSIVE, intPeriod);
			List<PrescribedMedicine> lst2 = utils.updatePeriod(tbcase, RegimenPhase.CONTINUOUS, conPeriod);
			lst.addAll(lst2);
		}
		else {
			lst = utils.updatePeriod(tbcase, RegimenPhase.INTENSIVE, new Period(iniDate, endDate));
			utils.splitPeriod(tbcase, iniContinuousPhase);
		}
		
		for (PrescribedMedicine pm: lst) {
			em.remove(pm);
		}

		// change the treatment period inside the case
		tbcase.setTreatmentPeriod(p);
		tbcase.setIniContinuousPhase(iniContinuousPhase);
		
		TreatmentHealthUnit healthUnit = tbcase.getHealthUnits().get(tbcase.getHealthUnits().size() - 1);
		healthUnit.setPeriod(p);
		healthUnit.getSyncData().setChanged(true);

		em.persist(tbcase);
	}
}
