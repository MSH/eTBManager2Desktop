/**
 * 
 */
package org.msh.etbm.desktop.common;

import javax.swing.JLabel;

import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.components.AwesomeIcon;

/**
 * Extension of the {@link JLabel} component setting the color and 
 * font to display a warning message to the user 
 * @author Ricardo Memoria
 *
 */
public class JWarningLabel extends JLabel{
	private static final long serialVersionUID = 5364376017618799391L;

	/**
	 * Default constructor
	 */
	public JWarningLabel() {
		super();
		initialize();
	}

	/**
	 * Constructor defining text and horizontal alignment
	 * @param text
	 * @param horizontalAlignment
	 */
	public JWarningLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initialize();
	}

	/**
	 * Constructor defining text to display
	 * @param text
	 */
	public JWarningLabel(String text) {
		super(text);
		initialize();
	}
	
	protected void initialize() {
		setIcon(new AwesomeIcon(AwesomeIcon.ICON_EXCLAMATION_SIGN, UiConstants.warningColor, 20));
		setFont(UiConstants.warningFont);
		setForeground(UiConstants.warningColor);
	}
}
