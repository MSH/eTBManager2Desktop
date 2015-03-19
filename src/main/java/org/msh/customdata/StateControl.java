/**
 * 
 */
package org.msh.customdata;

/**
 * @author Ricardo Memoria
 *
 */
public interface StateControl {

	boolean isChanged();
	
	void commitChanges();
	
	boolean isNew();
	
	void setNew(boolean value);
}
