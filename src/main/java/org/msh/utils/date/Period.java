package org.msh.utils.date;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Represent a period of time, i.e, an intervall between 2 dates
 * @author Ricardo Memoria
 *
 */
@Embeddable
public class Period {

	/**
	 * Initial date of the period
	 */
	@Temporal(TemporalType.DATE)
	private Date iniDate;
	
	/**
	 * Final date of the period
	 */
	@Temporal(TemporalType.DATE)
	private Date endDate;

	
	/**
	 * Create a new period defining the initial and final date
	 * @param iniDate
	 * @param endDate
	 */
	public Period(Date iniDate, Date endDate) {
		super();
		this.iniDate = iniDate != null? (Date)iniDate.clone() : null;
		this.endDate = endDate != null? (Date)endDate.clone() : null;
		checkDatesOrder();
	}

	
	/**
	 * Create a new period from an existing period
	 * @param p
	 */
	public Period(Period p) {
		super();
		this.iniDate = (Date)p.getIniDate().clone();
		this.endDate = (Date)p.getEndDate().clone();
	}


	/**
	 * Set the bounds of the period
	 * @param ini Initial date of the period
	 * @param end Final date of the period
	 */
	public void set(Date ini, Date end) {
		iniDate = (Date)ini.clone();
		endDate = (Date)end.clone();
		checkDatesOrder();
	}


	/**
	 * Standard constructor
	 */
	public Period() {
		super();
	}

	
	/**
	 * Check if initial date is before the initial date. If not, they are swapped
	 */
	private void checkDatesOrder() {
		if ((iniDate == null) || (endDate == null))
			return;
		if (iniDate.after(endDate)) {
			Date dt = iniDate;
			iniDate = endDate;
			endDate = dt;
		}
	}
	
	/**
	 * Return the number of days in the period
	 * @return
	 */
	public int getDays() {
		return ((iniDate != null) && (endDate != null) ? DateUtils.daysBetween(iniDate, endDate): 0);
	}

	
	/**
	 * Return number of months in the period
	 * @return
	 */
	public int getMonths() {
		return ((iniDate != null) && (endDate != null) ? DateUtils.monthsBetween(iniDate, DateUtils.incDays(endDate, 1)): 0);
	}


	/**
	 * Return number of years in the period
	 * @return
	 */
	public int getYears() {
		return ((iniDate != null) && (endDate != null) ? DateUtils.monthsBetween(iniDate, DateUtils.incDays(endDate, 1)): 0);
	}

	
	/**
	 * Check if the period is before the given period, even if they intersect
	 * @param p
	 */
	public boolean isBefore(Period p) {
		return (iniDate.before(p.getIniDate()));
	}


	/**
	 * Check if the period is after the given period, even if they intersect
	 * @param p
	 * @return
	 */
	public boolean isAfter(Period p) {
		return (iniDate.after(p.getIniDate()));
	}

	
	/**
	 * Check if a period is empty, i.e, if either the initial or the final date was not defined
	 * @return true if period is empty, otherwise false
	 */
	public boolean isEmpty() {
		return (iniDate == null) || (endDate == null);
	}


	/**
	 * Cut the period from the given date until the final date, turning in a small period
	 * from the initial date to the given date
	 * @param dt Date of cut
	 * @return true if the date is inside the period
	 */
	public boolean cutEnd(Date dt) {
		if (!isDateInside(dt))
			return false;
		
		endDate = dt;
		return true;
	}

	
	/**
	 * Cut the period from the initial date until the given date, turning in a small period
	 * from the given date to the final date
	 * @param dt Date of cut
	 * @return true if the date is inside the period
	 */
	public boolean cutIni(Date dt) {
		if (!isDateInside(dt))
			return false;
		
		iniDate = dt;
		return true;
	}

	
	/**
	 * Move the period to the initial date, keeping the distance between the initial and final date
	 * @param newIniDate the initial date of the period
	 */
	public void movePeriod(Date newIniDate) {
		if (isEmpty())
			return;
		
		int days = DateUtils.daysBetween(this.iniDate, newIniDate);
		if (iniDate.after(newIniDate))
			days = -days;

		iniDate = DateUtils.incDays(iniDate, days);
		endDate = DateUtils.incDays(endDate, days);
	}
	
	
	/**
	 * Move the period to a specific quantity of days, keeping the distance between the initial and final date
	 * @param days
	 */
	public void movePeriod(int days) {
		if (isEmpty())
			return;

		iniDate = DateUtils.incDays(iniDate, days);
		endDate = DateUtils.incDays(endDate, days);
	}


