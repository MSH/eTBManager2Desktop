package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.List;

import org.msh.xview.components.FieldReference;

/**
 * A view must provide an implementation of this interface in order
 * to validate the names (variables) contained in an expression
 * and to mount a list of the declared (and valid) variables
 *  
 * @author Ricardo Memoria
 *
 */
public class ViewVariableValidator  {

	private List<String> declaredVariables;
	
	/**
	 * Return the list of declared variables that were validated  
	 * @return list of variable names
	 */
	public List<String> getDeclaredVariables() {
		return declaredVariables;
	}

	/** {@inheritDoc}
	 * @see org.msh.expressions.VariableValidator#isValidVariable(java.lang.String)
	 */
	public boolean isValidVariable(String name) {
		FieldReference ref = new FieldReference(name);
		boolean b = org.msh.xview.impl.VariableCreator.instance().isValidVariable(ref.getVariable());
		if (b) {
			if (declaredVariables == null)
				declaredVariables = new ArrayList<String>();
			declaredVariables.add(name);
		}

		return b;
	}
	
	/**
	 * Add a variable to the list of declared variables
	 * @param name
	 */
	protected void addVariable(String name) {
		if (declaredVariables == null)
			declaredVariables = new ArrayList<String>();
		declaredVariables.add(name);
	}
}

/*public class ViewVariableResolver implements VariableValidator {
	private XView view;
	private boolean addDependencies;
	
	public ViewVariableResolver(XView view, boolean addDependencies) {
		super();
		this.view = view;
		this.addDependencies = addDependencies;
	}

	 (non-Javadoc)
	 * @see org.msh.expressions.VariableValidator#isValidVariable(java.lang.String)
	 
	@Override
	public boolean isValidVariable(String name) {
		FieldReference ref = new FieldReference(name);
		boolean b = org.msh.xview.impl.VariableCreator.instance().isValidVariable(ref.getVariable());
		if ((b) && (addDependencies))
			view.addDependency(name);
		return b;
	}
}*/