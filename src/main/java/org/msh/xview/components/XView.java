package org.msh.xview.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msh.xview.impl.VariableScanner;
import org.msh.xview.impl.ViewVariableValidator;

/**
 * This is the root class of all form elements (including the own form)
 * that stores information about the structure of the form.
 * <p/>
 * This class contains common properties of all form elements, and
 * doesn't implement any specific platform operation, just
 * store a form element data in order to be handled by the
 * proper platform specific library
 * 
 * @author Ricardo Memoria
 *
 */
public class XView {

	/**
	 * Points to the parent view. Null if it's the form
	 */
	private XView parent;

	/**
	 * Id of the view. To be used inside expressions
	 */
	private String id;

	/**
	 * Expression to evaluate if view is read-only
	 */
	private String readOnly;
	
	/**
	 * Expression to evaluate if view is visible
	 */
	private String visible;

	/**
	 * Number of columns that this view will span in the parent view
	 */
	private int colSpan = 1;

	
	/**
	 * Indicate if the view must start in a new line of the form
	 */
	private boolean forceNewRow;
	
	/**
	 * List of parameters declared inside this view
	 */
	private Map<String, String> parameters;

	/**
	 * List of the fields where this view depends on.
	 * The view must be notified in case of changes in the field
	 */
	private List<String> fieldDependencies;
	
	/**
	 * List of validation rules that will be applied to this view
	 */
	private List<XValidationRule> rules;
	
	/**
	 * Default constructor
	 */
	public XView() {
		super();
	}

	/**
	 * Return the instance of the {@link VariableValidator} responsible for 
	 * validating the name of the variables in expressions
	 * 
	 * @return instance of {@link VariableValidator}
	 */
	public ViewVariableValidator getVariableValidator() {
		return parent != null? parent.getVariableValidator(): null;
	}
	
	/**
	 * Add a dependency to this view. This view will be notified
	 * when the field represented by <code>fieldName</code> 
	 * changes its value in the data model
	 *
	 * @param dependentView
	 */
	protected void addDependency(String fieldName) {
		if (fieldDependencies == null) {
			fieldDependencies = new ArrayList<String>();
			fieldDependencies.add(fieldName);
		}
		else 
			if (!fieldDependencies.contains(fieldName))
				fieldDependencies.add(fieldName);
	}
	
	
	/**
	 * Return true if view is dependent of the value of the given field name
	 * @param fieldname
	 * @return
	 */
	public boolean isDependentOf(String fieldname) {
		return (fieldDependencies != null) && (fieldDependencies.contains(fieldname));
	}


	/**
	 * Refresh the list of dependencies
	 */
	public void refreshDependencies() {
		addDependenciesFromExpression(visible);
		addDependenciesFromExpression(readOnly);
	}
	
	/**
	 * Add dependencies from the expression
	 * @param exp
	 */
	protected void addDependenciesFromExpression(String exp) {
		if (exp == null)
			return;
		List<String> vars = VariableScanner.scan(exp);
		for (String var: vars) {
			addDependency(var);
		}
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public XView getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(XView parent) {
		this.parent = parent;
	}


	/**
	 * @return the readOnlyExpression
	 */
	public String getReadOnly() {
		return readOnly;
	}


	/**
	 * @param readOnlyExpression the readOnlyExpression to set
	 */
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}


	/**
	 * @return the visibleExpression
	 */
	public String getVisible() {
		return visible;
	}


	/**
	 * @param visibleExpression the visibleExpression to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	

	/**
	 * Return a parameter defined in the form definition
	 * @param paramname
	 * @return
	 */
	public String getParameter(String paramname) {
		return parameters == null? null: parameters.get(paramname);
	}
	
	/**
	 * Include a parameter defined in the form definition
	 * @param paramname
	 * @param value
	 */
	public void setParameter(String paramname, String value) {
		if (parameters == null)
			parameters = new HashMap<String, String>();
		parameters.put(paramname, value);
	}
	
	
	/**
	 * Compile expression of the view including in the view the dependencies that are found in the expression 
	 * @param expression is the expression to be compiled
	 * @param addDependencies if true, add the variable name to the list of dependencies of the view
	 * @return
	 */
/*	protected ExpressionOperation compileExpression(String expression, boolean addDependencies) {
		ViewVariableValidator viewVarVal = getVariableValidator();
		ExpressionOperation oper = ExpressionCompiler.compile(expression, viewVarVal);

		if (addDependencies) {
			if (viewVarVal.getDeclaredVariables() != null)
				for (String varname: viewVarVal.getDeclaredVariables())
					addDependency(varname);
		}

		return oper;
	}
*/
	
	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Id=" + id + "]";
	}


	/**
	 * @return the colSpan
	 */
	public int getColSpan() {
		return colSpan;
	}


	/**
	 * @param colSpan the colSpan to set
	 */
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}


	/**
	 * @return the forceNewRow
	 */
	public boolean isForceNewRow() {
		return forceNewRow;
	}


	/**
	 * @param forceNewRow the forceNewRow to set
	 */
	public void setForceNewRow(boolean forceNewRow) {
		this.forceNewRow = forceNewRow;
	}

	/**
	 * @return the rules
	 */
	public List<XValidationRule> getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(List<XValidationRule> rules) {
		this.rules = rules;
	}
}
