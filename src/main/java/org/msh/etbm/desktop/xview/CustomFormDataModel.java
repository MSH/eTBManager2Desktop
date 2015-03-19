/**
 * 
 */
package org.msh.etbm.desktop.xview;

import org.msh.customdata.CustomObject;
import org.msh.customdata.CustomProperties;
import org.msh.xview.impl.DefaultFormDataModel;

/**
 * Extension of the {@link DefaultFormDataModel} supporting variables
 * that implement the interface {@link CustomObject}
 *  
 * @author Ricardo Memoria
 *
 */
public class CustomFormDataModel extends DefaultFormDataModel {

	/** {@inheritDoc}
	 */
	@Override
	protected void setProperty(Object obj, String property, Object value) {
		if (obj instanceof CustomProperties) {
			((CustomProperties)obj).setValue(property, value);
			return;
		}

		String[] props = property.split("\\.");
		if (props.length > 1) {
			String subprop = property.substring(props[0].length() + 1);
			Object propvalue = getProperty(obj, props[0]);
			setProperty(propvalue, subprop, value);
		}
		else {
			super.setProperty(obj, property, value);
		}
	}


	/** {@inheritDoc}
	 */
	@Override
	protected Object getProperty(Object obj, String property) {
		if (obj instanceof CustomProperties) {
			return ((CustomProperties)obj).getValue(property);
		}
		
		String[] props = property.split("\\.");
		if (props.length > 1) {
			String subprop = property.substring(props[0].length() + 1);
			Object propvalue = getProperty(obj, props[0]);
			return getProperty(propvalue, subprop);
		}
		else {
			return super.getProperty(obj, property);
		}
	}


}
