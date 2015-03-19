package org.msh.xview.components;


/**
 * Store information about a section of a form. A section is an
 * area inside the form that contains other components.
 * <p/>
 * If the property <code>emptyBorders</code> is true (default value), 
 * the system will draw a rectangular border around the section
 * area, otherwise no border will be displayed.
 * <p/> 
 * If a <code>title</code> is defined, the title will be displayed
 * at the top of the section area.
 * <p/>
 * The section may also be used to display or hide a group of components
 * through its <code>visible</code> property. The same approach may
 * be done with the <code>readOnly</code> property.
 *     
 * @author Ricardo Memoria
 *
 */
public class XSection extends XContainer {


	/**
	 * Title of the section (only available if showBorder = true)
	 */
	private String title;

	/**
	 * Number of columns of the form, where components
	 * will be arranged into
	 */
	private int columnCount = 1;
	
	/**
	 * Indicate if the border will be displayed
	 */
	private boolean showBorder = true;



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the showBorder
	 */
	public boolean isShowBorder() {
		return showBorder;
	}

	/**
	 * @param showBorder the showBorder to set
	 */
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

}
