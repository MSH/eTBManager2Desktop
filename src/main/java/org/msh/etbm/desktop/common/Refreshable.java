package org.msh.etbm.desktop.common;

/**
 * All contents that want to be refreshed by its parent must implement this interface
 * 
 * @author Ricardo Memoria
 *
 */
public interface Refreshable {

	/**
	 * Refresh its content 
	 */
	void refresh();
}
