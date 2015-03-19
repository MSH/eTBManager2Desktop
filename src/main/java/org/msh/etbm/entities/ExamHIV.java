package org.msh.etbm.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.msh.etbm.entities.enums.HIVResult;
import org.msh.etbm.entities.enums.HIVResultKe;

/**
 * Records information about an HIV result during the treatment
 * 
 * @author Ricardo Mem�ria
 *
 */
@Entity
@Table(name="examhiv")
public class ExamHIV extends CaseData implements Serializable {
	private static final long serialVersionUID = 2237957846637585494L;

	private HIVResult result;
	
	@Temporal(TemporalType.DATE)
	private Date startedARTdate;
	
	@Temporal(TemporalType.DATE)
	private Date startedCPTdate;
	
	@Column(length=100)
	private String laboratory;

	//usrivast
	//addition for kenya workspace
	private Integer cd4Count;	
	
	@Temporal(TemporalType.DATE)
	private Date cd4StDate;

	private HIVResultKe partnerResult;
	
	@Temporal(TemporalType.DATE)
	private Date partnerResultDate;

	@Transient
	private Boolean ARTstarted;
	@Transient
	private Boolean CPTstarted;
	
	public boolean isPartnerPresent() {
		return partnerResultDate != null;
	}
	
	public void setPartnerPresent(boolean value) {
		if (!value)
			partnerResultDate = null;
	}
	
	
	/**
	 * Return true if ART has started
	 * @return boolean value
	 */
	public boolean isARTstarted() {
		if (ARTstarted == null)
			ARTstarted = startedARTdate != null;
		return ARTstarted;
	}
	
	/**
	 * Return true if CPT has started
	 * @return boolean value
	 */
	public boolean isCPTstarted() {
		if (CPTstarted == null)
			CPTstarted = startedCPTdate != null;
		return CPTstarted;
	}
	
	/**
	 * set if CPT has started or not
	 * @param value true if CPT has started
	 */
	public void setCPTstarted(boolean value) {
		CPTstarted = value;
		if (!value)
			startedCPTdate = null;
	}
	
	/**
	 * set if ART has started or not
	 * @param value true if ART has started
	 */
	public void setARTstarted(boolean value) {
		ARTstarted = value;
		if (!value)
			startedARTdate = null;
	}
	
	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public HIVResult getResult() {
		return result;
	}

	public void setResult(HIVResult result) {
		this.result = result;
	}

	public Date getStartedARTdate() {
		return startedARTdate;
	}

	public void setStartedARTdate(Date startedARTdate) {
		this.startedARTdate = startedARTdate;
	}

	public Date getStartedCPTdate() {
		return startedCPTdate;
	}

	public void setStartedCPTdate(Date startedCPTdate) {
		this.startedCPTdate = startedCPTdate;
	}
	
	public Integer getCd4Count() {
		return cd4Count;
	}

	public void setCd4Count(Integer cd4Count) {
		this.cd4Count = cd4Count;
	}

	public Date getCd4StDate() {
		return cd4StDate;
	}

	public void setCd4StDate(Date cd4StDate) {
		this.cd4StDate = cd4StDate;
	}

	public HIVResultKe getPartnerResult() {
		return partnerResult;
	}

	public void setPartnerResult(HIVResultKe partnerResult) {
		this.partnerResult = partnerResult;
	}

	public Date getPartnerResultDate() {
		return partnerResultDate;
	}

	public void setPartnerResultDate(Date partnerResultDate) {
		this.partnerResultDate = partnerResultDate;
	}		
}
