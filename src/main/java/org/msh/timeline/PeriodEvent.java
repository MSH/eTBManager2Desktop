/**
 * 
 */
package org.msh.timeline;

/**
 * Information about an event occurred in a period of the time line
 *  
 * @author Ricardo Memoria
 *
 */
public class PeriodEvent {

	public enum PeriodEventType { HOVER, CLICK, DOUBLE_CLICK };
	
	private TimelinePeriod period;
	private PeriodEventType type;

	/**
	 * Default constructor
	 * @param period if the {@link TimelinePeriod} where event occurred
	 * @param type is the type of event
	 */
	public PeriodEvent(TimelinePeriod period, PeriodEventType type) {
		super();
		this.period = period;
		this.type = type;
	}

	/**
	 * @return the period
	 */
	public TimelinePeriod getPeriod() {
		return period;
	}

	/**
	 * @return the type
	 */
	public PeriodEventType getType() {
		return type;
	}
}
