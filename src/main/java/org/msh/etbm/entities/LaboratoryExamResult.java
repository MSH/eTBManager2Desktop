package org.msh.etbm.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;

import org.msh.etbm.entities.enums.ExamStatus;
import org.msh.utils.FieldLog;


@MappedSuperclass
public abstract class LaboratoryExamResult extends SynchronizableEntity implements Serializable {
	private static final long serialVersionUID = 3229952267481224824L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

	@Temporal(TemporalType.DATE)
	@NotNull
	@FieldLog(key="PatientSample.dateCollected")
	private Date dateCollected;
	
	@Column(length=50)
	@FieldLog(key="PatientSample.sampleNumber")
	private String sampleNumber;
	
	@Column(length=250)
	@FieldLog(key="global.comments")
	private String comments;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CASE_ID")
	private TbCase tbcase;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="LABORATORY_ID")
	@FieldLog(key="Laboratory")
	private Laboratory laboratory;

	@Temporal(TemporalType.DATE)
	@FieldLog(key="cases.exams.dateRelease")
	private Date dateRelease;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="METHOD_ID")
	@FieldLog(key="cases.exams.method")
	private FieldValue method;

	private ExamStatus status;

	/**
	 * Return month of treatment based on the start treatment date and the collected date
	 * @return
	 */
	public Integer getMonthTreatment() {
		Date dt = getDateCollected();
		
		if (getTbcase() == null)
			return null;

		return tbcase.getMonthTreatment(dt);
	}
	
	/**
	 * Returns a key related to the system messages to display the month
	 * @return
	 */
	public String getMonthDisplay() {
		return tbcase.getTreatmentMonthDisplay(getDateCollected());
/*		Integer num = getMonthTreatment();
		
		if (num > 0) {
			return "global.month";  //Integer.toString(num);
		}
		
		Date dt = getDateCollected();
		Date dtReg = tbcase.getRegistrationDate();
		
		if ((dtReg == null) || (!dt.before(dtReg)))
			return "cases.exams.zero";
		else return "cases.exams.prevdt";
*/	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the laboratory
	 */
	public Laboratory getLaboratory() {
		return laboratory;
	}

	/**
	 * @param laboratory the laboratory to set
	 */
	public void setLaboratory(Laboratory laboratory) {
		this.laboratory = laboratory;
	}

	/**
	 * @return the dateRelease
	 */
	public Date getDateRelease() {
		return dateRelease;
	}

	/**
	 * @param dateRelease the dateRelease to set
	 */
	public void setDateRelease(Date dateRelease) {
		this.dateRelease = dateRelease;
	}

	/**
	 * @return the method
	 */
	public FieldValue getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(FieldValue method) {
		this.method = method;
	}


	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Locale locale = new Locale("en");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		return dateFormat.format(getDateCollected()) + " - " + getTbcase().getPatient().toString();
	}

	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	public Date getDateCollected() {
		return dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public ExamStatus getStatus() {
		return status;
	}

	public void setStatus(ExamStatus status) {
		this.status = status;
	}
}
