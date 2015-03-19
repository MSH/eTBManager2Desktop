package org.msh.etbm.entities;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;


@MappedSuperclass
public class CaseData extends SynchronizableEntity {
	private static final long serialVersionUID = -3400555189490766080L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name="EVENT_DATE")
	private Date date;
	
	@Lob
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="CASE_ID")
	@NotNull
	private TbCase tbcase;
	
	/**
	 * Return month of treatment based on the start treatment date and the collected date
	 * @return
	 */
	public Integer getMonthTreatment() {
		Date dt = getDate();
		if (dt == null)
			return null;
		
		if (getTbcase() == null)
			return null;

		return tbcase.getMonthTreatment(dt);
	}
	
	/**
	 * Returns a key related to the system messages to display the month
	 * @return
	 */
	public String getMonthDisplay() {
		return tbcase.getTreatmentMonthDisplay(getDate());
/*		if (date == null)
			return null;
		Integer num = tbcase.getMonthTreatment(date);
		
		if (num > 0) {
			return "global.month";  //Integer.toString(num);
		}
		
		Date dt = getDate();
		Date dtReg = tbcase.getRegistrationDate();
		
		if ((dtReg == null) || (!dt.before(dtReg)))
			return "cases.exams.zero";
		else return "cases.exams.prevdt";
*/	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comment) {
		this.comments = comment;
	}

	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (date == null)
			return getTbcase().getPatient().getFullName();
		
		Locale locale = new Locale("en");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		String s = dateFormat.format(getDate());
		return s + " - " + getTbcase().getPatient().getFullName();
	}

}
