/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import org.msh.xview.swing.component.JTextFieldEx;
import org.msh.xview.swing.component.JTextFieldEx.CharCase;

/**
 * Default editor for string fields in the XView library for swing app
 * 
 * @author Ricardo Memoria
 *
 */
public class StringFieldUI extends FieldComponentUI {

	private KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			handleKeyReleased();
		}
	};

	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JComponent comp;

		String mask = getView().getInputMask();

		// mask defined ?
		if (mask != null) {
			// create a mask component
			MaskFormatter mf = null;
			try {
				mf = new MaskFormatter(mask);
				mf.setPlaceholderCharacter('_');
			} catch (ParseException e) {
				System.out.println("Invalid mask " + mask + " in component " + getId());
			}
			comp = new JFormattedTextField(mf);
			comp.addKeyListener(keyListener);
		}
		else {
			// create a regular edit component
			comp = new JTextFieldEx();
			JTextFieldEx edt = new JTextFieldEx();
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

			Integer max = getView().getMaxChars();
			if (max == null)
				edt.setMaxLength(0);
			else edt.setMaxLength(max);
			edt.addKeyListener(keyListener);
			
			comp = edt;
		}

		// get the component size
		Integer width = getView().getWidth();
		if (width == null) {
			width = 130;
		}
		
		Dimension d = comp.getPreferredSize();
		d.setSize(width, (int)d.getHeight());
		comp.setSize(d);
		comp.setPreferredSize(d);
		comp.setMinimumSize(new Dimension(10, (int)d.getHeight()));
		return comp;
	}


	/** {@inheritDoc}
	 */
	@Override
	public void updateEditComponent() {
		Object value = getValue();
		setInputText(value != null? value.toString(): "");
		Integer width = getView().getWidth();
		if (width != null) {
			getComponent().setSize(width, (int)getComponent().getPreferredSize().getHeight());
		}
	}

	
	/**
	 * Set the text inside the input box
	 * @param value
	 */
	public void setInputText(String value) {
		JComponent comp = getComponent();

		if (comp instanceof JFormattedTextField) {
			((JFormattedTextField)comp).setText(value);
		}
		else {
			((JTextFieldEx)comp).setText(value);
		}
	}
	
	/**
	 * Return the text in the input text component
	 * @return
	 */
	public String getInputText() {
		JComponent comp = getComponent();

		if (comp instanceof JFormattedTextField) {
			return ((JFormattedTextField)comp).getText();
		}
		else {
			return ((JTextFieldEx)comp).getText();
		}
	}
	
	/**
	 * Called when the text changes in the input text
	 */
	protected void handleKeyReleased() {
		beginUpdate();
		try {
			String s = getInputText();
			// check about empty text
			if (s.isEmpty()) {
				s = null;
			}
			setValue(s);
		} finally {
			finishUpdate();
		}
	}
}
