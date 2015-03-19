/**
 * 
 */
package org.msh.xview.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.el.VariableMapper;


import de.odysseus.el.util.SimpleContext;

/**
 * Define an EL context where variable resolution is done using the
 * form data model
 * @author Ricardo Memoria
 *
 */
public class DataModelELContext extends SimpleContext{

	private DefaultFormDataModel dataModel;
	private MyVariableMapper variables;
	
	/**
	 * Defines its own variable mapper in order to resolve variables
	 * from the data model
	 * @author Ricardo Memoria
	 *
	 */
	public class MyVariableMapper extends VariableMapper {
		Map<String, ValueExpression> map = Collections.emptyMap();
		DataModelELContext context;
		
		/**
		 * Default constructor
		 * @param context
		 */
		MyVariableMapper(DataModelELContext context) {
			this.context = context;
		}
		
		@Override
		public ValueExpression resolveVariable(String variable) {
			ValueExpression ve = map.get(variable);
			if (ve == null) {
				Object value = dataModel.getValue(variable);
				ve = dataModel.createInstanceExpression(value);
				context.setVariable(variable, ve);
			}
			return ve;
		}

		@Override
		public ValueExpression setVariable(String variable, ValueExpression expression) {
			if (map.isEmpty()) {
				map = new HashMap<String, ValueExpression>();
			}
			return map.put(variable, expression);
		}
	}

	/**
	 * Default constructor
	 * @param dm is the instance of the {@link DefaultFormDataModel} to get variable reference
	 */
	public DataModelELContext(DefaultFormDataModel dm) {
		this.dataModel = dm;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public VariableMapper getVariableMapper() {
		if (variables == null)
			variables = new MyVariableMapper(this);
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
