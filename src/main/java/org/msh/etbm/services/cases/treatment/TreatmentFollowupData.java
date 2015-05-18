/**
 * 
 */
package org.msh.etbm.services.cases.treatment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TreatmentMonitoring;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.TreatmentDayOption;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;

/**
 * Store temporary information about the treatment follow-up of the case.
 * This object must be used to set treatment follow-up in a specific day
 * of the treatment. When all changes are done, it must be saved using the
 * {@link TreatmentFollowupServices} component
 * 
 * @author Ricardo Memoria
 *
 */
public class TreatmentFollowupData {

	private TbCase tbcase;
	private List<TreatmentMonitoring> disps;
	private List<TreatmentMonitoring> updatedDisps;
    private Workspace workspace;
    // just to speed up the process
	private TreatmentMonitoring current;
	
	/**
	 * Protected constructor, only available for the {@link TreatmentFollowupServices} class
	 * @param tbcase instance of {@link TbCase} 
	 * @param disps list of {@link TreatmentMonitoring} containing information about treatment follow-up
	 */
	protected TreatmentFollowupData(TbCase tbcase, List<TreatmentMonitoring> disps, Workspace ws) {
		this.tbcase = tbcase;
		this.disps = disps;
        this.workspace = ws;
	}
	
	/**
	 * Return information about the day of the treatment 
	 * @param dt
	 * @return
	 */
	public TreatmentDayOption getTreatmentDay(Date dt) {
		if (!tbcase.getTreatmentPeriod().isDateInside(dt))
			return null;

		TreatmentMonitoring tm = findTreatmentMonitoring(dt);
	
		if (tm == null)
			return null;

		return tm.getDay(DateUtils.dayOf(dt));
	}

    /**
     * Return true if the given day is planned for prescription
     * @param dt the date to check in
     * @return true if the date must have medicine prescription
     */
    public boolean isDayPrescription(Date dt) {
        return tbcase.isDayPrescription(dt);
    }
	
	
	/**
	 * Change the treatment follow-up of an specific treatment date
	 * @param dt
	 * @param treatFollowup
	 */
	public void setTreatmentDay(Date dt, TreatmentDayOption treatFollowup) {
		if (!tbcase.getTreatmentPeriod().isDateInside(dt))
			throw new IllegalArgumentException("The date doesn't belong to the treatment period");

		TreatmentMonitoring tm = findTreatmentMonitoring(dt);
		if (tm == null) {
			tm = new TreatmentMonitoring();
			tm.setMonth(DateUtils.monthOf(dt) + 1);
			tm.setYear(DateUtils.yearOf(dt));
			tm.setTbcase(tbcase);
			disps.add(tm);
		}
		
		tm.setDay(DateUtils.dayOf(dt), treatFollowup);
		
		if (updatedDisps == null)
			updatedDisps = new ArrayList<TreatmentMonitoring>();
		updatedDisps.add(tm);
	}


	/**
	 * Find an object of class {@link TreatmentMonitoring} by its date
	 * @param dt date to search for dispensing information
	 * @return instance of {@link TreatmentMonitoring}
	 */
	private TreatmentMonitoring findTreatmentMonitoring(Date dt) {
		int month = DateUtils.monthOf(dt) + 1;
		int year = DateUtils.yearOf(dt);

        // check if the last searched is the one
        if (current != null && current.getMonth() == month && current.getYear() == year) {
            return current;
        }

		for (TreatmentMonitoring tm: disps) {
			if ((tm.getMonth() == month) && (tm.getYear() == year)) {
                current = tm;
                return tm;
            }
		}
		return null;
	}
	
	/**
	 * Return the list of treatment follow-up months that were updated and must be saved
	 * @return list of {@link TreatmentMonitoring}
	 */
	protected List<TreatmentMonitoring> getUpdatedData() {
		return updatedDisps;
	}
	
	/**
	 * Update an instance of {@link TreatmentMonitoring} by a new one (usually replaced when the new
	 * one is just persisted)
	 * @param tm instance of {@link TreatmentMonitoring}
	 */
	protected void replaceTreatmentMonitoringMonth(TreatmentMonitoring tm) {
		for (TreatmentMonitoring aux: disps) {
			if ((aux.getMonth() == tm.getMonth()) && (aux.getYear() == tm.getYear())) {
				disps.remove(aux);
				disps.add(tm);
				return;
			}
		}
		
		disps.add(tm);
	}
}
