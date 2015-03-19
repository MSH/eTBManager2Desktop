/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.el.ValueExpression;

import org.msh.xview.swing.XViewUtils;

/**
 * Simple expression wrapper to check if the string is an expression or
 * a string to be translated (starting with @)
 * 
 * @author Ricardo Memoria
 *
 */
public class ExpressionWrapper<E> {

	private String expression;
	private ViewUI view;
	private ValueExpression valueExp;
	
	/**
	 * Default constructor
	 * @param view
	 * @param expression
	 * @param expectedClassResult
	 */
	public ExpressionWrapper(ViewUI view, String expression, Class expectedClassResult) {
		super();
		this.view = view;
		this.expression = expression;
		
		if ((expression != null) && (!expression.startsWith("@"))) {
			valueExp = view.getDataModel().createValueExpression(expression, expectedClassResult);
		}
	}
	
	/**
	 * Return the value
	 * @return Object instance
	 */
	public E getValue() {
		if (valueExp != null) {
			return (E)valueExp.getValue(view.getELContext());
		}
		
		if (expression == null) {
			return null;
		}
		
		return (E)XViewUtils.translateMessage(expression);
	}
}
