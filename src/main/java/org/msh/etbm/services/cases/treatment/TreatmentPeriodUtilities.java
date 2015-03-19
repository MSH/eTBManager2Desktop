/**
 * 
 */
package org.msh.etbm.services.cases.treatment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.PrescribedMedicine;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TreatmentHealthUnit;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Set of small services available to handle treatment period operations like spliting, merging,
 * cutting and creating new period.
 * @author Ricardo Memoria
 *
 */
@Component
public class TreatmentPeriodUtilities {
	
	@Autowired EntityManager em;
	
	/**
	 * Cut the treatment period for the given prescribed medicine. The prescription period
	 * of the medicine will be erased from the treatment, and new prescription periods will
	 * be created as requested
	 * @param med instance of {@link Medicine}
	 * @param period
	 */
	public void cutMedicinePeriod(TbCase tbcase, Medicine med, Period period, PrescribedMedicine pmToIgnore) {
		List<PrescribedMedicine> medsToDelete = new ArrayList<PrescribedMedicine>();
		
		List<PrescribedMedicine> lst = createPrescribedMedicineList(tbcase, med);
		for (PrescribedMedicine aux: lst) {
			if (aux == pmToIgnore) 
				continue;

			// prescription is inside new/edited one?
			if (period.contains(aux.getPeriod())) {
				tbcase.getPrescribedMedicines().remove(aux);
				if (em.contains(aux)) 
					em.remove(aux);
			}
			else
			// prescription is containing the whole new/edit prescription
			if (aux.getPeriod().contains(period)) {	
				PrescribedMedicine aux2 = clonePrescribedMedicine(aux);
				aux.getPeriod().setEndDate( DateUtils.incDays(period.getIniDate(), -1) );
				if (aux.getPeriod().getDays() <= 1)
					medsToDelete.add(aux);
				
				aux2.getPeriod().setIniDate( DateUtils.incDays(period.getEndDate(), 1) );
				if (aux2.getPeriod().getDays() > 1)
					tbcase.getPrescribedMedicines().add(aux2);
			}
			else 
			if (period.isDateInside(aux.getPeriod().getIniDate())) {
				aux.getPeriod().cutIni( DateUtils.incDays(period.getEndDate(), 1) );
			}
			else
			if (period.isDateInside(aux.getPeriod().getEndDate())) {
				aux.getPeriod().cutEnd( DateUtils.incDays(period.getIniDate(), -1) );
			}
		}
		
		// delete from the database extra periods
		deletePrescribedMedicines(medsToDelete);
	}
	
