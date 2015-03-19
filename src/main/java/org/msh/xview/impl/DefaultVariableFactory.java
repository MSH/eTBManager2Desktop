package org.msh.xview.impl;

import org.msh.xview.VariableFactory;

/**
 * Instantiate a new instance of the variable class through the {@link VariableFactory} interface
 *  
 * @author Ricardo Memoria
 *
 */
public class DefaultVariableFactory implements VariableFactory{

	private Class variableClass;

	/**
	 * The controller class is passed as parameter to the constructor
	 * @param controllerClass
	 */
	public DefaultVariableFactory(Class variableClass) {
		super();
		this.variableClass = variableClass;
	}
	
	/** {@inheritDoc}
	 * @see org.msh.xview.VariableFactory#createVariableInstance(java.lang.String)
	 */
	@Override
	public Object createVariableInstance(String varname) {
		try {
			return variableClass.newInstance();
		} catch (Exception e) {
			throw new IllegalAccessError("Error when creating instance of " + variableClass);
		}
	}

}
