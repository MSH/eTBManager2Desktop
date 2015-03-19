/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ricardo Memoria
 *
 */
public class CheckboxFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JCheckBox chk = new JCheckBox();
		chk.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				notifyValueChange();
			}
		});
		chk.setSize(chk.getPreferredSize());
		return chk;
	}

	/**
	 * Called when the check box state changes
	 */
	protected void notifyValueChange() {
		JCheckBox chk = (JCheckBox)getComponent();
		setValue(chk.isSelected());
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Object value = getValue();
		JCheckBox chk = (JCheckBox)getComponent();
		chk.setSelected((value != null) && (Boolean.TRUE.equals(value))); 
	}

}
