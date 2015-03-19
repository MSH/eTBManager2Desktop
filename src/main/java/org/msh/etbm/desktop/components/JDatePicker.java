package org.msh.etbm.desktop.components;

import org.msh.etbm.desktop.app.Messages;

import com.toedter.calendar.JDateChooser;

/**
 * This class extends the standard {@link JDateChooser} component setting the
 * standard date mask of the current language selected
 * 
 * @author Ricardo Memoria
 *
 */
public class JDatePicker extends JDateChooser {
	private static final long serialVersionUID = 6917232512537770228L;

	/**
	 * Default constructor
	 */
	public JDatePicker() {
		super(Messages.getString("locale.datePattern"), Messages.getString("locale.dateMask"), '_');
	}
}
