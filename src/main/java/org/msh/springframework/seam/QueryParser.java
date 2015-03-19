package org.msh.springframework.seam;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class QueryParser {
	private List<ValueExpression> parameterValueBindings = new ArrayList<ValueExpression>();
	private StringBuilder ejbqlBuilder;

	public static String getParameterName(int loc) {
		return "el" + (loc + 1);
	}

	public String getEjbql() {
		return ejbqlBuilder.toString();
	}

	public List<ValueExpression> getParameterValueBindings() {
		return parameterValueBindings;
	}

	public QueryParser(String ejbql) {
		this(ejbql, 0);
	}

	public QueryParser(String ejbql, int startingParameterNumber) {
		StringTokenizer tokens = new StringTokenizer(ejbql, "#}", true);
		
		ejbqlBuilder = new StringBuilder(ejbql.length());
		
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if ("#".equals(token)) {
				if (!tokens.hasMoreTokens()) {
					throw new IllegalArgumentException("query fragment terminates in #");
				}
				String expressionToken = tokens.nextToken();
				if (!expressionToken.startsWith("{")) {
					throw new IllegalArgumentException("missing { after # in query fragment");
				}
				if (!tokens.hasMoreTokens()) {
					throw new IllegalArgumentException("missing } after expression in query fragment");
				}
				String expression = token + expressionToken
						+ tokens.nextToken();
				
				ejbqlBuilder.append(':').append( getParameterName(startingParameterNumber + parameterValueBindings.size()) );
				
				parameterValueBindings.add(Expressions.instance().createValueExpression(expression));
			} 
			else {
				ejbqlBuilder.append(token);
			}
		}
	}

}
