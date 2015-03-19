/**
 * 
 */
package org.msh.etbm.services.cases;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.DiagnosisType;

/**
 * Store temporary information about a patient and its last case, if available
 * @author Ricardo Memoria
 *
 */
public class PatientData {
	
	/**
	 * Patient entity
	 */
	private Patient patient;
	/**
	 * Latest case of the patient
	 */
	private TbCase tbcase;

	public PatientData(Patient patient, TbCase tbcase) {
		super();
		this.patient = patient;
		this.tbcase = tbcase;
	}

	/**
	 * Return the status of the patient in a friendly display format
	 * @return String value
	 */
	public String getCaseStatus() {
		if (tbcase == null)
			return null;
		
		String s;
		
		if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT)
			 s = App.getMessage(tbcase.getClassification().getKeySuspect());
		else s = App.getMessage(tbcase.getClassification().getKey());

		SimpleDateFormat f = new SimpleDateFormat("MMM-yyyy");
		
		if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT) {
			if (tbcase.getRegistrationDate() != null)
				s += ", " + MessageFormat.format(App.getMessage("cases.sit.SUSP.date"), f.format(tbcase.getRegistrationDate()));
		}
		else 
		if (tbcase.getState() == CaseState.WAITING_TREATMENT) {
			if (tbcase.getDiagnosisDate() != null)
				s += ", " + MessageFormat.format(App.getMessage("cases.sit.CONF.date"), f.format( tbcase.getDiagnosisDate() ));
		}
		else 
		if ((tbcase.getState() == CaseState.ONTREATMENT) || (tbcase.getState() == CaseState.TRANSFERRING)) {
			if (tbcase.getTreatmentPeriod().getIniDate() != null)
				s += ", " + MessageFormat.format(App.getMessage("cases.sit.ONTREAT.date"), f.format( tbcase.getTreatmentPeriod().getIniDate() ));
		}
		else 
		if (tbcase.getState().ordinal() > CaseState.TRANSFERRING.ordinal()) {
			if (tbcase.getOutcomeDate() != null)
				s += ", " + MessageFormat.format(App.getMessage("cases.sit.OUTCOME.date"), f.format( tbcase.getOutcomeDate() )) + " (" +
					App.getMessage(tbcase.getState().getKey()) + ")";

		}
		
		return s;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

}
