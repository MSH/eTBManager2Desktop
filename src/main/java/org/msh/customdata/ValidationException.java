/**
 * 
 */
package org.msh.customdata;

/**
 * Exception that is thrown when an object validation happens
 *  
 * @author Ricardo Memoria
 *
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 2137673183658775982L;

	private String propertyName;

	/**
	 * Default message, including the property name where exception happened and
	 * the message
	 * @param propertyName is the name of the property in the custom object
	 * @param message is the validation error message
	 */
	public ValidationException(String propertyName, String message) {
		super(message);
	}
	
	
	/**
	 * Constructor that is called when validation exception happened in the object
	 * level, and not in a specific property
	 * @param message is the validation error message
	 */
	public ValidationException(String message) {
		
	}
	
	@Override
	public String toString() {
		if (propertyName != null) {
			return "Field " + propertyName + ": " + getMessage();
		}
		else {
			return super.toString();
		}
	}
}
