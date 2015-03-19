/**
 * 
 */
package org.msh.xview.components;

/**
 * Represents a button in the form. The icon is a representation
 * of the font-awesome constant, for example, ICON_OK.
 * <p/>
 * Action can be an EL representation of a method or an action string
 * (JSF style). If it's a string, the user must map the event in the 
 * form.
 * <p/>
 * A label is a string, an EL expression or a link to the message file
 * starting with an @
 *  
 * @author Ricardo Memoria
 *
 */
public class XButton extends XView {

	// the label of the button
	private String label;
	
	// the icon of the button (i.e., ICON_PLUS)
	private String icon;
	
	// the action to be executed (a method reference or a string)
	private String action;

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
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
}
