/**
 * 
 */
package org.msh.etbm.desktop;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import de.odysseus.el.util.SimpleContext;

/**
 * @author Ricardo Memoria
 *
 */
public class ELTest3 {

	private TBCase tbcase = new TBCase(100, "Banana");
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws Exception {
		ELTest3 el = new ELTest3();
		el.test();
	}
	
	/**
	 * Run the expression
	 * @throws NoSuchMethodException 
	 * @throws Exception 
	 */
	public void test() throws Exception {
		tbcase.setName("Ricardo");
		tbcase.setNumber(109);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("tbcase", tbcase);

//		Properties props = new Properties();
//		props.put("javax.el.methodInvocations", Boolean.TRUE);
//		ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl(props);
		ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		context.setFunction("", "toString", ELTest3.class.getMethod("toString", Object.class));
		context.setVariable("model", factory.createValueExpression(this, ELTest3.class));
		context.setVariable("tbcase", factory.createValueExpression(context, "${model.getVariable('tbcase')}", TBCase.class));
		ValueExpression e = factory.createValueExpression(context, "Hello ${tbcase.name}! = ${tbcase.number}", String.class);
		System.out.println(e.getValue(context));
		
		tbcase = new TBCase(400, "WORLD");
		context.setVariable("tbcase", factory.createValueExpression(tbcase, TBCase.class));
//		variables.put("tbcase", tbcase);
		System.out.println(e.getValue(context));
		
		e = factory.createValueExpression(context, "${toString(tbcase.name)}", String.class);
		System.out.println(e.getValue(context));
	}

	
	public Object getVariable(String name) {
		if ("tbcase".equals(name))
			 return tbcase;
		else return null;
	}

	
	public static String toString(Object value) {
		return "This is = " + value.toString();
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
	
}
