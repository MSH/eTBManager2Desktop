package org.msh.xview.components;


/**
 * Validation rule inside a field
 * 
 * @author Ricardo Memoria
 *
 */
public class XValidationRule {

	/**
	 * Expression of the validation rule. Must return true to be validated
	 */
	private String rule;
	
	/**
	 * Condition to enable this validation. If no expression is present, it is true by default
	 */
	private String enabledIf;

	/**
	 * Message to be displayed if the validation fails
	 */
	private String message;

	// the view this validation rule is assigned to
	private XView view;
	
	public XValidationRule(XView view) {
		super();
		this.view = view;
	}
	
	
/*	*//**
	 * Return true if the rule is valid (validation succeeded)
	 * @param form
	 * @return
	 *//*
	public boolean isValid(FormDataModel dataModel) {
		if (rule == null) {
			if (ruleExpression == null)
				return true;
			rule = view.compileExpression(ruleExpression, false);
		}

		rule.setVariableResolver(dataModel);
		return (Boolean)rule.getValue();
	}
*/

	/**
	 * Return true if the validation rule is enabled, i.e, must be tested
	 * @param form
	 * @return
	 */
/*	public boolean isEnabled(FormDataModel dataModel) {
		if (enabledIf == null) {
			if (enabledIfExpression == null)
				return true;
			enabledIf = view.compileExpression(enabledIfExpression, false);
		}
		
		enabledIf.setVariableResolver(dataModel);
		return (Boolean)enabledIf.getValue();
	}
*/

	/**
	 * @return the ruleExpression
	 */
	public String getRule() {
		return rule;
	}

	/**
	 * @param ruleExpression the ruleExpression to set
	 */
	public void setRule(String ruleExpression) {
		this.rule = ruleExpression;
	}

	/**
	 * @return the enabledIfExpression
	 */
	public String getEnabledIf() {
		return enabledIf;
	}

	/**
	 * @param enabledIfExpression the enabledIfExpression to set
	 */
	public void setEnabledIf(String enabledIfExpression) {
		this.enabledIf = enabledIfExpression;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @return the view
	 */
	public XView getView() {
		return view;
	}

}
