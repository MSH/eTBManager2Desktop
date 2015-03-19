package org.msh.core;

/**
 * Objects implement this interface when they want to expose
 * a method to return the message key of the message file
 * to be displayed to the user
 *  
 * @author Ricardo Memoria
 *
 */
public interface Displayable {

	/**
	 * Return the message key located in the file with the messages
	 * of the current locale
	 * @return
	 */
	String getMessageKey();
}
