/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Editor for number fields
 * 
 * @author Ricardo Memoria
 *
 */
public class NumberFieldUI extends FieldComponentUI {

	// store the minimum value allowed as the default value
	private Number minimumValue;

	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JSpinner edt;
		edt = new JSpinner(createNumberModel());
		
		Integer width = getView().getWidth();
		if (width == null) {
			width = 80;
		}
		edt.setSize(width, edt.getPreferredSize().height);
		edt.setPreferredSize(new Dimension(width, edt.getPreferredSize().height));
		edt.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				notifyValueChange();
			}
		});
		return edt;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Object value = getValue();
		JSpinner edt = (JSpinner)getComponent();

		if (value == null) {
			if (minimumValue == null)
				 edt.setValue(0);
			else edt.setValue(minimumValue);
		}
		else edt.setValue((Number)value);
	}

	
	/**
	 * Called when the value is changed in the component
	 */
	protected void notifyValueChange() {
		Number value = (Number)((JSpinner)getComponent()).getValue();
		setValue( convertNumberToFieldType( value ));
	}
	
	
	/**
	 * Convert the number to the expected field type
	 * @param value instance of {@link Number}, i.e, float, int, double or long
	 * @return instance of {@link Number} converted to the field type
	 */
	protected Number convertNumberToFieldType(Number value) {
		Class clazz = getFieldClassType();
		if ((clazz == Integer.class) || (clazz == int.class)) {
			return value.intValue();
		}
		
		if ((clazz == Long.class) || (clazz == long.class)) {
			return value.longValue();
		}
		
		if ((clazz == Double.class) || (clazz == double.class)){ 
			return value.doubleValue();
		}
		
		if ((clazz == Float.class) || (clazz == float.class)) {
			return value.floatValue();
		}
		
		return value;
	}


	/**
	 * Return the number in the field type
	 * @return instance of {@link Number}
	 */
	protected Number getValueInFieldClass() {
		Number value = (Number)getValue();
		
		return convertNumberToFieldType( value );
	}
	
	
	/**
	 * Create model based on the number type
	 * @return
	 */
	protected SpinnerNumberModel createNumberModel() {
		Class type = getFieldClassType();

		if ((type.isAssignableFrom(Integer.class)) || (type.isAssignableFrom(int.class))) {
			Integer min = getIntParam("min");
			Integer max = getIntParam("max");
			Integer val;
			if (min != null)
				 val = min;
			else val = new Integer(1);
			return new SpinnerNumberModel(val, min, max, new Integer(1));
		}
		
		if ((type.isAssignableFrom(Long.class)) || (type.isAssignableFrom(long.class))) {
			Long min = getLongParam("min");
			minimumValue = min;
			Long max = getLongParam("max");
			Long val;
			if (min != null)
				 val = min;
			else val = new Long(1);
			return new SpinnerNumberModel(val, min, max, new Long(1));
		}
		
		if ((type.isAssignableFrom(Float.class)) || (type.isAssignableFrom(float.class))) {
			Float min = getFloatParam("min");
			minimumValue = min;
			Float max = getFloatParam("max");
			Float val;
			if (min != null)
				 val = min;
			else val = new Float(1);
			return new SpinnerNumberModel(val, min, max, new Float(1));
		}
		
		if ((type.isAssignableFrom(Double.class)) || (type.isAssignableFrom(double.class))) {
			Double min = getDoubleParam("min");
			minimumValue = min;
			Double max = getDoubleParam("max");
			Double val;
			if (min != null)
				 val = min;
			else val = new Double(1);
			return new SpinnerNumberModel(val, min, max, new Double(1));
		}
		
		throw new IllegalArgumentException("type " + type + " not supported by editor");
	}
	/**
	 * Get a parameter defined in the field as a {@link Integer} value
	 * 
	 * @param paramname
	 * @return
	 */
	protected Integer getIntParam(String paramname) {
		String s = getView().getParameter(paramname);
		if (s == null)
			return null;

		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			System.out.println("Error converting parameter " + paramname + " with value " + s + " to Integer");
		}
		
		return null;
	}
	
	
	/**
	 * Get a parameter defined in the field as a {@link Long} value
	 * 
	 * @param paramname
	 * @return
	 */
	protected Long getLongParam(String paramname) {
		String s = getView().getParameter(paramname);
		if (s == null)
			return null;

		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			System.out.println("Error converting parameter " + paramname + " with value " + s + " to Long");
		}
		
		return null;
	}
	
	/**
	 * Return a parameter defined in the field as a {@link Double} value
	 * 
	 * @param paramname
	 * @return
	 */
	protected Double getDoubleParam(String paramname) {
		String s = getView().getParameter(paramname);
		if (s == null)
			return null;

		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			System.out.println("Error converting parameter " + paramname + " with value " + s + " to Double");
		}
		
		return null;
	}
	
	
	/**
	 * Get a parameter defined in the field as a {@link Float} value
	 * 
	 * @param paramname
	 * @return
	 */
	protected Float getFloatParam(String paramname) {
		String s = getView().getParameter(paramname);
		if (s == null)
			return null;

		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			System.out.println("Error converting parameter " + paramname + " with value " + s + " to Float");
		}
		
		return null;
	}

}
