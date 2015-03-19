/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXLabel;
import org.msh.xview.components.XText;
import org.msh.xview.swing.XViewUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class AbstractTextUI<E extends XText> extends ComponentUI<XText> {

	private String text;
	private ExpressionWrapper<String> expLabel;
	

	/**
	 * Return the label to be displayed
	 * @return
	 */
	public String getText() {
		if (expLabel == null) {
			String s = text != null? text: getView().getText();
			
			if (s == null) {
				return null;
			}
			
			expLabel = new ExpressionWrapper<String>(this, s, String.class);
		}
	
		return expLabel.getValue();
	}
	
	
	/**
	 * Set the text to be displayed
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	
	/**
	 * Return the label that is used to display the text
	 * @return instance of {@link JXLabel}
	 */
	public JXLabel getLabelComponent() {
		return (JXLabel)getComponent();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JXLabel text = new JXLabel();
		// just an initial size
//		text.setSize(getForm().getLabelWidth(), 30);
		text.setLineWrap(true);
		text.setVerticalAlignment(SwingConstants.TOP);
		return text;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		JXLabel lbl = (JXLabel)getComponent();
		int h = XViewUtils.calcTextHeight2(lbl, width, getText());
		lbl.setSize(width, h);
		lbl.setMinimumSize(new Dimension(20, (int)lbl.getMinimumSize().getHeight()));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void doComponentUpdate() {
		JXLabel text = (JXLabel)getComponent();

		if (isVisible()) {
			text.setVisible(true);
			String s = getText();
			text.setText(s);
		}
		else text.setVisible(false);
	}


	/**
	 * Return the form where this label is in
	 * @return instance of the {@link FormUI} class
	 */
	protected FormUI getForm() {
		return ((FormUI)getRootView());
	}

}
