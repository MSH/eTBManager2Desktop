package org.msh.etbm.desktop.common;

public class TableCellID {

	private Object value;
	private Object displayText;
	
	public TableCellID(Object value, Object displayText) {
		this.value = value;
		this.displayText = displayText;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return displayText != null? displayText.toString(): "";
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the displayText
	 */
	public Object getDisplayText() {
		return displayText;
	}
}
