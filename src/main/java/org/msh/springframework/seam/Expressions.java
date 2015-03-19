package org.msh.springframework.seam;

import org.msh.etbm.desktop.app.App;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Expressions {

	private SpelExpressionParser parser = new SpelExpressionParser();
	private StandardEvaluationContext context;
	
	public ValueExpression createValueExpression(String expression) {
		return new ValueExpression( expression );
	}

	public Expression parseExpression(String expression) {
		if (expression.startsWith("#{")) {
			expression = "@" + expression.substring(2, expression.length() - 1);
			expression = expression.replace(".", "?.");
		}
		return parser.parseExpression(expression);
	}

	public EvaluationContext getEvaluationContext() {
		if (context == null)
			createEvaluationContext();
		return context;
	}
	
	protected void createEvaluationContext() {
		context = new StandardEvaluationContext();
		context.setBeanResolver(new AppBeanResolver());
	}
	
	public static Expressions instance() {
		return App.getComponent(Expressions.class);
	}
	
	
	public class AppBeanResolver implements BeanResolver {

		public Object resolve(EvaluationContext context, String beanName)
				throws AccessException {
			return App.getComponent(beanName);
		}
		
	}
	
}
