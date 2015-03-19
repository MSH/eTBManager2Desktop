/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.el.ValueExpression;

import org.msh.xview.FormDataModel;
import org.msh.xview.components.XValidationRule;
import org.msh.xview.swing.XViewUtils;

/**
 * Implementation of validation rules for fields
 * @author Ricardo Memoria
 *
 */
public class ValidationRuleUI {

	private FormDataModel dataModel;
	private XValidationRule rule;
	private ValueExpression expEnabled;
	private ValueExpression expValid;
	private ValueExpression expMessage;
	
	/**
	 * Default constructor
	 * @param dataModel
	 * @param rule
	 */
	public ValidationRuleUI(FormDataModel dataModel, XValidationRule rule) {
		super();
		this.dataModel = dataModel;
		this.rule = rule;
	}

	
	/**
	 * Return the message to be displayed to the user in case of failure on the rule
	 * @return String value
	 */
	public String getMessage() {
		if (expMessage == null) {
			String msg = rule.getMessage();
			if (msg == null) {
				return msg;
			}
			
			if (msg.startsWith("@")) {
				return XViewUtils.translateMessage(msg);
			}
			
			expMessage = dataModel.createValueExpression(msg, String.class);
		}
		return (String)expMessage.getValue(dataModel.getELContext());
	}
	
	/**
	 * Return true if the validation rule is enabled
	 * @return boolean value
	 */
	public boolean isEnabled() {
		if (expEnabled == null) {
			String exp = rule.getEnabledIf();
			if (exp == null)
				return true;
			
			expEnabled = dataModel.createValueExpression(exp, Boolean.class);
		}
		return (Boolean)expEnabled.getValue(dataModel.getELContext());
	}

	
	/**
	 * Return true if the validation rule is valid
	 * @return boolean value
	 */
	public boolean isValid() {
		if (expValid == null) {
			String exp = rule.getRule();
			if (exp == null) {
				return true;
			}
			
			expValid = dataModel.createValueExpression(exp, Boolean.class);
		}
		return (Boolean)expValid.getValue(dataModel.getELContext());
	}
	
	/**
	 * @return the dataModel
	 */
	public FormDataModel getDataModel() {
		return dataModel;
	}
	/**
	 * @return the rule
	 */
	public XValidationRule getRule() {
		return rule;
	}
}
