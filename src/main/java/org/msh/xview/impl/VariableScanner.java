/**
 * 
 */
package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Scan for other variable dependencies in the expression 
 * @author Ricardo Memoria
 *
 */
public class VariableScanner {

	/**
	 * Scan for variables inside the expression
	 * @param expression
	 * @return
	 */
	public static List<String> scan(String expression) {
		List<String> lst = new ArrayList<String>();
		
		int pos = 0;
		while (pos < expression.length()) {
			int n = expression.indexOf("${", pos);
			if (n == -1) {
				n = expression.indexOf("#{", pos);
			}
			
			if (n == -1) {
				break;
			}
			
			int end = expression.indexOf("}", n);
			
			if (end == -1) {
				break;
			}
			
			String exp = expression.substring(n + 2, end);
			scanExpression(lst, exp);
			pos = end;
		}
		
		return lst;
	}

	/**
	 * Scan an expression that was identified inside a ${} or a #{}
	 * @param lst
	 * @param substring
	 */
	private static void scanExpression(List<String> lst, String exp) {
		int index = 0;
		while (index < exp.length()) {
			char c = exp.charAt(index);
			if (Character.isJavaIdentifierStart(c)) {
				String val = exp.substring(index);
				index = index + scanIdentifier(lst, val);
			}
			
			// is beginning of a string
			if (c == '\'') {
				index = exp.indexOf('\'', index + 1);
				if (index == -1) {
					return;
				}
			}
			
			index++;
		}
	}

	/**
	 * Scan the beginning of an identifier
	 * @param lst
	 * @param substring
	 * @return
	 */
	private static int scanIdentifier(List<String> lst, String exp) {
		int index = 1;
		while (index < exp.length()) {
			char c = exp.charAt(index);
			if (!Character.isJavaIdentifierPart(c)) {
				if (c != '.') {
					// is a function ?
					if (c == '(') {
						return index;
					}
					
					// is a variable ?
					if (c == '[') {
						return index;
					}
					
					// is the end of identifier
					String ident = exp.substring(0, index);
					addIdentifier(lst, ident);
					
					return index;
				}
			}
			index++;
		}
		
		addIdentifier(lst, exp);
		return index;
	}
	
	/**
	 * Add identifier to the list, depending on its content
	 * @param lst
	 * @param ident
	 */
	private static void addIdentifier(List<String> lst, String ident) {
		if ("empty".equals(ident))
			return;
		if ("not".equals(ident))
			return;
		if ("null".equals(ident))
			return;
		if ("ne".equals(ident))
			return;
		if ("eq".equals(ident))
			return;
		if ("and".equals(ident))
			return;
		if ("or".equals(ident))
			return;
		if ("lt".equals(ident))
			return;
		if ("gt".equals(ident))
			return;
		if ("ge".equals(ident))
			return;
		if ("le".equals(ident))
			return;
		if ("div".equals(ident))
			return;
		if ("mod".equals(ident))
			return;

		if (lst.contains(ident))
			return;

		lst.add(ident);
	}
}
