package org.msh.xview.components;

import java.util.List;


/**
 * A field representation in the form
 * 
 * @author Ricardo Memoria
 *
 */
public class XField extends XView {

	public enum CharCase {
		NORMAL, UPPER, LOWER;
	}
	
	/**
	 * Contains information about a field reference 
	 */
	private FieldReference field;
	
	/**
	 * Label to be displayed beside the component 
	 */
	private String label;
	
	/**
	 * Hint message to complement the label
	 */
	private String hint;
	
	/**
	 * Expression that test if the field is required or not
	 */
	private String required;
	
	/**
	 * Maximum number of characters allowed
	 */
	private Integer maxChars;
	
	/**
	 * Width of the field, represented in approximated number of visible chars
	 */
	private Integer width;
	
	/**
	 * Input mask of the field, when editing
	 */
	private String inputMask;
	
	/**
	 * Char case for input text fields
	 */
	private CharCase charCase = CharCase.NORMAL;
	
	/**
	 * A handler is a specific identifier that includes more information to the UI control to be generated
	 */
	private String handler;

	/**
	 * Variable containing the list of options to feed the field
	 */
	private String options;

	/**
	 * The property that will be used as a label in the objects that compound the options
	 */
	private String optionLabel;
	
	private List<XValidationRule> validationRules;


	/**
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}

	/**
	 * @param hint the hint to set
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}


	/**
	 * @return the requiredExpression
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * @param requiredExpression the requiredExpression to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * @return the maxChars
	 */
	public Integer getMaxChars() {
		return maxChars;
	}

	/**
	 * @param maxChars the maxChars to set
	 */
	public void setMaxChars(Integer maxChars) {
		this.maxChars = maxChars;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * @return the inputMask
	 */
	public String getInputMask() {
		return inputMask;
	}

	/**
	 * @param inputMask the inputMask to set
	 */
	public void setInputMask(String inputMask) {
		this.inputMask = inputMask;
	}

	/**
	 * @return the required
	 */
/*	public Boolean getRequired(FormContext context) {
		if (required == null)
			required = compileExpression(requiredExpression);
		return (Boolean)required.getValue();
	}
*/
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
	 * @return the field
	 */
	public FieldReference getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(FieldReference field) {
		this.field = field;
	}

	/**
	 * @return the charCase
	 */
	public CharCase getCharCase() {
		return charCase;
	}

	/**
	 * @param charCase the charCase to set
	 */
	public void setCharCase(CharCase charCase) {
		this.charCase = charCase;
	}

	/**
	 * @return the required
	 */
/*	public boolean isRequired(VariableResolver resolver) {
		if (required == null) 
			required = compileExpression(requiredExpression, false);

		required.setVariableResolver(resolver);
		Boolean res = (Boolean)required.getValue();
		return res != null? res : false;
	}
*/

	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XField " + getId() + " [field=" + field + "]";
	}

	/**
	 * @return the validationRules
	 */
	public List<XValidationRule> getValidationRules() {
		return validationRules;
	}

	/**
	 * @param validationRules the validationRules to set
	 */
	public void setValidationRules(List<XValidationRule> validationRules) {
		this.validationRules = validationRules;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/** {@inheritDoc}
	 * @see org.msh.xview.components.XView#isDependentOf(java.lang.String)
	 */
	@Override
	public boolean isDependentOf(String fieldname) {
		return super.isDependentOf(fieldname) || ((field != null) && (field.getFieldName().equals(fieldname)));
	}

	/**
	 * @return the optionLabel
	 */
	public String getOptionLabel() {
		return optionLabel;
	}

	/**
	 * @param optionLabel the optionLabel to set
	 */
	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}
}