	/**
	 * Generate a list of prescribed medicines of a specific medicine for the current case
	 * @param med
	 * @return
	 */
	protected List<PrescribedMedicine> createPrescribedMedicineList(TbCase tbcase, Medicine med) {
		List<PrescribedMedicine> lst = new ArrayList<PrescribedMedicine>();
		
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			if ((med == null) || (pm.getMedicine().equals(med)))
				lst.add(pm);
		}
		return lst;
	}

	/**
	 * Split the prescribed medicines periods in two based on the date parameters 
	 * @param tbcase instance of {@link TbCase}
	 * @param dt Date to split the period
	 */
	public void splitPeriod(TbCase tbcase, Date dt) {
		List<PrescribedMedicine> lstnew = new ArrayList<PrescribedMedicine>();
		
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			if ((pm.getPeriod().isDateInside(dt)) && (!pm.getPeriod().getIniDate().equals(dt))) {
				PrescribedMedicine aux = clonePrescribedMedicine(pm);
				
				pm.getPeriod().setEndDate( DateUtils.incDays(dt, -1) );
				aux.getPeriod().setIniDate( dt );
				
				// add to a temporary list not to interfere with the main loop
				lstnew.add(aux);
			}
		}
		
		for (PrescribedMedicine pm: lstnew)
			tbcase.getPrescribedMedicines().add(pm);
	}


	/**
	 * Create a clone of the prescribed medicine object
	 * @param pm instance of the {@link PrescribedMedicine} class
	 * @return cloned instance of the {@link PrescribedMedicine} class
	 */
	public PrescribedMedicine clonePrescribedMedicine(PrescribedMedicine pm) {
		PrescribedMedicine aux = new PrescribedMedicine();
		aux.setDoseUnit(pm.getDoseUnit());
		aux.setPeriod( new Period(pm.getPeriod()) );
		aux.setFrequency(pm.getFrequency());
		aux.setMedicine(pm.getMedicine());
		aux.setSource(pm.getSource());
		aux.setTbcase(pm.getTbcase());
		return aux;
	}


	/** 
	 * Crop the treatment period according to the new period
	 * @param iniDate
	 * @param endDate
	 */
	public void cropTreatmentPeriod(TbCase tbcase, Period p) {
		int index = 0;
	
		// crop treatment health units
		while (index < tbcase.getHealthUnits().size()) {
			TreatmentHealthUnit hu = tbcase.getHealthUnits().get(index);
			// if doesn't intersect, so it's out of the period
			if (!hu.getPeriod().intersect(p)) {
				tbcase.getHealthUnits().remove(hu);
				
				if (em.contains(hu))
					em.remove(hu);
			}
			else index++;
		}
		
		// crop prescribed medicines
		index = 0;
		while (index < tbcase.getPrescribedMedicines().size()) {
			PrescribedMedicine pm = tbcase.getPrescribedMedicines().get(index);
			if (!pm.getPeriod().intersect(p)) {
				tbcase.getPrescribedMedicines().remove(pm);
				if (em.contains(pm))
					em.remove(pm);
			}
			else index++;
		}

		// set the new treatment period, if necessary
		if (tbcase.getTreatmentPeriod().getIniDate().before(p.getIniDate()) || tbcase.getTreatmentPeriod().getEndDate().after(p.getEndDate())) 
			tbcase.setTreatmentPeriod(p);
	}
	

	/**
	 * Join two periods that were split in a date dt
	 * @param dt date to join two adjacent periods
	 */
	public void joinPeriods(TbCase tbcase, Date dt) {
		List<PrescribedMedicine> lst = new ArrayList<PrescribedMedicine>();

		// find periods and join them (and store in a temporary list the period to delete)
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			if (pm.getPeriod().getIniDate().equals(dt)) {
				PrescribedMedicine aux = findCompactibleLeftAdjacentPeriod(pm);
				if (aux != null) {
					pm.getPeriod().setIniDate(aux.getPeriod().getIniDate());
					lst.add(pm);
				}
			}
		}

		deletePrescribedMedicines(lst);
	}

	
	/**
	 * Delete the prescribed medicines in the database
	 * @param lst list of {@link PrescribedMedicine} objects
	 */
	private void deletePrescribedMedicines(List<PrescribedMedicine> lst) {
		// delete periods from the database
		for (PrescribedMedicine pm: lst) {
			TbCase tbcase = pm.getTbcase();
			tbcase.getPrescribedMedicines().remove(pm);
			if (em.contains(pm))
				em.remove(pm);
		}
	}


	/**
	 * Find a compatible left adjacent prescribed medicine of the given prescribed medicine. To be left adjacent,
	 * not only the prescription must be the same, but the end date of the left period must be one day before
	 * the initial date of the right period
	 * @param pm instance of the {@link PrescribedMedicine} class
	 * @return instance of {@link PrescribedMedicine} in the left adjacent period or null if none is found
	 */
	private PrescribedMedicine findCompactibleLeftAdjacentPeriod(PrescribedMedicine pm) {
		TbCase tbcase = pm.getTbcase();
		Date dt = DateUtils.incDays( pm.getPeriod().getIniDate(), -1);
		for (PrescribedMedicine aux: tbcase.getPrescribedMedicines()) {
			if ((aux.getPeriod().getEndDate().equals(dt)) && (aux.getMedicine().equals(pm.getMedicine())) &&
			   (aux.getSource().equals(pm.getSource())) &&
			   (aux.getDoseUnit() == pm.getDoseUnit()) && (aux.getFrequency() == pm.getFrequency()))
			{
				return aux;
			}
		}
		return null;
	}
	
	
	/**
	 * Check all prescribed medicines inside this period and stretch the period
	 * if the initial or final date match with the given period
	 * @param tbcase the TB case to check the prescribed medicine periods
	 * @param period the new period to stretch
	 * @return the list of {@link PrescribedMedicine} objects that were removed from the TB case
	 */
	public List<PrescribedMedicine> updatePeriod(TbCase tbcase, RegimenPhase phase, Period newperiod) {
		Period p = (RegimenPhase.INTENSIVE.equals(phase) ? tbcase.getIntensivePhasePeriod(): tbcase.getContinuousPhasePeriod());
		
		if (p == null)
			throw new RuntimeException("Null period for case phase " + phase);

		List<PrescribedMedicine> deletedItems = new ArrayList<PrescribedMedicine>();
		
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			if (pm.getPeriod().isInside(p)) {
				// indicate if end date of medicine is at end date of period
				boolean fixedEndDate = pm.getPeriod().getEndDate().equals(p.getEndDate());
				
				// calc number of days period is ahead phase
				int days = DateUtils.daysBetween(p.getIniDate(), pm.getPeriod().getIniDate());

				// calc new initial date
				Date dt = DateUtils.incDays(newperiod.getIniDate(), days);
				
				// move period
				pm.getPeriod().movePeriod( dt );
				
				if (fixedEndDate)
					pm.getPeriod().setEndDate( newperiod.getEndDate() );

				// cut period if end date is after
				pm.getPeriod().intersect(newperiod);
				
				if (pm.getPeriod().getDays() <= 1)
					deletedItems.add(pm);
			}
		}
		
		return deletedItems;
	}

}
