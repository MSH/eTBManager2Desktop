/**
 * 
 */
package org.msh.etbm.controller;

import java.util.Date;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.treatment.TreatmentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller to change the treatment period
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class ChangeTreatPeriodController  {

	private Date iniDate;
	private Date iniContinuousPhase;
	private Date endDate;
	private TbCase tbcase;

	@Autowired TreatmentServices treatmentServices;

	/**
	 * Initialize the controller to change the treatment period
	 */
	public void initialize() {
		iniDate = tbcase.getTreatmentPeriod().getIniDate();
		endDate = tbcase.getTreatmentPeriod().getEndDate();
		iniContinuousPhase = tbcase.getIniContinuousPhase();
	}
	
	/**
	 * Change the treatment period based on the new dates
	 */
	public void changePeriod() {
		tbcase = App.getEntityManager().merge(tbcase);
		treatmentServices.changeTreatmentPeriod(tbcase, iniDate, iniContinuousPhase, endDate);
	}
	
	/**
	 * Return true if the initial date can be changed
	 * @return
	 */
	public boolean isIniDateEditable() {
		return tbcase.getHealthUnits().size() > 1;
	}

	/**
	 * @return the iniDate
	 */
	public Date getIniDate() {
		return iniDate;
	}
	/**
	 * @param iniDate the iniDate to set
	 */
	public void setIniDate(Date iniDate) {
		this.iniDate = iniDate;
	}
	/**
	 * @return the iniContinuousPhase
	 */
	public Date getIniContinuousPhase() {
		return iniContinuousPhase;
	}
	/**
	 * @param iniContinuousPhase the iniContinuousPhase to set
	 */
	public void setIniContinuousPhase(Date iniContinuousPhase) {
		this.iniContinuousPhase = iniContinuousPhase;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
}
