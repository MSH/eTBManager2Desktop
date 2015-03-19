/**
 * 
 */
package org.msh.etbm.desktop.common;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.SynchronizationData;

/**
 * Render a cell of a table containing an instance of {@link SynchronizationData} class
 * 
 * @author Ricardo Memoria
 *
 */
public class SyncCellRenderer extends AppTableCellRenderer{
	private static final long serialVersionUID = 4757651894097601185L;

	/** {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (value instanceof SynchronizationData) {
			SynchronizationData data = (SynchronizationData)value;
			AwesomeIcon icon;
			if (data.isChanged()) {
				value = "Unsync";
				icon = new AwesomeIcon(AwesomeIcon.ICON_CLOUD_UPLOAD, new Color(0xe48b00), 16);
			}
			else {
				value = "Sync";
				icon = new AwesomeIcon(AwesomeIcon.ICON_OK_SIGN, new Color(0x17853e), 16);
			}
			setIcon(icon);
		}
		
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		return comp;
	}
}
