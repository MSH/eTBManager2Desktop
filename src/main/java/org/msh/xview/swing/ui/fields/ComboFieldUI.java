/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.msh.etbm.desktop.common.SelectItem;


/**
 * Combo box editor for fields in the XView library
 * @author Ricardo Memoria
 *
 */
public class ComboFieldUI extends FieldComponentUI {

	/**
	 * Default action listener to answer to changes in the combo box selection
	 */
	private final ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			comboboxChangeHandler();
		}
	}; 


	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JComboBox cb = new JComboBox();
		
		cb.setSize(200, cb.getPreferredSize().height);
		cb.addItem("-");
		cb.addActionListener(actionListener);

		return cb;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Collection options = getOptions();
		JComboBox cb = (JComboBox)getComponent();
		
		cb.removeAllItems();

		cb.addItem("-");
		// mount options
		if (options != null) {
			for (Object val: options) {
				cb.addItem(new SelectItem(val, getDisplayString(val)));
			}
		}

		// select item in the combo box
		Object value = getValue();
		if (value != null) {
			for (int i = 1; i < cb.getItemCount(); i++) {
				SelectItem item = (SelectItem)cb.getItemAt(i);
				if (item.getValue().equals(value)) {
					cb.setSelectedIndex(i);
					break;
				}
			}
		}
		else cb.setSelectedIndex(0);

		// set combobox width
		Dimension d = cb.getPreferredSize();
		Integer w = getView().getWidth();
		if (w == null) {
			w = (int)d.getWidth();
		}
		cb.setSize(w, (int)d.getHeight());
	}


	/**
	 * Called when the combo box selection changes 
	 */
	private void comboboxChangeHandler() {
		if (isUpdating()) {
			return;
		}

		JComboBox cb = (JComboBox)getComponent();
		
		int index = cb.getSelectedIndex();
		
		if (index <= 0) {
			setValue(null);
			return;
		}

		SelectItem item = (SelectItem)cb.getSelectedItem();
		
		setValue( item != null? item.getValue(): null );
	}
}
