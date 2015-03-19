/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * Editor that displays a combo box for the number selection. 
 * Just supports {@link Integer} and {@link Long} types, and its
 * primitive representations
 * 
 * @author Ricardo Memoria
 *
 */
public class NumberComboFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		// create the combo
		JComboBox combo = new JComboBox();
		combo.addItem("-");
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyValueChange();
			}
		});
		
		// check minimum and maximum limits of the combo
		Integer min = getIntParam("min");
		Integer max = getIntParam("max");
		if ((min == null) && (max == null))
			throw new IllegalArgumentException("Parameters min and max must be defined for field " + getView().getField().toString());
		
		if (min > max)
			throw new IllegalArgumentException("Parameters min and max are incorrect: min <= max is false for field" + getView().getField().toString());

		// check type
		Class type = getFieldClassType();
		if ((type.isAssignableFrom(Integer.class)) || (type.isAssignableFrom(int.class))) {
			for (int i = min; i <= max; i++)
				combo.addItem(new Integer(i));
		}
		else
		if ((type.isAssignableFrom(Integer.class)) || (type.isAssignableFrom(int.class))) {
			for (int i = min; i <= max; i++)
				combo.addItem(new Long(i));
		}
		else throw new IllegalArgumentException("Type not supported " + type);

		// adjust size, otherwise it will not be displayed
		Dimension d = combo.getPreferredSize();
		combo.setSize(d.width, d.height);

		return combo;
	}

	/**
	 * Receives notification when the value in the combo box is changed
	 */
	protected void notifyValueChange() {
		JComboBox cb = (JComboBox)getComponent();
		
		int index = cb.getSelectedIndex();
		
		if (index <= 0) {
			return;
		}

		setValue( cb.getSelectedItem());
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Object value = getValue();
		JComboBox combo = (JComboBox)getComponent();

		// update the value of the combo
		if (value == null)
			 combo.setSelectedIndex(0);
		else combo.setSelectedItem(value);
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
	
}
