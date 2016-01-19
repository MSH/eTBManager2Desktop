/**
 * 
 */
package org.msh.etbm.services.cases.treatment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.entities.MedicineRegimen;
import org.msh.etbm.entities.PrescribedMedicine;
import org.msh.etbm.entities.Regimen;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.TreatmentHealthUnit;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Services to support treatment start of a TB case
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class StartTreatmentServices {

	@Autowired EntityManager em;
	
	/**
	 * Start a standard regimen for a case
	 * @param tbcase is the case to start the treatment
	 * @param treatmentUnit treatment health unit where case will be handled
	 * @param iniDate start treatment date
	 * @param regimen the standard regimen
	 * @param lstIntPhase medicines in use in the intensive phase
	 * @param lstContPhase medicines in use in the continuous phase
	 */
	public void startStandardRegimen(TbCase tbcase, Tbunit treatmentUnit, Date iniDate, Regimen regimen, 
			List<MedicineTreatmentInfo> lstIntPhase, List<MedicineTreatmentInfo> lstContPhase) {
		startTreatment(tbcase, regimen, treatmentUnit, iniDate, lstIntPhase, lstContPhase);
	}

	
	/**
	 * Start an individualized regimen for a case
	 * @param tbcase is the instance of {@link TbCase}
	 * @param treatmentUnit is the treatment unit instance of {@link Tbunit}
	 * @param iniDate is the start date of the treatment
	 * @param lstIntPhase is the list of medicines prescribed to the intensive phase
	 * @param lstContPhase is the list of medicines prescribed to the continuous phase
	 */
	public void startIndividualizedTreatment(TbCase tbcase, Tbunit treatmentUnit, Date iniDate, 
			List<MedicineTreatmentInfo> lstIntPhase, List<MedicineTreatmentInfo> lstContPhase) {
		startTreatment(tbcase, null, treatmentUnit, iniDate, lstIntPhase, lstContPhase);
	}

	
	/**
	 * Start a treatment regimen for the given case
	 * @param tbcase
	 * @param regimen
	 * @param treatmentUnit
	 * @param iniDate
	 * @param lstIntPhase
	 * @param lstContPhase
	 */
	private void startTreatment(TbCase tbcase, Regimen regimen, Tbunit treatmentUnit, Date iniDate, List<MedicineTreatmentInfo> lstIntPhase, List<MedicineTreatmentInfo> lstContPhase) {
		if (tbcase.getState() != CaseState.WAITING_TREATMENT)
			throw new IllegalArgumentException("Illegal state of the case. Case must be waiting to start treatment");

		int monthsIntPhase = 0;
		int monthsContPhase = 0;

		// is a standard regimen ?
		if (regimen != null) {
			monthsIntPhase = regimen.getMonthsIntensivePhase();
			monthsContPhase = regimen.getMonthsContinuousPhase();
			// because it's a standard regimen, use the definitions there
			for (MedicineTreatmentInfo mti: lstIntPhase)
				mti.setMonths(monthsIntPhase);

			for (MedicineTreatmentInfo mti: lstContPhase)
				mti.setMonths(monthsContPhase);
		}
		else {
			// calculate number of months for intensive phase
			for (MedicineTreatmentInfo mti: lstIntPhase) {
				if (mti.getMonths() == 0)
					throw new IllegalArgumentException("Months of treatment cannot be 0 to " + mti.getMedicine());
				if (mti.getMonths() > monthsIntPhase)
					monthsIntPhase = mti.getMonths();
			}

			// calculate number of months for continuous phase
			for (MedicineTreatmentInfo mti: lstContPhase) {
				if (mti.getMonths() == 0)
					throw new IllegalArgumentException("Months of treatment cannot be 0 to " + mti.getMedicine());
				if (mti.getMonths() > monthsContPhase)
					monthsContPhase = mti.getMonths();
			}
		}
		int monthsTreat = monthsIntPhase + monthsContPhase;
		if (monthsTreat == 0)
			throw new IllegalArgumentException("The regimen informed has no treatment period defined");

		
		// get the end treatment date
		Date endDate = DateUtils.incDays(DateUtils.incMonths(iniDate, monthsTreat), -1);
		Date iniContPhase = DateUtils.incMonths(iniDate, monthsIntPhase);

		// set the treatment period
		Period treatmentPeriod = new Period(iniDate, endDate);
		
		// set the prescribed medicines for the intensive phase
		addMedicinesToCase(tbcase, iniDate, lstIntPhase);
		
		// set the prescribed medicines for the continuous phase
		addMedicinesToCase(tbcase, iniContPhase, lstContPhase);
		
		// set the treatment period
		TreatmentHealthUnit hu = new TreatmentHealthUnit();
		hu.setTbcase(tbcase);
		tbcase.getHealthUnits().add(hu);
		hu.setPeriod(treatmentPeriod);
		hu.setTbunit(treatmentUnit);
		hu.setTransferring(false);
		em.persist(hu);
		
		// set TB case fields
		tbcase.setTreatmentPeriod(treatmentPeriod);
		tbcase.setRegimen(regimen);
		tbcase.setRegimenIni(regimen);
		tbcase.setState(CaseState.ONTREATMENT);
		tbcase.setOwnerUnit(treatmentUnit);
		tbcase.setIniContinuousPhase(iniContPhase);
		tbcase.updateDaysTreatPlanned();

		CaseServices.instance().save(tbcase);
		em.flush();
	}

	
	/**
	 * Add prescribed medicines to the case in a given period. Used inside startStandardRegimen
	 * @param tbcase the case
	 * @param period the period to add the medicines
	 * @param lst the list of {@link PrescribedMedicine} instances
	 */
	private void addMedicinesToCase(TbCase tbcase, Date iniDate, List<MedicineTreatmentInfo> lst) {
		for (MedicineTreatmentInfo mti: lst) {
			PrescribedMedicine pm = new PrescribedMedicine();
			pm.setMedicine(mti.getMedicine());
			pm.setDoseUnit(mti.getDoseUnit());
			pm.setFrequency(mti.getFrequency());
			pm.setSource(mti.getSource());
			Date endDate = DateUtils.incDays( DateUtils.incMonths(iniDate, mti.getMonths()), -1);
			pm.setPeriod( new Period(iniDate, endDate) );
			pm.setTbcase(tbcase);
			tbcase.getPrescribedMedicines().add(pm);
			em.persist(pm);
		}
	}


	/**
	 * Mount the list of prescribed medicines to be filled by the user in order to
	 * start a standard regimen
	 * @param regimen instance of {@link Regimen} class
	 * @return return an instance of {@link PrescribedMedicineLists} containing the medicines
	 * to be prescribed according to the regimen
	 */
	public PrescribedMedicineLists prepareStandardRegimenMedicines(Regimen regimen) {
		ArrayList<MedicineTreatmentInfo> lstContPhase = new ArrayList<MedicineTreatmentInfo>();
		ArrayList<MedicineTreatmentInfo> lstIntPhase = new ArrayList<MedicineTreatmentInfo>();

		List<MedicineRegimen> regs = em.createQuery("from MedicineRegimen m join fetch m.medicine join fetch m.defaultSource "
				+ "where m.regimen.id = :id "
				+ "order by m.medicine.genericName.name1")
			.setParameter("id", regimen.getId())
			.getResultList();

		// create the list of prescribed medicines
		for (MedicineRegimen mr: regs) {
			MedicineTreatmentInfo item = new MedicineTreatmentInfo();
			// set default values
			item.setMedicine(mr.getMedicine());
			item.setDoseUnit(mr.getDefaultDoseUnit());
			item.setSource(mr.getDefaultSource());
			item.setFrequency(mr.getDefaultFrequency());

			// add in the corresponding list
			if (mr.getPhase() == RegimenPhase.CONTINUOUS) {
				lstContPhase.add(item);
			}
			else {
				lstIntPhase.add(item);
			}
		}

		// mount the result
		PrescribedMedicineLists list = new PrescribedMedicineLists();
		list.medicinesContinuousPhase = lstContPhase;
		list.medicinesIntensivePhase = lstIntPhase;
		return list;
	}
}
