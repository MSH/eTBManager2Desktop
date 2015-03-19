/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.entities.enums.TreatmentDayOption;
import org.msh.etbm.services.cases.treatment.TreatmentFollowupData;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;

/**
 * @author Ricardo Memoria
 *
 */
public class TreatTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 53429696444382680L;

	final static public AwesomeIcon iconSelf = new AwesomeIcon(AwesomeIcon.ICON_REMOVE_SIGN, new Color(51, 102, 153), 13);
	final static public AwesomeIcon iconDots = new AwesomeIcon(AwesomeIcon.ICON_REMOVE_SIGN, new Color(134, 203, 98), 13);
	final static public AwesomeIcon iconDisabled = new AwesomeIcon(AwesomeIcon.ICON_SIGN_BLANK, new Color(230,230,230), 16);
	
	private TbCase tbcase;
	private RegimenPhase phase;
	private TreatmentFollowupData data;

	/**
	 * Default constructor
	 */
	public TreatTableCellRenderer(TbCase tbcase, RegimenPhase phase, TreatmentFollowupData data) {
		this.tbcase = tbcase;
		this.phase = phase;
		this.data = data;
	}

	public static AwesomeIcon getIcon(TreatmentDayOption value) {
		switch (value) {
		case DOTS: return iconDots;
		case NOT_TAKEN: return iconDisabled;
		case SELF_ADMIN: return iconSelf;
		default:
			return null;
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Date dt = getDate(row, column);
		if (dt == null) {
			iconDisabled.setColor(new Color(230,230,230));
			setIcon(iconDisabled);
		}
		else {
			TreatmentDayOption status = data.getTreatmentDay(dt);
			if (status != null) {
				switch (status) {
				case DOTS:
					setIcon(iconDots);
					break;
				case SELF_ADMIN:
					setIcon(iconSelf);
					break;
				default:
					setIcon(null);
					break;
				}
			}
			else setIcon(null);
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}
	
	/**
	 * @param row
	 * @param column
	 * @return
	 */
	protected Date getDate(int row, int column) {
		Period p;
		if (phase == RegimenPhase.INTENSIVE)
			 p = tbcase.getIntensivePhasePeriod();
		else p = tbcase.getContinuousPhasePeriod();

		Date dt = p.getIniDate();
		if (row > 0)
			dt = DateUtils.incMonths(p.getIniDate(), row);

		int month = DateUtils.monthOf(dt);
		int year = DateUtils.yearOf(dt);
		int day = column;
		int daysInMonth = DateUtils.daysInAMonth(year, month);
		if (day > daysInMonth)
			return null;
		
		dt = DateUtils.newDate(year, month, day);
		
		if (p.isDateInside(dt))
			 return dt;
		else return null;
	}
}
