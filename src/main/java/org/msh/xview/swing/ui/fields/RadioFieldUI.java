/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Ricardo Memoria
 *
 */
public class RadioFieldUI extends FieldComponentUI {

	private static final String VALUE_PROPERTY = "etbm.option.value";
	
	private List<JRadioButton> buttons;

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JPanel pnl = new JPanel();
		pnl.setLayout( null );
		Collection lst = getOptions();
		ButtonGroup grp = new ButtonGroup();

		int y = 2;
		if (lst != null) {
			buttons = new ArrayList<JRadioButton>();
			for (Object obj: lst) {
				JRadioButton btn = new JRadioButton(getDisplayString(obj));
				btn.putClientProperty(VALUE_PROPERTY, obj);
				grp.add(btn);
				pnl.add(btn);
				Dimension d = btn.getPreferredSize();
				btn.setBounds(2, y, (int)d.getWidth(), (int)d.getHeight());
				y += d.getHeight() + 3;
				buttons.add(btn);
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						notifyValueChange((JRadioButton)e.getSource());
					}
				});
			}
		}
		pnl.setSize(200, y);
		pnl.setPreferredSize(new Dimension(200, y));

		return pnl;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		Object value = getValue();
		for (JRadioButton btn: buttons) {
			if (btn.getClientProperty(VALUE_PROPERTY) == value) {
				btn.setSelected(true);
				break;
			}
			else {
				if (btn.isSelected()) {
					btn.setSelected(false);
				}
			}
		}
	}

	/**
	 * Called when the value is changed
	 * @param btn
	 */
	protected void notifyValueChange(JRadioButton btn) {
		Object value = btn.getClientProperty(VALUE_PROPERTY);
		setValue(value);
	}
	
	
}
