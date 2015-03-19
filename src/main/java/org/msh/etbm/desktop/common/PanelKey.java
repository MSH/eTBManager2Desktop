package org.msh.etbm.desktop.common;

/**
 * Interface that return a unique key for a panel. This interface must be implemented
 * in all panels that are displayed in the main window in order to avoid the same
 * panel to be opened twice. Before opening the panel, the system will check if a panel
 * with same key is already available, and if so, the available one will be displayed
 * 
 * @author Ricardo Memoria
 *
 */
public interface PanelKey {

	/**
	 * Return an unique key for a panel that will identify it over the other panels
	 * 
	 * @return Object value
	 */
	public Object getKey();
}
