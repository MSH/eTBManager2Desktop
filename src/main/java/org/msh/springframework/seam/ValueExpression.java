package org.msh.springframework.seam;

import org.springframework.expression.Expression;

public class ValueExpression<E> {

	private String expressionString;
	private Expression expression;
	
	public ValueExpression(String expressionString) { 
		this.expressionString = expressionString;
	}
	
	public E getValue() {
		if (!expressionString.contains("#{"))
			return (E)expressionString;

		Expressions expressions = Expressions.instance();
		if (expression == null)
			expression = expressions.parseExpression(expressionString);
		
		return (E)expression.getValue(expressions.getEvaluationContext());
	}
	
	
	public String getExpressionString() {
		return expressionString;
	}
}
