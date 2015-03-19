/**
 * 
 */
package org.msh.customdata;


/**
 * Interface that must be implemented for every class that wants to
 * expose the {@link CustomProperties} object, in order to include
 * custom properties in the object
 * 
 * @author Ricardo Memoria
 *
 */
public interface CustomObject {

	/**
	 * Implementation of the {@link CustomProperties} interface that
	 * will store the custom properties of the object
	 * @return implementation of the {@link CustomProperties}
	 */
	CustomProperties getCustomProperties();
	
	/**
	 * Return the ID to be used to store the custom properties of this object
	 * @return
	 */
	Object getCustomPropertiesId();
}
