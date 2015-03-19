package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msh.xview.VariableControllerFactory;
import org.msh.xview.VariableFactory;

/**
 * Singleton class that register variable factories and controllers,
 * check if variable names are valid and return instance of these
 * variables
 * 
 * @author Ricardo Memoria
 *
 */
public class VariableCreator  {

	private static VariableCreator myinstance;

	// list of factories for the variables 
	private List<VariableFactory> factories = new ArrayList<VariableFactory>();

	// maintain a map of fields to variables
	private Map<String, String> fieldLinks = new HashMap<String, String>();
	
	
	private VariableCreator() {
		super();
	}

	
	/**
	 * Add a link between a field and the variable pointing to it. It's a way
	 * of declaring variables that are represented by properties of other variables 
	 * 
	 * @param fieldname
	 * @param variable
	 */
	public void addFieldLink(String fieldname, String variable) {
		fieldLinks.put(fieldname, variable);
	}


	/**
	 * Return the list of fields from the given variable name that are linked to other variables
	 * @param fieldname
	 * @return
	 */
	public Map<String, String> getFieldLinks(String varname) {
		String name = varname + '.';
		HashMap<String, String> map = new HashMap<String, String>();
		
		for (String key: fieldLinks.keySet()) {
			if (key.startsWith(name))
				map.put(key, fieldLinks.get(key));
		}

		return map;
	}

	/**
	 * Return a list of field names of other variables that are linked to the given variable name
	 * @param variable
	 * @return
	 */
	public List<String> getFieldLinksToVariable(String variable) {
		List<String> lst = new ArrayList<String>();

		for (String field: fieldLinks.keySet()) {
			String varlink = fieldLinks.get(field);
			if (varlink.equals(variable))
				lst.add(field);
		}
		return lst;
	}
	
	/**
	 * Register a variable class controller
	 * @param varname
	 * @param clazz
	 */
/*	public void registerVariableControllerFactory(String varname, VariableControllerFactory factory) {
		factories.put(varname, new VariableFactoryInfo(factory));
	}
*/

	/**
	 * Register a variable factory, i.e, an implementation of the {@link VariableFactory} that will return
	 * an instance of a variable by its name
	 * @param varname
	 * @param factory
	 */
	public void registerVariableFactory(VariableFactory factory) {
		factories.add(factory);
	}
	
	
	/**
	 * Create an instance of the {@link VariableController} assigned to the variable
	 * @param varname
	 * @return
	 */
/*	public VariableController createVariableController(String varname) {
		VariableFactoryInfo factory = factories.get(varname);
		
		if ((factory == null) || (factory.getControllerFactory() == null))
			return null;
		
		return factory.getControllerFactory().createControllerInstance(varname);
	}
*/

	/**
	 * Create a variable instance from its registered factory
	 * @param varname
	 * @return
	 */
	public Object createVariableFromFactory(String varname) {
		for (VariableFactory factory: factories) {
			Object data = factory.createVariableInstance(varname);
			if (data != null)
				return data;
		}
		return null;
/*		VariableFactoryInfo factory = factories.get(varname);
		if ((factory == null) || (factory.getVariableFactory() == null))
			return null;
		
		return factory.getVariableFactory().createVariableInstance(varname);
*/	}
	
	
	/**
	 * Create instance inside a synchronized thread
	 */
	protected synchronized static void createInstance() { 
		if (myinstance == null)
			myinstance = new VariableCreator();
	}
	
	/**
	 * Return instance of the class
	 * @return
	 */
	public static VariableCreator instance() {
		if (myinstance == null)
			createInstance();
		
		return myinstance;
	}


	/** {@inheritDoc}
	 * @see org.msh.expressions.VariableValidator#isValidVariable(java.lang.String)
	 */
	public boolean isValidVariable(String name) {
		// since the variable is created by the container, it's not possible to 
		// check if the variable is valid or not
		return true;
//		return factories.containsKey(name);
	}


	/**
	 * Store information about the variable factories
	 * 
	 * @author Ricardo Memoria
	 *
	 */
	public class VariableFactoryInfo {
		private VariableControllerFactory controllerFactory;
		private VariableFactory variableFactory;

		public VariableFactoryInfo(VariableFactory variableFactory) {
			super();
			this.variableFactory = variableFactory;
		}

		public VariableFactoryInfo(VariableControllerFactory controllerFactory) {
			super();
			this.controllerFactory = controllerFactory;
		}

		/**
		 * @return the controllerFactory
		 */
		public VariableControllerFactory getControllerFactory() {
			return controllerFactory;
		}

		/**
		 * @return the variableFactory
		 */
		public VariableFactory getVariableFactory() {
			return variableFactory;
		}
	}
}
