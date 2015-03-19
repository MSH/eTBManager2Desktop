package org.msh.etbm.desktop.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.UIManager;

import org.msh.etbm.desktop.app.UiConstants;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 * @author Ricardo Memoria
 *
 */
public class AppTableCellRenderer extends SubstanceDefaultTableCellRenderer {
	private static final long serialVersionUID = -2325382745378134668L;

	private Font fontBold;
	private Font fontRegular;

	/**
	 * Adjust the layout of the component to be displayed in the table, like background color, font, etc
	 * @param comp
	 * @param isSelected
	 * @param row
	 */
	protected void adjustComponentLayout(Component comp, boolean isSelected, int row) {
		fontRegular = comp.getFont();
		if (fontBold == null)
			fontBold = new Font(fontRegular.getName(), Font.BOLD, fontRegular.getSize());

		if (!isSelected) {
			Color bgcolor;
			if (row % 2 == 0)
				 bgcolor = new Color(255,255,255);
			else bgcolor = new Color(246,246,240);
			comp.setBackground(bgcolor);
		}
		
		comp.setFont(fontRegular);
		comp.setForeground(Color.BLACK);
	}


	/** {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		fontRegular = comp.getFont();
		if (fontBold == null)
			fontBold = new Font(fontRegular.getName(), Font.BOLD, fontRegular.getSize());

		comp.setFont(fontRegular);
		if (!isSelected) {
			Color bgcolor;
			if (row % 2 == 0)
				 bgcolor = new Color(255,255,255);
			else bgcolor = new Color(245,245,245);
//			else bgcolor = new Color(246,246,240);
			comp.setBackground(bgcolor);
			comp.setForeground(Color.BLACK);
		}
		else {
			Color bkcolor = UiConstants.selectedRow.darker();// UIManager.getDefaults().getColor("List.selectionBackground").darker();
			Color fgcolor = UIManager.getDefaults().getColor("List.selectionForeground");
			comp.setBackground(bkcolor);
			comp.setForeground(fgcolor);
		}
		
		return comp;
	}
	

}
