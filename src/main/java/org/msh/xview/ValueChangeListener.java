package org.msh.xview;

/**
 * Interface that a class must implement in order to receive notification
 * about value changes in the {@link FormDataModel} class
 * 
 * @author Ricardo Memoria
 *
 */
public interface ValueChangeListener {

	/**
	 * Callback function that is called when a field value is changed
	 * @param field
	 */
	void onValueChange(String field, Object oldValue, Object newValue);
}
