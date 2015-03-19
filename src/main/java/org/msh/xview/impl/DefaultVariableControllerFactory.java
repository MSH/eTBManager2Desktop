package org.msh.xview.impl;

import org.msh.etbm.services.core.EntityServices;
import org.msh.xview.VariableControllerFactory;

/**
 * Default implementation of a {@link VariableControllerFactory} where controller
 * is instantiated by invoking Class.newInstance()
 * 
 * @author Ricardo Memoria
 *
 */
public class DefaultVariableControllerFactory implements VariableControllerFactory {

	private Class<? extends EntityServices> controllerClass;

	/**
	 * The controller class is passed as parameter to the constructor
	 * @param controllerClass
	 */
	public DefaultVariableControllerFactory(Class<? extends EntityServices> controllerClass) {
		super();
		this.controllerClass = controllerClass;
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.VariableControllerFactory#createControllerInstance(java.lang.String)
	 */
	@Override
	public EntityServices createControllerInstance(String varname) {
		try {
			return controllerClass.newInstance();
		} catch (Exception e) {
			throw new IllegalAccessError("Error when creating instance of " + controllerClass);
		}
	}

}
