/**
 * 
 */
package org.msh.etbm.entities.enums;

/**
 * @author Ricardo Memoria
 *
 */
public enum TreatMonitoringInput {
	SIMPLE, DETAILED;

	/** {@inheritDoc}
	 */
	public String getMessageKey() {
		return getClass().getSimpleName() + "." + toString();
	}
}
