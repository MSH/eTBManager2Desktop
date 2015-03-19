package org.msh.xview.swing.event;

/**
 * Interface that all class must implement in order to receive notifications
 * about events that happen inside the form
 * 
 * @author Ricardo Memoria
 *
 */
public interface FormEventHandler {

	/**
	 * Form event handler
	 * @param event instance of {@link FormEvent}
	 */
	void onFormEvent(FormEvent event);
}
