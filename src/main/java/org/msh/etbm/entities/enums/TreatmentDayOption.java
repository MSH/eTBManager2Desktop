/**
 * 
 */
package org.msh.etbm.entities.enums;

import org.msh.core.Displayable;

/**
 * Options for the registration of the treatment monitoring for a day of treatment
 * 
 * @author Ricardo Memoria
 *
 */
public enum TreatmentDayOption implements Displayable {

	NOT_TAKEN,
	DOTS,
	SELF_ADMIN;

	/** {@inheritDoc}
	 */
	@Override
	public String getMessageKey() {
		return getClass().getSimpleName() + "." + toString();
	}
	
}
