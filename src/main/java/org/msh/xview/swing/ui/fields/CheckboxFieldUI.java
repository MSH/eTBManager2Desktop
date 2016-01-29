/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.components.AwesomeIcon;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

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

    @Override
    protected void updateReadOnlyComponent() {
        JXLabel lbl = (JXLabel)getComponent();
        Boolean val = (Boolean)getValue();
        if (val != null && val) {
            lbl.setText("");
            lbl.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CHECK_SIGN, new Color(8, 153, 73), 14));
        }
        else {
            lbl.setText("-");
            lbl.setIcon(null);
        }
    }

    @Override
    public String getDisplayText() {
        Boolean val = (Boolean)getValue();
        if (val == null) {
            return "";
        }
        return val ? "x" : "-";
    }
}
