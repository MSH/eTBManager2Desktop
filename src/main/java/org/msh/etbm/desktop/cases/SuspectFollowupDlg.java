/**
 * 
 */
package org.msh.etbm.desktop.cases;

import java.awt.Dimension;
import java.util.Date;

import org.msh.core.Displayable;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.FieldValue;
import org.msh.etbm.entities.FieldValueComponent;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.entities.enums.DrugResistanceType;
import org.msh.etbm.entities.enums.InfectionSite;
import org.msh.etbm.entities.enums.PatientType;
import org.msh.etbm.services.cases.CaseCloseService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.FormDataModel;

/**
 * @author Ricardo Memoria
 *
 */
public class SuspectFollowupDlg extends GenericFormDialog {
	private static final long serialVersionUID = 4646200138997985658L;


	/**
	 * Follow-up status
	 * @author Ricardo Memoria
	 *
	 */
	public enum FollowupOption implements Displayable {
		CONFIRMED, NOT_CONFIRMED;

		/** {@inheritDoc}
		 */
		@Override
		public String getMessageKey() {
			if (this == CONFIRMED)
				 return "DiagnosisType.CONFIRMED";
			else return "CaseState.NOT_CONFIRMED";
		}
	}

	
	private FollowupOption status;
	private Date diagnosisDate;
	private CaseClassification classification;
	private DrugResistanceType drugResistanceType;
	private PatientType patientType;
	private String patientTypeOther;
	private InfectionSite infectionSite;
	private FieldValue pulmonary;
	private FieldValueComponent extrapulmonary1;
	private FieldValueComponent extrapulmonary2;
	private Date outcomeDate;
	private CaseState outcome;
	private TbCase tbcase;
	private PatientType previouslyTreatedType;

	
	/**
	 * Default constructor
	 */
	public SuspectFollowupDlg() {
		super("suspectfollowup");

		setTitle(Messages.getString("cases.suspect.followup"));
		setFormSize(new Dimension(500, 500));
	}
	
	
	/**
	 * Open the form to follow-up a TB case 
	 * @param tbcase instance of the {@link TBCase}
	 * @return true if the case was forwarded, otherwise false if the operation was canceled
	 */
	public boolean open(TbCase tbcase) {
		this.tbcase = tbcase;
		return showModal();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		dataModel.setValue("controller", this);
		CaseCloseService srv = App.getComponent(CaseCloseService.class);
		dataModel.setValue("outcomeOptions", srv.getSuspectoutcomes());
//		dataModel.setValue("globalLists", GlobalLists.instance());
		dataModel.setValue("tbcase", tbcase);
		
//		dataModel.setValue("fieldsOptions", App.getComponent("fieldsOptions"));
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				tbcase = App.getEntityManager().merge(tbcase);
				if (status == FollowupOption.NOT_CONFIRMED) {
					CaseCloseService srv = App.getComponent(CaseCloseService.class);
					srv.closeCase(tbcase, outcomeDate, outcome, null);
				}
				else {
					tbcase.setClassification(classification);
					tbcase.setDiagnosisDate(diagnosisDate);
					tbcase.setDiagnosisType(DiagnosisType.CONFIRMED);
					tbcase.setPatientType(patientType);
					tbcase.setPatientTypeOther(patientType == PatientType.OTHER? patientTypeOther: null);
					tbcase.setDrugResistanceType(drugResistanceType);
					tbcase.setInfectionSite(infectionSite);
					if (tbcase.getInfectionSite() == InfectionSite.PULMONARY) {
						tbcase.setPulmonaryType(App.getEntityManager().merge(pulmonary));
					}
					
					if (tbcase.getInfectionSite() == InfectionSite.EXTRAPULMONARY) {
						if (extrapulmonary1 != null) {
							tbcase.setExtrapulmonaryType(App.getEntityManager().merge(extrapulmonary1));
						}
						else {
							tbcase.setExtrapulmonaryType(null);
						}
						// has extrapulmonary 2 ?
						if (extrapulmonary2 != null) {
							tbcase.setExtrapulmonaryType2(App.getEntityManager().merge(extrapulmonary2));
						}
						else {
							tbcase.setExtrapulmonaryType2(null);
						}
					}
					App.getEntityManager().persist(tbcase);
				}
			}
		});
		
		return true;
	}

	
	/**
	 * Return the options of the follow-up
	 * @return instance of {@link FollowupOption}
	 */
	public FollowupOption[] getFollowupOptions() {
		return FollowupOption.values();
	}

	/**
	 * @return the classification
	 */
	public CaseClassification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(CaseClassification classification) {
		this.classification = classification;
	}

	/**
	 * @return the drugResistanceType
	 */
	public DrugResistanceType getDrugResistanceType() {
		return drugResistanceType;
	}

	/**
	 * @param drugResistanceType the drugResistanceType to set
	 */
	public void setDrugResistanceType(DrugResistanceType drugResistanceType) {
		this.drugResistanceType = drugResistanceType;
	}

	/**
	 * @return the patientType
	 */
	public PatientType getPatientType() {
		return patientType;
	}

	/**
	 * @param patientType the patientType to set
	 */
	public void setPatientType(PatientType patientType) {
		this.patientType = patientType;
	}

	/**
	 * @return the patientTypeOther
	 */
	public String getPatientTypeOther() {
		return patientTypeOther;
	}

	/**
	 * @param patientTypeOther the patientTypeOther to set
	 */
	public void setPatientTypeOther(String patientTypeOther) {
		this.patientTypeOther = patientTypeOther;
	}

	/**
	 * @return the infectionSite
	 */
	public InfectionSite getInfectionSite() {
		return infectionSite;
	}

	/**
	 * @param infectionSite the infectionSite to set
	 */
	public void setInfectionSite(InfectionSite infectionSite) {
		this.infectionSite = infectionSite;
	}

	/**
	 * @return the pulmonary
	 */
	public FieldValue getPulmonary() {
		return pulmonary;
	}

	/**
	 * @param pulmonary the pulmonary to set
	 */
	public void setPulmonary(FieldValue pulmonary) {
		this.pulmonary = pulmonary;
	}

	/**
	 * @return the extrapulmonary1
	 */
	public FieldValueComponent getExtrapulmonary1() {
		return extrapulmonary1;
	}

	/**
	 * @param extrapulmonary1 the extrapulmonary1 to set
	 */
	public void setExtrapulmonary1(FieldValueComponent extrapulmonary1) {
		this.extrapulmonary1 = extrapulmonary1;
	}

	/**
	 * @return the extrapulmonary2
	 */
	public FieldValueComponent getExtrapulmonary2() {
		return extrapulmonary2;
	}

	/**
	 * @param extrapulmonary2 the extrapulmonary2 to set
	 */
	public void setExtrapulmonary2(FieldValueComponent extrapulmonary2) {
		this.extrapulmonary2 = extrapulmonary2;
	}

	/**
	 * @return the outcomeDate
	 */
	public Date getOutcomeDate() {
		return outcomeDate;
	}

	/**
	 * @param outcomeDate the outcomeDate to set
	 */
	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	/**
	 * @return the outcome
	 */
	public CaseState getOutcome() {
		return outcome;
	}

	/**
	 * @param outcome the outcome to set
	 */
	public void setOutcome(CaseState outcome) {
		this.outcome = outcome;
	}

	/**
	 * @return the diagnosisDate
	 */
	public Date getDiagnosisDate() {
		return diagnosisDate;
	}

	/**
	 * @param diagnosisDate the diagnosisDate to set
	 */
	public void setDiagnosisDate(Date diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	/**
	 * @return the status
	 */
	public FollowupOption getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(FollowupOption status) {
		this.status = status;
	}

	public PatientType getPreviouslyTreatedType() {
		return previouslyTreatedType;
	}

	public void setPreviouslyTreatedType(PatientType previouslyTreatedType) {
		this.previouslyTreatedType = previouslyTreatedType;
	}
}
