package org.msh.etbm.desktop.common;

/**
 * Item to be used for holding values and display information
 * 
 * @author Ricardo Memoria
 *
 */
public class SelectItem {

	private Object value;
	private String displayString;
	
	public SelectItem(Object value, String displayString) {
		super();
		this.value = value;
		this.displayString = displayString;
	}
	
	public SelectItem() {
		super();
	}
	

	@Override
	public String toString() {
		return displayString;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the displayString
	 */
	public String getDisplayString() {
		return displayString;
	}

	/**
	 * @param displayString the displayString to set
	 */
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
}
