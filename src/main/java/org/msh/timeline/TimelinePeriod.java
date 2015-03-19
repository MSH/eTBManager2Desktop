/**
 * 
 */
package org.msh.timeline;

import java.awt.Rectangle;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * Represents a bar to be displayed in the time line
 * @author Ricardo Memoria
 *
 */
public class TimelinePeriod {

	private TimelineRow row;
	private Date iniDate;
	private Date endDate;
	private PeriodStyle style;
	private String label;
	private ImageIcon icon;
	private boolean clickable = true;
	private Object data;
	
	private Rectangle rectangle;
	
	
	public TimelinePeriod(TimelineRow row) {
		super();
		this.row = row;
	}
	/**
	 * @return the iniDate
	 */
	public Date getIniDate() {
		return iniDate;
	}
	/**
	 * @param iniDate the iniDate to set
	 */
	public void setIniDate(Date iniDate) {
		this.iniDate = iniDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the style
	 */
	public PeriodStyle getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(PeriodStyle style) {
		this.style = style;
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
	 * @return the row
	 */
	public TimelineRow getRow() {
		return row;
	}
	/**
	 * @return the rectangle
	 */
	public Rectangle getRectangle() {
		return rectangle;
	}
	/**
	 * @param rectangle the rectangle to set
	 */
	protected void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	/**
	 * @return the clickable
	 */
	public boolean isClickable() {
		return clickable;
	}
	/**
	 * @param clickable the clickable to set
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
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
