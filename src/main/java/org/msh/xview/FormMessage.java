package org.msh.xview;

/**
 * Define a message to be displayed in the form. If the property <code>componentId</code>
 * is defined, so the message is assigned to an specific component,
 * otherwise, it's a global message of the form
 * @author Ricardo Memoria
 *
 */
public class FormMessage {

	private String componentId;
	
	private String message;

	public FormMessage() {
		super();
	}
	
	public FormMessage(String componentId, String message) {
		super();
		this.componentId = componentId;
		this.message = message;
	}

	/**
	 * @return the componentId
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
