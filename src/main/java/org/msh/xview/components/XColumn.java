/**
 * 
 */
package org.msh.xview.components;

/**
 * Store information about a table's column
 * 
 * @author Ricardo Memoria
 *
 */
public class XColumn extends XContainer {

	/**
	 * The title of the column
	 */
	private String title;


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
}
