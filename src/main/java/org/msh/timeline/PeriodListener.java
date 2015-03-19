/**
 * 
 */
package org.msh.timeline;

/**
 * Listener to events related to the periods of the time line
 * 
 * @author Ricardo Memoria
 *
 */
public interface PeriodListener {

	/**
	 * Called by the {@link TimelinePanel} to all event listeners registered 
	 * to notify about events related to {@link TimelinePeriod} 
	 * @param event is the instance of {@link PeriodEvent}
	 */
	void onTimelinePeriodEvent(PeriodEvent event);
}
