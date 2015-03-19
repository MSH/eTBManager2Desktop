package org.msh.etbm.desktop.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

/**
 * Button that executes an action inside a database transaction
 * 
 * @author Ricardo Memoria
 *
 */
public class JTransactionalButton extends JButton {
    private static final long serialVersionUID = -8460954939804942608L;

    private Object data;
    
    /**
     * Default constructor
     */
    public JTransactionalButton() {
	super();
    }

	/**
	 * {@inheritDoc}
	 */
	public JTransactionalButton(Action a) {
		super(a);
	}

	/**
	 * {@inheritDoc}
	 */
	public JTransactionalButton(Icon icon) {
		super(icon);
	}

	/**
	 * {@inheritDoc}
	 */
	public JTransactionalButton(String text, Icon icon) {
		super(text, icon);
	}

	/**
	 * {@inheritDoc}
	 */
	public JTransactionalButton(String text) {
		super(text);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setDefaultCapable(boolean defaultCapable) {
		super.setDefaultCapable(defaultCapable);
		if (defaultCapable) {
			setForeground(new Color(255, 255, 255));
			setBackground(new Color(70, 130, 180));
			setFont(getFont().deriveFont(Font.BOLD));
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void init(String text, Icon icon) {
		super.init(text, icon);
		setSize(108, 29);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1.0));
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void fireActionPerformed(ActionEvent event) {
		EntityManagerUtils.doInTransaction(new ActionCallback<ActionEvent>(event) {
			@Override
			public void execute(ActionEvent evt) {
				fireActionPerformedInTransaction(evt);
			}
		});
	}


	/**
	 * Fire the action of the super class
	 * @param event instance of the {@link ActionEvent} rose
	 */
	protected void fireActionPerformedInTransaction(ActionEvent event) {
		super.fireActionPerformed(event);
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
