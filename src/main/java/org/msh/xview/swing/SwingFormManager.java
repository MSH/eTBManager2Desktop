package org.msh.xview.swing;

import org.msh.xview.FormManager;
import org.msh.xview.impl.LocalResourceFormManager;

/**
 * Implement a specific instance of {@link FormManager} including information about the component
 * editors of specified field types
 * @author Ricardo Memoria
 *
 */
public class SwingFormManager extends LocalResourceFormManager {
	
	/**
	 * Default constructor
	 * @param resourcePath
	 */
	public SwingFormManager(String resourcePath) {
		super(resourcePath, SwingFormContext.class);
	}

}
