/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXLabel;

/**
 * @author Ricardo Memoria
 *
 */
public class ReadOnlyUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JXLabel lbl = new JXLabel();
		lbl.setLineWrap(true);
		return lbl;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Object val = getValue();
		JXLabel lbl = (JXLabel)getComponent();
		
		if (val == null) {
			lbl.setText("");
		}
		else {
			lbl.setText(val.toString());
		}
	}

}
