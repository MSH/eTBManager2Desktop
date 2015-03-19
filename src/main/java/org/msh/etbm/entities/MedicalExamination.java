package org.msh.etbm.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.msh.etbm.entities.enums.ClinicalEvolution;
import org.msh.etbm.entities.enums.DotBy;
import org.msh.etbm.entities.enums.MedAppointmentType;
import org.msh.etbm.entities.enums.ReferredBy;
import org.msh.etbm.entities.enums.ReferredTo;
import org.msh.etbm.entities.enums.YesNoType;

/**
 *
 * Records information about a medical examination of a case
 *
 * @author Ricardo Mem�ria
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("gen")
@Table(name="medicalexamination")
public class MedicalExamination extends CaseData implements Serializable {
	private static final long serialVersionUID = 2760727118134685773L;

	private Double weight;
	
	private Float height;
	
	private Float heartRate;
	private Float bloodPressureMin;
	private Float bloodPressureMax;
	private Float respRateRest;
	private Float temperature;

	private MedAppointmentType appointmentType;

	private YesNoType usingPrescMedicines;
	
	@Column(length=100)
	private String reasonNotUsingPrescMedicines;
	
	@Column(length=100)
	private String responsible;
	
	@Column(length=100)
	private String positionResponsible;

	
	private YesNoType supervisedTreatment;
	
	@Column(length=100)
	private String supervisionUnitName;
	
	private YesNoType surgicalProcedure;
	
	@Column(length=100)
	private String surgicalProcedureDesc;

	//Used for Kenya Workspace
	//usrivastava
	private ReferredBy patientRefBy;
	
	private YesNoType nutrtnSupport;
	
	@Column(length=100)
	private String referredByUnitName;	
	
	@Temporal(TemporalType.DATE)
	@Column(name="REF_BY_DATE")
	private Date refByDate;	

	private ReferredTo patientRefTo;
	
	@Column(length=100)
	private String referredToUnitName;	
	
	@Temporal(TemporalType.DATE)
	@Column(name="REF_TO_DATE")
	private Date refToDate;	
	
	private DotBy dotDurinIntPhase;	
	
	@Column(length=50)
	private String dotPhoneNumber;

	private ClinicalEvolution clinicalEvolution;
	
	@Column(length=100)
	private String dotProvName;
	
	
	public String getSurgicalProcedureDesc() {
		return surgicalProcedureDesc;
	}

	public void setSurgicalProcedureDesc(String surgicalProcedureDesc) {
		this.surgicalProcedureDesc = surgicalProcedureDesc;
	}

	public YesNoType getSurgicalProcedure() {
		return surgicalProcedure;
	}

	public void setSurgicalProcedure(YesNoType surgicalProcedure) {
		this.surgicalProcedure = surgicalProcedure;
	}
	
	public YesNoType getSupervisedTreatment() {
		return supervisedTreatment;
	}

	public void setSupervisedTreatment(YesNoType supervisedTreatment) {
		this.supervisedTreatment = supervisedTreatment;
	}

	public String getSupervisionUnitName() {
		return supervisionUnitName;
	}

	public void setSupervisionUnitName(String supervisionUnitName) {
		this.supervisionUnitName = supervisionUnitName;
	}

	public ClinicalEvolution getClinicalEvolution() {
		return clinicalEvolution;
	}

	public void setClinicalEvolution(ClinicalEvolution clinicalEvolution) {
		this.clinicalEvolution = clinicalEvolution;
	}

	
	/**
	 * Calculate the BMI (using weight and height)
	 * @return BMI value
	 */
	public double getBMI() {
		if ((height == null) || (height == 0))
			return 0;
		float h = height / 100;
		return (weight == null? 0: weight/(h*h)); 
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public YesNoType getUsingPrescMedicines() {
		return usingPrescMedicines;
	}

	public void setUsingPrescMedicines(YesNoType usingPrescMedicines) {
		this.usingPrescMedicines = usingPrescMedicines;
	}

	public String getReasonNotUsingPrescMedicines() {
		return reasonNotUsingPrescMedicines;
	}

	public void setReasonNotUsingPrescMedicines(String reasonNotUsingPrescMedicines) {
		this.reasonNotUsingPrescMedicines = reasonNotUsingPrescMedicines;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public MedAppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(MedAppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}
	/**
	 * @return the heartRate
	 */
	public Float getHeartRate() {
		return heartRate;
	}

	/**
	 * @param heartRate the heartRate to set
	 */
	public void setHeartRate(Float heartRate) {
		this.heartRate = heartRate;
	}

	/**
	 * @return the temperature
	 */
	public Float getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the bloodPressureMin
	 */
	public Float getBloodPressureMin() {
		return bloodPressureMin;
	}

	/**
	 * @param bloodPressureMin the bloodPressureMin to set
	 */
	public void setBloodPressureMin(Float bloodPressureMin) {
		this.bloodPressureMin = bloodPressureMin;
	}

	/**
	 * @return the bloodPressureMax
	 */
	public Float getBloodPressureMax() {
		return bloodPressureMax;
	}

	/**
	 * @param bloodPressureMax the bloodPressureMax to set
	 */
	public void setBloodPressureMax(Float bloodPressureMax) {
		this.bloodPressureMax = bloodPressureMax;
	}

	/**
	 * @return the respRateRest
	 */
	public Float getRespRateRest() {
		return respRateRest;
	}

	/**
	 * @param respRateRest the respRateRest to set
	 */
	public void setRespRateRest(Float respRateRest) {
		this.respRateRest = respRateRest;
	}

	/**
	 * @return the positionResponsible
	 */
	public String getPositionResponsible() {
		return positionResponsible;
	}

	/**
	 * @param positionResponsible the positionResponsible to set
	 */
	public void setPositionResponsible(String positionResponsible) {
		this.positionResponsible = positionResponsible;
	}
	
	public ReferredBy getPatientRefBy() {
		return patientRefBy;
	}

	public void setPatientRefBy(ReferredBy patientRefBy) {
		this.patientRefBy = patientRefBy;
	}

	public String getReferredByUnitName() {
		return referredByUnitName;
	}

	public void setReferredByUnitName(String referredByUnitName) {
		this.referredByUnitName = referredByUnitName;
	}

	public Date getRefByDate() {
		return refByDate;
	}

	public void setRefByDate(Date refByDate) {
		this.refByDate = refByDate;
	}

	public ReferredTo getPatientRefTo() {
		return patientRefTo;
	}

	public void setPatientRefTo(ReferredTo patientRefTo) {
		this.patientRefTo = patientRefTo;
	}

	public String getReferredToUnitName() {
		return referredToUnitName;
	}

	public void setReferredToUnitName(String referredToUnitName) {
		this.referredToUnitName = referredToUnitName;
	}

	public Date getRefToDate() {
		return refToDate;
	}

	public void setRefToDate(Date refToDate) {
		this.refToDate = refToDate;
	}

	public DotBy getDotDurinIntPhase() {
		return dotDurinIntPhase;
	}

	public void setDotDurinIntPhase(DotBy dotDurinIntPhase) {
		this.dotDurinIntPhase = dotDurinIntPhase;
	}

	public YesNoType getNutrtnSupport() {
		return nutrtnSupport;
	}

	public void setNutrtnSupport(YesNoType nutrtnSupport) {
		this.nutrtnSupport = nutrtnSupport;
	}

	public String getDotProvName() {
		return dotProvName;
	}

	public void setDotProvName(String dotProvName) {
		this.dotProvName = dotProvName;
	}

	/**
	 * @return the dotPhoneNumber
	 */
	public String getDotPhoneNumber() {
		return dotPhoneNumber;
	}

	/**
	 * @param dotPhoneNumber the dotPhoneNumber to set
	 */
	public void setDotPhoneNumber(String dotPhoneNumber) {
		this.dotPhoneNumber = dotPhoneNumber;
	}
	
	
}
