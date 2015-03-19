/**
 * 
 */
package org.msh.customdata;

import java.util.Set;

/**
 * This is a simple interface that is exposed by the {@link CustomObject} interface.
 * The implementation of this interface will store the custom properties
 * and its values of an object. 
 * 
 * @author Ricardo Memoria
 *
 */
public interface CustomProperties {

	/**
	 * Change the value of a custom property
	 * @param propertyName is the property name
	 * @param value is the new value of this property
	 */
	void setValue(String propertyName, Object value);


	/**
	 * Return the value of a custom property
	 * @param propertyName is the property name
	 * @return the value of the property, or null if the value or property doesn't exist
	 */
	Object getValue(String propertyName);

	/**
	 * Return the list of all custom properties stored in the object
	 * @return List of string values containing the name of the property
	 */
	Set<String> getProperties();
	
	/**
	 * Delete a custom property from an object
	 * @param propertyName is the property name
	 */
	void deleteProperty(String propertyName);
	
	/**
	 * Clear all property values
	 */
	void clear();
}
