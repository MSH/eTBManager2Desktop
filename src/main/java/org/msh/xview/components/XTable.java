package org.msh.xview.components;


/**
 * Representation of a list of values in a table format
 * 
 * @author Ricardo Memoria
 *
 */
public class XTable extends XContainer {

	public enum RowSelection { NONE, SINGLE, MULTIPLE };
	
	/**
	 * The list of values that this list is displaying
	 */
	private String values;

	/**
	 * The local variable declared for the list
	 */
	private String var;

	/**
	 * Indicate if buttons will be available in the table to create or delete rows
	 */
	private boolean rowsEditors;
	
	/**
	 * Indicate the type of row selection
	 */
	private RowSelection rowSelection;

	/**
	 * Expression that is evaluated for each row in order to initialize the row selection.
	 * Just used if selectableRows is true
	 */
	private String rowCheckBoxField;
	

	/**
	 * Check if row is initialized as selected
	 * @param resolver is the instance of the {@link VariableResolver} to get variable values
	 * @return true if the row is selected
	 */
/*	public Boolean isRowSelected(VariableResolver resolver) {
		if (iniSelectionCondition == null)
			iniSelectionCondition = compileExpression(iniSelectionConditionExpression, false);
		iniSelectionCondition.setVariableResolver(resolver);
		return (Boolean)iniSelectionCondition.getValue();
	}
*/	
	/**
	 * @return the value
	 */
	public String getValues() {
		return values;
	}

	/**
	 * @param value the value to set
	 */
	public void setValues(String value) {
		this.values = value;
	}

	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @return the rowsEditors
	 */
	public boolean isRowsEditors() {
		return rowsEditors;
	}

	/**
	 * @param rowsEditors the rowsEditors to set
	 */
	public void setRowsEditors(boolean rowsEditors) {
		this.rowsEditors = rowsEditors;
	}

	/**
	 * @return the rowSelection
	 */
	public RowSelection getRowSelection() {
		return rowSelection;
	}

	/**
	 * @param rowSelection the rowSelection to set
	 */
	public void setRowSelection(RowSelection rowSelection) {
		this.rowSelection = rowSelection;
	}

	/**
	 * @return the rowCheckBoxField
	 */
	public String getRowCheckBoxField() {
		return rowCheckBoxField;
	}

	/**
	 * @param rowCheckBoxField the rowCheckBoxField to set
	 */
	public void setRowCheckBoxField(String rowCheckBoxField) {
		this.rowCheckBoxField = rowCheckBoxField;
	}
}
