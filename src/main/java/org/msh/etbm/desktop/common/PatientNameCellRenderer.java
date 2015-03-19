package org.msh.etbm.desktop.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.Gender;

/**
 * Cell renderer of a {@link JTable} to display the patient name and an icon on his left side representing
 * its gender. It expects an object of type {@link Patient} in the cell
 * @author Ricardo Memoria
 *
 */
public class PatientNameCellRenderer extends AppTableCellRenderer {
	private static final long serialVersionUID = -59360863156469211L;

	private final ImageIcon iconMale = new ImageIcon(getClass().getResource("/resources/images/male_16x16.png"));
	private final ImageIcon iconFemale = new ImageIcon(getClass().getResource("/resources/images/female_16x16.png"));
	private Font fontBold;

	/** {@inheritDoc}
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (fontBold == null) {
			Font fontRegular = renderer.getFont();
			fontBold = new Font(fontRegular.getName(), Font.BOLD, fontRegular.getSize());
		}

		if (value instanceof CaseState) {
			CaseState st = (CaseState)value;
			String s = Messages.getString( st.getKey() );
			if ((st == CaseState.ONTREATMENT) || (st == CaseState.TRANSFERRING)) {
				renderer.setFont(fontBold);
				renderer.setForeground(new Color(0xff6600));
			}
			setText(s);
		}
		else
		if (value instanceof Patient) {
			Patient p = (Patient)value;
			if (p.getGender() == Gender.MALE)
				 setIcon(iconMale);
			else setIcon(iconFemale);
			setText(p.getFullName());
		}
		else {
			setIcon(null);
			setText(value != null? value.toString(): "");
		}

		return renderer;
	}
	
}