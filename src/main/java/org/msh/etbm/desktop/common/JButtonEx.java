package org.msh.etbm.desktop.common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

/**
 * Simple extension of the Swing {@link JButton} component to include a standard
 * look and feel of the buttons and a highlight color for the default button
 * 
 * @author Ricardo Memoria
 *
 */
public class JButtonEx extends JButton {
	private static final long serialVersionUID = -5391062689523711955L;

	private Object data;
	
	public enum ButtonStyle {
		SMALL, STANDARD, BIG;
	};

	private ButtonStyle style;
	
	/** {@inheritDoc}
	 */
	public JButtonEx() {
		super();
	}

	/** {@inheritDoc}
	 */
	public JButtonEx(Action a) {
		super(a);
	}

	/** {@inheritDoc}
	 */
	public JButtonEx(Icon icon) {
		super(icon);
	}

	/** {@inheritDoc}
	 */
	public JButtonEx(String text, Icon icon) {
		super(text, icon);
	}

	/** {@inheritDoc}
	 */
	public JButtonEx(String text) {
		super(text);
	}
	
	
	/**
	 * Constructor informing a text label and the ID of the {@link AwesomeIcon} class
	 * @param text the button label
	 * @param awesomeIcon the ID of the {@link AwesomeIcon} to be displayed
	 */
	public JButtonEx(String text, int awesomeIcon) {
		super(text);
		setIcon(new AwesomeIcon(awesomeIcon, this));
	}
	
	
	/**
	 * Constructor with a text label, ID of an awesome icon and a button style
	 * @param text
	 * @param awesomeIcon
	 * @param style
	 */
	public JButtonEx(String text, int awesomeIcon, ButtonStyle style) {
		super(text);
		setStyle(style);
		setIcon(new AwesomeIcon(awesomeIcon, this));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setDefaultCapable(boolean defaultCapable) {
		super.setDefaultCapable(defaultCapable);
		if (defaultCapable) {
			setForeground(new Color(255, 255, 255));
			setBackground(new Color(70, 130, 180));
		}
	}

	/** {@inheritDoc}
	 * @see javax.swing.AbstractButton#init(java.lang.String, javax.swing.Icon)
	 */
	@Override
	protected void init(String text, Icon icon) {
		super.init(text, icon);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1.0));
		setSize(getPreferredSize());
	}

	/**
	 * @return the size
	 */
	public ButtonStyle getStyle() {
		return style;
	}

	/**
	 * @param size the size to set
	 */
	public void setStyle(ButtonStyle style) {
		this.style = style;
		switch (style) {
		case SMALL:
			setFont(UiConstants.buttonSmallFont);
			setMargin(new Insets(0,0,0,0));
			break;
		
		case STANDARD:
			setFont(UiConstants.buttonStandardFont);
			break;
		
		case BIG:
			setFont(UiConstants.buttonBigFont);
			setBorder(new EmptyBorder(8,12,8,12));
			break;
		}
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
