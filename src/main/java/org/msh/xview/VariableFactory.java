package org.msh.xview;

/**
 * Interface that must be implemented in order to get an instance of a variable
 * 
 * @author Ricardo Memoria
 *
 */
public interface VariableFactory {

	Object createVariableInstance(String varname);
}
