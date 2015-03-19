package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.desktop.app.App;
import org.msh.xview.FormDataModel;
import org.msh.xview.ValueChangeListener;
import org.msh.xview.components.FieldReference;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;


/**
 * Standard implementation of the {@link FormDataModel} interface
 * 
 * @author Ricardo Memoria
 *
 */
public class DefaultFormDataModel implements FormDataModel {

	// the expression factory used to create EL expressions
	private static final ExpressionFactory expressionFactory = new ExpressionFactoryImpl();

	private Map<String, Object> variables = new HashMap<String, Object>();
	private List<ValueChangeListener> listeners;
	private DataModelELContext context = new DataModelELContext(this);
	private FormDataModel parent;
	
	
	/**
	 * Default constructor passing a parent data model to be used
	 * if the variables are not resolved
	 * @param parent instance of {@link FormDataModel}
	 */
	public DefaultFormDataModel(FormDataModel parent) {
		super();
		this.parent = parent;
		initializeELContext();
	}
	
	/**
	 * Default constructor
	 */
	public DefaultFormDataModel() {
		super();
		initializeELContext();
	}
	
	
	private void initializeELContext() {
		// initialize the model variable
		try {
			context.setFunction("", "msg", App.class.getMethod("getMessage", String.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		context.setVariable("model", expressionFactory.createValueExpression(this, getClass()));
		variables.put("model", this);
	}

	
	/**
	 * Return the value of a variable by its name
	 * @param varname is the name of the variable
	 * @return the value of the variable
	 */
	public Object getVariableByName(String varname) {
		Object variable = variables.get(varname);
		if (variable == null) {
			if (parent != null) {
				variable = parent.getValue(varname);
			}

			if (variable == null) {
				variable = createVariableInstance(varname);
				setVariableValue(varname, variable);
				checkPropertyLinks(varname, variable);
			}
		}
		
		return variable;
	}

	
	
	/**
	 * Set a value to a variable and create a reference to the variable in the EL expression
	 * @param varname is the variable name
	 * @param value is the variable value
	 */
	protected void setVariableValue(String varname, Object value) {
		String exp = "${model.getVariableByName('" + varname + "')}";
		context.setVariable(varname, 
				expressionFactory.createValueExpression(context,exp, value.getClass()));
		variables.put(varname, value);
	}

	
	/**
	 * Return the EL context in use
	 * @return instance of {@link SimpleContext}
	 */
	@Override
	public SimpleContext getELContext() {
		return context;
	}
	
	/**
	 * Create a value expression from a given expression 
	 * @param expression
	 * @param expectedClassResult
	 * @return instance of {@link ValueExpression}
	 */
	@Override
	public ValueExpression createValueExpression(String expression, Class expectedClassResult) {
		return expressionFactory.createValueExpression(context, expression, expectedClassResult);
	}

	/**
	 * Create a value expression based on an instance value
	 * @param value
	 * @return
	 */
	public ValueExpression createInstanceExpression(Object value) {
		return expressionFactory.createValueExpression(value, value.getClass());
	}

	
	/**
	 * Resolve a variable using the standard {@link VariableCreator}
	 * @param varname
	 * @return
	 */
	protected Object createVariableInstance(String varname) {
		// before trying to create an instance, search for field reference
		List<String> links = VariableCreator.instance().getFieldLinksToVariable(varname);
		// look for reference that is actually a field
		for (String fieldname: links) {
			FieldReference fieldRef = new FieldReference(fieldname);
			if (variables.containsKey(fieldRef.getVariable())) {
				Object val = getFieldValue(fieldRef);
				if (val != null)
					return val;
			}
		}

		// create a new instance
		Object variable = VariableCreator.instance().createVariableFromFactory(varname);
		if (variable == null)
			throw new IllegalArgumentException("Variable not found: " + varname);
		
		return variable;
	}

	
	/**
	 * Check links of properties of this variable with other variables
	 * @param varname
	 * @param variable
	 */
	protected void checkPropertyLinks(String varname, Object variable) {
		Map<String, String> map = VariableCreator.instance().getFieldLinks(varname);
		
		for (String field: map.keySet()) {
			String othervarname = map.get(field);
			// check if variable was instantiated
			if (variables.containsKey(othervarname)) {
				Object othervar = getVariableByName(othervarname);
				FieldReference ref = new FieldReference(field);
				setProperty(variable, ref.getProperty(), othervar);
			}
		}
	}


	/**
	 * Find a variable instance in one property linked to a variable name
	 * 
	 * @param varname
	 * @return
	 */
	protected Object findInstanceInOtherVariableProperties(String varname) {
		VariableCreator varResolver = VariableCreator.instance();

		// return all variables and its properties that can reference the item 
		List<String> props = varResolver.getFieldLinksToVariable(varname);

		// search for instantiated variable
		for (String prop: props) {
			FieldReference ref = new FieldReference(prop);
			if (variables.containsKey(ref.getVariable())) {
				Object val = getFieldValue(ref);
				if (val != null)
					return val;
			}
		}
		return null;
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#getFieldValue(org.msh.xview.components.FieldReference)
	 */
	protected Object getFieldValue(FieldReference field) {
		Object object = getVariableByName(field.getVariable());

		if ((field.getProperty() == null) || (field.getProperty().isEmpty()))
			return object;

		return getProperty(object, field.getProperty());
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#setFieldValue(org.msh.xview.components.FieldReference, java.lang.Object)
	 */
	protected void setFieldValue(FieldReference field, Object value) {
		// no property was defined?
		if ((field.getProperty() == null) || (field.getProperty().isEmpty())) {
			setVariable(field.getVariable(), value);
			return;
		}

		Object object = getVariableByName(field.getVariable());

		Object oldVal = getProperty(object, field.getProperty());
		setProperty(object, field.getProperty(), value);
		if (!areEquals(oldVal, value))
			notifyValueChange(field.getFieldName(), oldVal, value);
	}
	
	
	/**
	 * Set the property of an object
	 * @param obj
	 * @param property
	 * @param value
	 */
	protected void setProperty(Object obj, String property, Object value) {
		try {
			if (value == null) {
				value = checkPropertyForNullValue(obj, property);
			}
			PropertyUtils.setProperty(obj, property, value);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	/**
	 * If the value is null, check if the type being assigned is a primitive
	 * type (for example, int or boolean) that doesn't accept a null value 
	 * @param obj the object containing the property
	 * @param property the name of the property
	 * @return the value to be assigned to the property (different from null)
	 */
	private Object checkPropertyForNullValue(Object obj, String property) throws Exception {
		Class type = PropertyUtils.getPropertyType(obj, property);
		if (boolean.class.isAssignableFrom(type))
			return false;

		if (int.class.isAssignableFrom(type))
			return 0;
		
		if (long.class.isAssignableFrom(type))
			return 0L;
		
		if (char.class.isAssignableFrom(type))
			return (char)0;
		
		if (float.class.isAssignableFrom(type))
			return 0.0F;
		
		if (double.class.isAssignableFrom(type))
			return 0.0D;
	
		return null;
//		throw new IllegalArgumentException("Type not expected to receive null value: " + type);
	}

	/**
	 * Get the value of a property in an object
	 * @param obj
	 * @param property
	 * @return
	 */
	protected Object getProperty(Object obj, String property) {
		try {
			return PropertyUtils.getProperty(obj, property);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	

	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#getFieldType(org.msh.xview.components.FieldReference)
	 */
	@Override
	public Class getValueType(String valueref) {
		FieldReference field = new FieldReference(valueref);
		Object object = getVariableByName(field.getVariable());
//		Class type = ClassUtils.getPropertyType(object.getClass(), field.getProperty());
//		return type;
		try {
			return PropertyUtils.getPropertyType(object, field.getProperty());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Set the value of a variable by its name
	 * @param varname is the name of the variable
	 * @param value is the value of the variable
	 */
	protected void setVariable(String varname, Object value) {
		// check if variable has controller
		Object oldValue = variables.get(varname);
		setVariableValue(varname, value);

		// check if there are other variables where instance was connected to properties of the previous variable
		List<String> lst = VariableCreator.instance().getFieldLinksToVariable(varname);
		for (String field: lst) {
			FieldReference ref = new FieldReference(field);
			if ((containsVariable(ref.getVariable())) && (getFieldValue(ref) == null)) {
				setFieldValue(ref, value);
			}
		}
		checkPropertyLinks(varname, value);

		if (!areEquals(value, oldValue))
			notifyValueChange(varname, oldValue, value);
	}
	
	
	/**
	 * Check if two objects are the same
	 * @param obj1
	 * @param obj2
	 * @return true if they are
	 */
	protected boolean areEquals(Object obj1, Object obj2) {
		if (obj1 == obj2)
			return true;

		if ((obj1 == null) && (obj2 == null))
			return true;
		
		if ((obj1 == null) || (obj2 == null))
			return false;

		return obj1.equals(obj2);
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#getVariables()
	 */
	@Override
	public Set<String> getVariables() {
		return variables.keySet();
	}

	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String fieldname) {
		if (fieldname == null)
			return null;
		return getFieldValue(new FieldReference(fieldname));
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValue(String fieldname, Object value) {
		setFieldValue(new FieldReference(fieldname), value);
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#addChangeListener(org.msh.xview.ActionChangeListener)
	 */
	@Override
	public void addChangeListener(ValueChangeListener listener) {
		if (listeners == null)
			listeners = new ArrayList<ValueChangeListener>();
		listeners.add(listener);
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#removeChangeListener(org.msh.xview.ActionChangeListener)
	 */
	@Override
	public void removeChangeListener(ValueChangeListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
	}

	/**
	 * Notify all listeners about change in the value of a field
	 * @param field
	 * @param value
	 */
	protected void notifyValueChange(String field, Object oldValue, Object newValue) {
		if (listeners == null)
			return;
		
		for (ValueChangeListener listener: listeners)
			listener.onValueChange(field, oldValue, newValue);
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#containsVariable(java.lang.String)
	 */
	@Override
	public boolean containsVariable(String varname) {
		return variables.containsKey(varname);
	}



	/** {@inheritDoc}
	 * @see org.msh.xview.FormDataModel#clear()
	 */
	@Override
	public void clear() {
		variables.clear();
	}


	/** {@inheritDoc}
	 */
	@Override
	public Object getVariable(Class variableClass) {
		for (String varname: variables.keySet()) {
			Object variable = variables.get(varname);
			if (variableClass.isAssignableFrom(variable.getClass()))
				return variable;
		}
		return null;
	}

	/**
	 * @return the context
	 */
	public ELContext getContext() {
		return context;
	}

	/**
	 * @return the parent
	 */
	public FormDataModel getParent() {
		return parent;
	}

}
