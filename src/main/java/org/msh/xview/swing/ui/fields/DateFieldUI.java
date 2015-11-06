/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;

import com.toedter.calendar.JTextFieldDateEditor;
import org.msh.etbm.desktop.app.Messages;

import com.toedter.calendar.JDateChooser;

/**
 * @author Ricardo Memoria
 *
 */
public class DateFieldUI extends FieldComponentUI {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JDateChooser edt = new JDateChooser(Messages.getString("locale.datePattern"), Messages.getString("locale.dateMask"), '_');
		edt.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!"date".equals(evt.getPropertyName()))
					return;
				notifyValueChange();
			}
		});

        edt.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("value".equals(evt.getPropertyName())) {
                    JTextFieldDateEditor edt = (JTextFieldDateEditor)evt.getSource();
                    notifyValueChange();
                }
            }
        });
		edt.setSize(140, edt.getPreferredSize().height);
		edt.setPreferredSize(new Dimension(140, edt.getPreferredSize().height));
		return edt;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		JDateChooser edt = (JDateChooser)getComponent();
		Object value = getValue();
		edt.setDate((Date)value);
	}

	
	/**
	 * Called when the date is changed
	 */
	protected void notifyValueChange() {
		JDateChooser edt = (JDateChooser)getComponent();
        Date dt = edt.getDate();
		setValue(dt);
	}


	/** {@inheritDoc}
	 */
	@Override
	public String getDisplayText() {
		Date dt = (Date)getValue();
		if (dt == null)
			return "";
		
		String patt = Messages.getString("locale.outputDatePattern");
		SimpleDateFormat df = new SimpleDateFormat(patt);
		
		return df.format(dt);
	}

}
