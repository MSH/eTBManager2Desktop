/**
 * 
 */
package org.msh.xview;

/**
 * Interceptor of basic functions of the form
 * @author "Ricardo Memoria"
 *
 */
public interface FormInterceptor {

	/**
	 * Called before an update in the form
	 * @param formContext
	 */
	void beforeFormUpdate(FormContext formContext);

	/**
	 * Called after an update in the form
	 * @param formContext
	 */
	void afterFormUpdate(FormContext formContext);
}
