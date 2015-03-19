/**
 * 
 */
package org.msh.timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Represents a row of the time line. The row is displayed in the left side 
 * of the time line and contains a label for the bars in that row 
 * 
 * @author Ricardo Memoria
 *
 */
public class TimelineRow {

	private String label;
	private ImageIcon icon;
	private List<TimelinePeriod> periods = new ArrayList<TimelinePeriod>();
	private int y;
	private boolean drawHorizontalLine = true;

	/**
	 * Default constructor
	 */
	public TimelineRow() {
		super();
	}
	
	/**
	 * Constructor initializing label and icon
	 * @param label
	 * @param icon
	 */
	public TimelineRow(String label, ImageIcon icon) {
		super();
		this.label = label;
		this.icon = icon;
	}

	/**
	 * Add a period to the time line row
	 * @param iniDate is the initial date of the period
	 * @param endDate is the final date of the period
	 * @param label is the label to be displayed inside the period bar
	 * @return instance of the {@link TimelinePeriod}
	 */
	public TimelinePeriod addPeriod(Date iniDate, Date endDate, String label) {
		TimelinePeriod bar = new TimelinePeriod(this);
		bar.setIniDate(iniDate);
		bar.setEndDate(endDate);
		bar.setLabel(label);
		periods.add(bar);
		return bar;
	}
	
	/**
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the periods
	 */
	public List<TimelinePeriod> getPeriods() {
		return periods;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	protected void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the drawHorizontalLine
	 */
	public boolean isDrawHorizontalLine() {
		return drawHorizontalLine;
	}

	/**
	 * @param drawHorizontalLine the drawHorizontalLine to set
	 */
	public void setDrawHorizontalLine(boolean drawHorizontalLine) {
		this.drawHorizontalLine = drawHorizontalLine;
	}
}
