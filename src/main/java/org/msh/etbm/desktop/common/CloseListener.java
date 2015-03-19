/**
 * 
 */
package org.msh.etbm.desktop.common;

/**
 * Interface that a panel must implement to be notified when its being
 * disposed from the container it is
 * 
 * @author Ricardo Memoria
 *
 */
public interface CloseListener {

	/**
	 * Called before the panel is being disposed in its container
	 */
	void closeHandler();
}
