/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.msh.etbm.desktop.app.Messages;

/**
 * A simple combo box editor for boolean values to select YES or NO options
 * 
 * @author Ricardo Memoria
 *
 */
public class YesNoFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JComboBox combo = new JComboBox();
		combo.addItem("-");
		combo.addItem(Messages.getString("global.yes"));
		combo.addItem(Messages.getString("global.no"));
		
		Dimension d = combo.getPreferredSize();
		combo.setSize(d.width, d.height);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyValueChange();
			}
		});
		return combo;
	}

	/**
	 * Receives notification when the combo box selection changes
	 */
	protected void notifyValueChange() {
		JComboBox combo = (JComboBox)getComponent();
		switch (combo.getSelectedIndex()) {
		case 1:
			setValue(Boolean.TRUE);
			break;
		case 2:
			setValue(Boolean.FALSE);
			break;
		default:
			setValue(null);
			break;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		JComboBox combo = (JComboBox)getComponent();
		Object value = getValue();

		// update value selection
		if (Boolean.TRUE.equals( value ))
			combo.setSelectedIndex(1);
		else 
		if (Boolean.FALSE.equals( value ))
			combo.setSelectedIndex(2);
		else combo.setSelectedIndex(0);
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.swing_old.editors.AbstractEditor#getDisplayString()
	 */
	@Override
	public String getDisplayText() {
		Object value = getValue();
		if (value == null)
			return "";
		
		if (Boolean.TRUE.equals(value))
			return Messages.getString("global.yes");
		
		if (Boolean.FALSE.equals(value))
			return Messages.getString("global.no");
		
		return super.getDisplayText();
	}

}
