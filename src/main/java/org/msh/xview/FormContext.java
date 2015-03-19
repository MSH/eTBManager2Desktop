package org.msh.xview;

import java.util.List;

import org.msh.xview.components.XForm;

/**
 * A form context is a platform dependent implementation of the form and represents
 * the instance of a form. Initially it will implement the binding between the data 
 * model and the user interface, but it also may store the UI form itself 
 * (like controls, widgets, etc).
 * <p/>
 * An adapter is created for each form created and may last within the life of 
 * the form.
 * 
 * @author Ricardo Memoria
 *
 */
public interface FormContext {

	/**
	 * Called when the form adapter is created to initialize its values
	 * @param form instance of the {@link XForm} containing information about the form 
	 */
	void initialize(XForm form);
	
	/**
	 * Return the form structure for this adapter
	 * @return
	 */
	XForm getForm();
	
	/**
	 * Validate the components of the form.
	 * @return true if all components passed by validation control, otherwise return false
	 * if validation fails. In this case the form must display the messages
	 */
	boolean validate();


	/**
	 * Save the form content calling the method save of all variable controllers (if available).
	 * Before saving, the method will call the validate and applyValues method. If it fails
	 * on validating the data the result value will be false.
	 * 
	 * @return true if form data was successfully save
	 */
//	boolean save();
	
	/**
	 * Add a new message to the form. If it's a global message, the componentId parameter
	 * must be null
	 * @param componentId
	 * @param message
	 * @return
	 */
	FormMessage addMessage(String componentId, String message);
	
	/**
	 * Return the list of global messages, i.e, messages that are not assigned to 
	 * any component
	 * @return
	 */
	List<FormMessage> getGlobalMessages();
	
	/**
	 * Return the list of all messages
	 * @return
	 */
	List<FormMessage> getMessages();
	
	
	/**
	 * Clear the list of messages
	 */
	void clearMessages();
	
	/**
	 * Apply values from the user interface to the data model (variables)
	 * 
	 * @param validateBefore if true, the form will be validated before values are applied. 
	 * If false, values will be applied to the variables without validation
	 * @return true if the values were applied successfully, or false if there was a validation
	 * error or if there was an error to apply a value to a variable reference, so the
	 * you must check the messages in order to get description of the error
	 */
	boolean applyValues(boolean validateBefore);

	
	
	/**
	 * Return the instance of the {@link FormDataModel} implemented in this form context
	 * @return
	 */
	FormDataModel getDataModel();

	
	/**
	 * Helper function to return a value from the data model of the form context
	 * It's the same as calling <Code>FormContext.getDataModel().getValue(String)</code>
	 * @param varname is a reference to a variable or a variable field
	 * @return the value of the reference
	 */
	Object getValue(String varname);
	
	
	/**
	 * Wrapper method to set the value of a variable or its field in the form context.
	 * It's the same as calling <code>FormContext.getDataModel().setValue(String, Object)</code>
	 * @param valuered is a reference to a variable by its name or a variable field
	 * @param value is the new value of the value reference
	 */
	void setValue(String valuered, Object value);

	/**
	 * Return true if it's applying values to the data model (after validation)
	 * @return
	 */
	boolean isApplyingValues();

	/**
	 * Add a form interceptor that will receive form events
	 * @param formInterceptor instance of the {@link FormInterceptor}
	 */
	void addFormInterceptor(FormInterceptor formInterceptor);

	/**
	 * Remove a form interceptor previously added
	 * @param formInterceptor instance of the {@link FormInterceptor}
	 */
	void removeFormInterceptor(FormInterceptor formInterceptor);
	
}
