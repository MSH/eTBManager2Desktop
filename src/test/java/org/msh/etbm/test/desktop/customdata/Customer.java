/**
 * 
 */
package org.msh.etbm.test.desktop.customdata;

import org.msh.customdata.CustomObject;
import org.msh.customdata.CustomProperties;
import org.msh.customdata.CustomPropertiesImpl;

/**
 * A simple class for testing of the custom object library
 * 
 * @author Ricardo Memoria
 *
 */
public class Customer implements CustomObject {

	private Integer id;
	private CustomProperties customProperties;
	
	/** {@inheritDoc}
	 */
	@Override
	public CustomProperties getCustomProperties() {
		if (customProperties == null) {
			customProperties = new CustomPropertiesImpl();
		}
		return customProperties;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getCustomPropertiesId() {
		return id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
