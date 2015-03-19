/**
 * 
 */
package org.msh.xview.components;

/**
 * A generic component to store a text to be displayed
 * 
 * @author Ricardo Memoria
 *
 */
public class XText extends XView {

	/**
	 * The text to be displayed
	 */
	private String text;
	
	/**
	 * Optionally, an icon to be displayed beside the text
	 */
	private String icon;


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
