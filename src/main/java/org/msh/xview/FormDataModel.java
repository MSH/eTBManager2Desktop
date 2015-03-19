package org.msh.xview;

import java.util.Set;

import javax.el.ELContext;
import javax.el.ValueExpression;


/**
 * Available interface for a form data model manipulation
 * 
 * @author Ricardo Memoria
 *
 */
public interface FormDataModel  {

	/**
	 * Return the context to execute expressions
	 * @return instance of {@link ELContext}
	 */
	ELContext getELContext();

	/**
	 * Create a value expression from a given expression 
	 * @param expression
	 * @param expectedClassResult
	 * @return instance of {@link ValueExpression}
	 */
	ValueExpression createValueExpression(String expression, Class expectedClassResult);
	
	/**
	 * Return the class type of a given field reference
	 * @param valueref is the reference to a value
	 * @return Class of the value reference
	 */
	Class getValueType(String valueref);

	
	/**
	 * Get the value of a field name. The field name is represented by its
	 * variable name + property reference, separated by dots
	 * @param fieldname
	 * @return
	 */
	Object getValue(String fieldname);
	
	/**
	 * Get the variable in use in the form by its name. This method is intended to
	 * create a typed reference to get reference of a variable in use in the form.
	 *
	 * @param variableClass class of the variable available in the form
	 * @return instance of the variable class or null if the variable is not instantiated in the form
	 */
	<E> E getVariable(Class<E> variableClass);
	
	/**
	 * Set the value of a given field represented by its field name. The
	 * field name is composed of variable name + property reference, separated by dot
	 * 
	 * @param fieldname
	 * @param value
	 */
	void setValue(String fieldname, Object value);
	
	/**
	 * Return the variable controller of the variable name, if available.
	 * If it's not available, returns null
	 * 
	 * @param field
	 * @return
	 */
//	VariableController getController(String variable);
	
	/**
	 * Add a local variable to the data model (or replace an existing one)
	 * @param varname
	 * @param value
	 */
//	void setVariable(String varname, Object value);
	
	
	/**
	 * Return the list of variables declared in the form
	 * 
	 * @return
	 */
	Set<String> getVariables();


	/**
	 * Add a listener to be called when the value of a field changes
	 * 
	 * @param listener
	 */
	void addChangeListener(ValueChangeListener listener);
	
	/**
	 * Remove listener of the data model. The listener will no longer receive notification of value changes
	 * @param listener
	 */
	void removeChangeListener(ValueChangeListener listener);
	

	/**
	 * Return true if data model contains the variable of given name
	 * @param varname
	 * @return
	 */
	boolean containsVariable(String varname);
	
	/**
	 * Clear all variables contained in the data model. Useful when you want to read again variables
	 * from the database
	 */
	void clear();
}
