/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import org.msh.etbm.desktop.components.JPersonNameEdit;
import org.msh.etbm.entities.PersonNameComponent;
import org.msh.xview.swing.component.JTextFieldEx.CharCase;

/**
 * @author Ricardo Memoria
 *
 */
public class PersonNameFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JPersonNameEdit edt = new JPersonNameEdit();

		// add listener
		edt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				nameChanged();
			}
		});
		return edt;
	}

	/**
	 * Receives notification when the name editor is changed
	 */
	protected void nameChanged() {
		JPersonNameEdit edt = (JPersonNameEdit)getComponent();
		setValue( edt.getName() );
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		JPersonNameEdit edt = (JPersonNameEdit)getComponent();

		switch (getView().getCharCase()) {
		case LOWER:
			edt.setCharCase(CharCase.LOWER);
			break;
		case UPPER:
			edt.setCharCase(CharCase.UPPER);
			break;
		default:
			edt.setCharCase(CharCase.NORMAL);
			break;
		}
		
		PersonNameComponent name = (PersonNameComponent)getValue();
		edt.setPersonName(name);
		edt.setSize(edt.getPreferredSize());
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getDisplayText() {
		PersonNameComponent pn = (PersonNameComponent)getValue();
		if (pn == null)
			return "";
		return pn.getDisplayName();
	}

}