	/**
	 * Check if a given period is inside the period, including the days at the limit
	 * @param ini
	 * @param end
	 * @return
	 */
	public boolean contains(Date ini, Date end) {
		return (!ini.before(iniDate)) && (!end.after(endDate));
	}


	/**
	 * Check if a given period is inside the period, including the days at the limit
	 * @param p
	 * @return
	 */
	public boolean contains(Period p) {
		return contains(p.getIniDate(), p.getEndDate());
	}

	
	/**
	 * Check if a given period intersects the period 
	 * @param ini initial date of the given period
	 * @param end final date of the given period
	 * @return true if the period defined by (ini, end) intersect with the period
	 */
	public boolean isIntersected(Date ini, Date end) {
		return (!ini.after(endDate)) && (!end.before(iniDate));
	}

	
	/**
	 * Check if a given period intersects the period, i.e, if the periods share a common period
	 * @param p period to check if is intersected with the period
	 * @return true if periods share a common intersection
	 */
	public boolean isIntersected(Period p) {
		return isIntersected(p.getIniDate(), p.getEndDate());
	}

	
	/**
	 * Check if the period is totally inside the given period
	 * @param p outer period
	 * @return true if period is inside period p
	 */
	public boolean isInside(Period p) {
		if (isEmpty())
			return false;
		
		return ((!iniDate.before(p.getIniDate())) && (!endDate.after(p.getEndDate())));
	}


	/**
	 * Initialize the period from an existing period
	 * @param p
	 */
	public void set(Period p) {
		iniDate = (Date)p.getIniDate().clone();
		endDate = (Date)p.getEndDate().clone();
	}


	/**
	 * Intersect the period with the given period. The period is changed according
	 * to the result of the intersection
	 * @param ini initial date of the period to be intersected
	 * @param end final date of the period to be intersected
	 * @return
	 */
	public boolean intersect(Date ini, Date end) {
		if (!isIntersected(ini, end))
			return false;

		if (ini.after(iniDate))
			iniDate = ini;
		if (end.before(endDate))
			endDate = end;
			
		return true;
	}


	/**
	 * Intersect the period with the given period. The period is changed according
	 * to the result of the intersection
	 * @param p {@link Period} instance to intersect with the period
	 * @return true if period was intersected, otherwise false if the period doesn't intersect
	 */
	public boolean intersect(Period p) {
		return intersect(p.getIniDate(), p.getEndDate());
	}
	
	
	/**
	 * Check if a date is inside the period
	 * @param dt
	 * @return
	 */
	public boolean isDateInside(Date dt) {
		return (!dt.before(iniDate)) && (!dt.after(endDate));
	}


	/**
	 * Return the initial date
	 * @return
	 */
	public Date getIniDate() {
		return iniDate;
	}
	

	/**
	 * Set the initial date
	 * @param iniDate
	 */
	public void setIniDate(Date iniDate) {
		this.iniDate = (iniDate != null? (Date)iniDate.clone(): iniDate);
		checkDatesOrder();
	}


	/**
	 * Return the ending date
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * Set the ending date
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = (endDate != null? (Date)endDate.clone(): endDate);
		checkDatesOrder();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((iniDate == null) ? 0 : iniDate.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (iniDate == null) {
			if (other.iniDate != null)
				return false;
		} else if (!iniDate.equals(other.iniDate))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return (iniDate != null? iniDate.toString(): "null") + "..." +
				(endDate != null? endDate.toString(): "null");
	}
}
