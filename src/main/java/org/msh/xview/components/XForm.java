package org.msh.xview.components;

import org.msh.xview.impl.ViewVariableValidator;


/**
 * Store information about a form structure, including
 * its child form elements (instances of {@link XView} class).
 * 
 * @author Ricardo Memoria
 *
 */
public class XForm extends XContainer {

	/**
	 * Number of columns of the form, where components
	 * will be arranged into
	 */
	private int columnCount = 1;
	private String validator;
	private String action;


	/** {@inheritDoc}
	 * @see org.msh.xview.components.XView#getVariableValidator()
	 */
	@Override
	public ViewVariableValidator getVariableValidator() {
		return new ViewVariableValidator();
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
	 * @return the validatorExpression
	 */
	public String getValidator() {
		return validator;
	}


	/**
	 * @param validatorExpression the validatorExpression to set
	 */
	public void setValidator(String validatorExpression) {
		this.validator = validatorExpression;
	}


	/**
	 * @return the actionExpression
	 */
	public String getAction() {
		return action;
	}


	/**
	 * @param actionExpression the actionExpression to set
	 */
	public void setAction(String actionExpression) {
		this.action = actionExpression;
	}
}
