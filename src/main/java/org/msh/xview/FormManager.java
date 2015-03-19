package org.msh.xview;

/**
 * Interface that implements a form manager, responsible for creating new forms adapter
 * 
 * @author Ricardo Memoria
 *
 */
public interface FormManager {

	/**
	 * Create a new form adapter to the given form structure
	 * @param formId
	 * @return
	 */
	FormContext createFormAdapter(String formId);

	/**
	 * Include a form interceptor
	 * @param interceptor instance of the {@link FormInterceptor}
	 */
	void addInterceptor(FormInterceptor interceptor);
	
	/**
	 * Remove a form interceptor
	 * @param interceptor instance of the {@link FormInterceptor}
	 */
	void removeInterceptor(FormInterceptor interceptor);
}
