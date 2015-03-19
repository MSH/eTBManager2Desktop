/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Visual editor that displays a text editor with multiple lines.
 * 
 * @author Ricardo Memoria
 *
 */
public class TextFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JTextArea textArea = new JTextArea(6,0);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				notifyValueChange();
			}
		});
		JScrollPane comp = new JScrollPane(textArea);
		comp.setSize(300, 80);
		return comp;
	}

	/**
	 * Receives notification of when the value changes in the text area 
	 */
	protected void notifyValueChange() {
		String s = getTextArea().getText();
		setValue(s);
	}

	
	/**
	 * Return the text area component being edited
	 * @return
	 */
	protected JTextArea getTextArea() {
		JScrollPane sp = (JScrollPane)getComponent();
		return (JTextArea)sp.getViewport().getView();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		getTextArea().setText( (String)getValue() );
	}

	@Override
	protected void updateEditWidth(int width) {
		getComponent().setSize(width, getComponent().getHeight());
	}
}
