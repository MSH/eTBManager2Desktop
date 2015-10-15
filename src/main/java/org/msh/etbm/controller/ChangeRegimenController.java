/**
 *
 */
package org.msh.etbm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.Regimen;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.services.RegimenServices;
import org.msh.etbm.services.cases.treatment.MedicineTreatmentInfo;
import org.msh.etbm.services.cases.treatment.StartTreatmentServices;
import org.msh.etbm.services.cases.treatment.PrescribedMedicineLists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller to change regimen of a case.
 *
 * @author Mauricio Santos
 *
 */
@Component
@Scope("prototype")
public class ChangeRegimenController {

	private Tbunit treatmentUnit;
	private Date iniTreatmentDate;
	private Regimen regimen;
	private List<MedicineTreatmentInfo> medicinesIntensivePhase;
	private List<MedicineTreatmentInfo> medicinesContinuousPhase;
	private List<Regimen> regimens;

	@Autowired RegimenServices regimenServices;
	@Autowired StartTreatmentServices startTreatmentServices;

	/**
	 * Start the standard treatment regimen
	 */
	public void startStandardTreatmentRegimen(TbCase tbcase) {
		// call service to start the treatment
		startTreatmentServices.startStandardRegimen(tbcase, treatmentUnit, iniTreatmentDate,
				regimen, medicinesIntensivePhase, medicinesContinuousPhase);
	}

	/**
	 * Start the standard treatment regimen
	 */
	public void startIndividualizedTreatmentRegimen(TbCase tbcase) {
		// call service to start the treatment
		startTreatmentServices.startStandardRegimen(tbcase, treatmentUnit, iniTreatmentDate,
				regimen, medicinesIntensivePhase, medicinesContinuousPhase);
	}

	/**
	 * Initialize the list of medicines for the intensive and continuous phase 
	 */
	private void initializeMedicines() {
		// regimen was set ?
		if (regimen == null) {
			// No, so clean it all
			medicinesContinuousPhase = null;
			medicinesIntensivePhase = null;
		}
		else {
			PrescribedMedicineLists lst = startTreatmentServices.prepareStandardRegimenMedicines(regimen);
			// yes, so create list of medicines
			medicinesContinuousPhase = lst.medicinesContinuousPhase;
			medicinesIntensivePhase = lst.medicinesIntensivePhase;
		}
	}

	/**
	 * Add a medicine to the treatment. Used when initializing an individualized regimen treatment
	 * @return instance of {@link MedicineTreatmentInfo} with parameters about the prescribed medicine
	 */
	public MedicineTreatmentInfo addMedicine(Medicine med, RegimenPhase phase) {
		MedicineTreatmentInfo mti = new MedicineTreatmentInfo();
		mti.setMedicine(med);
		if (phase == RegimenPhase.INTENSIVE) {
			if (medicinesIntensivePhase == null)
				medicinesIntensivePhase = new ArrayList<MedicineTreatmentInfo>();
			medicinesIntensivePhase.add(mti);
		}
		else {
			if (medicinesContinuousPhase == null)
				medicinesContinuousPhase = new ArrayList<MedicineTreatmentInfo>();
			medicinesContinuousPhase.add(mti);
		}
		return mti;
	}

	/**
	 * @return the treatmentUnit
	 */
	public Tbunit getTreatmentUnit() {
		return treatmentUnit;
	}
	/**
	 * @param treatmentUnit the treatmentUnit to set
	 */
	public void setTreatmentUnit(Tbunit treatmentUnit) {
		this.treatmentUnit = treatmentUnit;
	}
	/**
	 * @return the iniTreatmentDate
	 */
	public Date getIniTreatmentDate() {
		return iniTreatmentDate;
	}
	/**
	 * @param iniTreatmentDate the iniTreatmentDate to set
	 */
	public void setIniTreatmentDate(Date iniTreatmentDate) {
		this.iniTreatmentDate = iniTreatmentDate;
	}
	/**
	 * @return the regimen
	 */
	public Regimen getRegimen() {
		return regimen;
	}
	/**
	 * @param regimen the regimen to set
	 */
	public void setRegimen(Regimen regimen) {
		this.regimen = regimen;
		initializeMedicines();
	}

	/**
	 * @return the medicinesIntensivePhase
	 */
	public List<MedicineTreatmentInfo> getMedicinesIntensivePhase() {
		return medicinesIntensivePhase;
	}
	/**
	 * @return the medicinesContinuousPhase
	 */
	public List<MedicineTreatmentInfo> getMedicinesContinuousPhase() {
		return medicinesContinuousPhase;
	}
	/**
	 * @return the regimens
	 */
	public List<Regimen> getRegimens(CaseClassification c) {
		if (regimens == null)
			regimens = regimenServices.getRegimens(c);
		return regimens;
	}

}
