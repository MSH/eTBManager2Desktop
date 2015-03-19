/**
 * 
 */
package org.msh.etbm.desktop;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * @author Ricardo Memoria
 *
 */
public class ELTest2 {

	private TBCase tbcase = new TBCase(100, "Banana");
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {
		ELTest2 el = new ELTest2();
		el.test();
	}
	
	/**
	 * Run the expression
	 */
	public void test() {
		tbcase.setName("Ricardo");
		tbcase.setNumber(109);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("tbcase", tbcase);

		ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl();
		SimpleContext context = new MyContext(tbcase);
//		context.setVariable("model", factory.createValueExpression(this, ELTest2.class));
//		context.setVariable("tbcase", factory.createValueExpression(context, "${model.getVariable()}", TBCase.class));
		ValueExpression e = factory.createValueExpression(context, "Hello ${tbcase.name}! = ${tbcase.number}", String.class);
		System.out.println(e.getValue(context));
		
		tbcase = new TBCase(400, "WORLD");
		context.setVariable("tbcase", factory.createValueExpression(tbcase, TBCase.class));
//		variables.put("tbcase", tbcase);
		System.out.println(e.getValue(context));
	}

	
	public Object getVariable() {
//		if ("tbcase".equals(name))
			 return tbcase;
//		else return null;
	}
	
	
	public class TBCase {
		private int number;
		private String name;
		public TBCase(int number, String name) {
			super();
			this.number = number;
			this.name = name;
		}
		/**
		 * @return the number
		 */
		public int getNumber() {
			return number;
		}
		/**
		 * @param number the number to set
		 */
		public void setNumber(int number) {
			this.number = number;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
	}
	
	
	
	
	/**
	 * @author Ricardo Memoria
	 *
	 */
	public class MyContext extends SimpleContext {

		private ExpressionFactory factory = new ExpressionFactoryImpl();

		private Object variable;
		private MyVariableMapper variableMapper;
		
		public MyContext(Object variable) {
			super();
			this.variable = variable;
		}
		
		public ExpressionFactory getFactory() {
			return factory;
		}

		/** {@inheritDoc}
		 */
		@Override
		public VariableMapper getVariableMapper() {
			if (variableMapper == null) {
				variableMapper = new MyVariableMapper(this, variable, factory);
			}
			return variableMapper;
		}

		/** {@inheritDoc}
		 */
		@Override
		public ValueExpression setVariable(String name,
				ValueExpression expression) {
			return getVariableMapper().setVariable(name, expression);
		} 
	}
	
	
	
	
	/**
	 * @author Ricardo Memoria
	 *
	 */
	public class MyVariableMapper extends VariableMapper {
		private Object tbcase;
		private ExpressionFactory factory;
		private Map<String, ValueExpression> variables = new HashMap<String, ValueExpression>();
		private Map<String, Object> values = new HashMap<String, Object>();
		private MyContext context;
		
		public MyVariableMapper(MyContext context, Object tbcase, ExpressionFactory factory) {
			super();
			this.tbcase = tbcase;
			this.factory = factory;
			this.context = context;
		}

		/** {@inheritDoc}
		 */
		@Override
		public ValueExpression resolveVariable(String variable) {
			ValueExpression val = variables.get(variable);
			if (("tbcase".equals(variable)) && (val == null)) {
				values.put("tbcase", tbcase);
				val = factory.createValueExpression(context, "${variables['tbcase']}", tbcase.getClass());
				setVariable("tbcase", val);
				return val;
			}
			
			if (("variables".equals(variable)) && (val == null)) {
				val = factory.createValueExpression(values, HashMap.class);
				variables.put("variables", val);
			}
			return val;
		}

		
		/** {@inheritDoc}
		 */
		@Override
		public ValueExpression setVariable(String variable,
				ValueExpression expression) {
			
			if (!expression.isLiteralText()) {
				values.put(variable, expression.getValue(context));
			}
			return variables.put(variable, expression);
		}
		
	}
}
