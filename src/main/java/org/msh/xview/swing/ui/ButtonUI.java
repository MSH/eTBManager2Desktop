/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.xview.components.XButton;

/**
 * @author Ricardo Memoria
 *
 */
public class ButtonUI extends ComponentUI<XButton> {

	private ExpressionWrapper<String> expLabel;
	private ExpressionWrapper<String> expIcon;
	private ExpressionWrapper<String> expAction;
	private AwesomeIcon icon;


	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JButton btn = new JButton(getLabel());
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clickHandler();
			}
		});
		return btn;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		if (width < getComponent().getWidth()) {
			getComponent().setSize(width, getComponent().getHeight());
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void doComponentUpdate() {
		JButton btn = (JButton)getComponent();

		// update button state
		btn.setText(getLabel());
		Icon icon = getIcon();
		if (icon != null) {
			btn.setIcon(icon);
		}
		btn.setSize(getComponent().getPreferredSize());
	}
	
	/**
	 * Called when the button is clicked
	 */
	protected void clickHandler() {
		if (expAction == null) {
			expAction = new ExpressionWrapper<String>(this, getView().getAction(), String.class);
		}
		String result = expAction.getValue();
		System.out.println(result);
	}
	
	
	/**
	 * Return the label of the button
	 * @return
	 */
	public String getLabel() {
		if (expLabel == null) {
			expLabel = new ExpressionWrapper<String>(this, getView().getLabel(), String.class);
		}
		
		return expLabel.getValue();
	}

	/**
	 * Return the icon string
	 * @return
	 */
	protected String getIconString() {
		if (expIcon == null) {
			expIcon = new ExpressionWrapper<String>(this, getView().getIcon(), String.class);
		}
		return expIcon.getValue();
	}
	
	/**
	 * Return the icon of the button
	 * @return instance of {@link Icon} class, or null if no icon is set
	 */
	public AwesomeIcon getIcon() {
		if (icon == null) {
			createIcon();
		}
		return icon;
	}
	
	
	/**
	 * Create the icon to be displayed
	 */
	protected void createIcon() {
		// get the string value of the icon
		String iconStr = getIconString();
		if (iconStr == null) {
			return;
		}
		
		// using reflection, get the icon ID and create the icon
		try {
			Field field = AwesomeIcon.class.getDeclaredField(iconStr);
			if (field.getType() == int.class) {
				int iconId = field.getInt(null);
				icon = new AwesomeIcon(iconId, getComponent());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
