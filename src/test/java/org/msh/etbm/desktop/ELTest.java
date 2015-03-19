/**
 * 
 */
package org.msh.etbm.desktop;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.msh.etbm.desktop.ELTest2.MyContext;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

/**
 * @author Ricardo Memoria
 *
 */
public class ELTest {
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {
		ELTest el = new ELTest();
		el.test();
	}
	

	public void test() {
		ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl();
		SimpleContext context = new MyContext();
		ValueExpression e = factory.createValueExpression(context, "Hello ${tbcase.name}! = ${tbcase.number}", String.class);
		System.out.println(e.getValue(context));
		
	}

	
	public class MyVariableMapper extends VariableMapper {
		Map<String, ValueExpression> map = Collections.emptyMap();

		@Override
		public ValueExpression resolveVariable(String variable) {
			System.out.println("Resolving: " + variable);
			return map.get(variable);
		}

		@Override
		public ValueExpression setVariable(String variable, ValueExpression expression) {
			if (map.isEmpty()) {
				map = new HashMap<String, ValueExpression>();
			}
			return map.put(variable, expression);
		}
	}
	
	public class MyContext extends SimpleContext {
		
		private VariableMapper variables;
		
		@Override
		public VariableMapper getVariableMapper() {
			if (variables == null)
				variables = new MyVariableMapper();
			return variables;
		}
		/**
		 * Define a variable.
		 */
		@Override
		public ValueExpression setVariable(String name, ValueExpression expression) {
			return getVariableMapper().setVariable(name, expression);
		}
	}
	
}
