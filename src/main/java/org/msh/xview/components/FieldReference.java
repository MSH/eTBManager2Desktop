package org.msh.xview.components;

/**
 * Contain information about a field name reference.
 * <p/>
 * The <code>variable</code> is the name of the variable of the field.
 * <code>property</code> is the full qualified name of the property for 
 * the given variable.
 * <p/>
 * IndexedName is specially used in situations where the variable resolution
 * depends on another value, and is preceded by a @ character (for example, 
 * to reference components of the form using the notation form@compId.visible)
 * 
 * @author Ricardo Memoria
 *
 */
public class FieldReference {

	private String variable;
	private String property;

	public FieldReference() {
		super();
	}
	
	public FieldReference(String fieldName) {
		super();
		setFieldName(fieldName);
	}
	
	public FieldReference(String variable, String property, String indexedName) {
		super();
		this.variable = variable;
		this.property = property;
	}

	/**
	 * Return the field name of this field reference
	 * @return
	 */
	public String getFieldName() {
		return variable + "." + property;
	}
	
	/**
	 * Set the field name being referenced
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		int firstDotPos = fieldName.indexOf('.');
		
		if (firstDotPos == -1) {
			variable = fieldName;
			property = null;
			return;
		}

		variable = fieldName.substring(0, firstDotPos);
		property = fieldName.substring(firstDotPos + 1);
		
		if ((variable == null)|| (variable.isEmpty()) || 
			(property == null) || (property.isEmpty()))
			throwIllegalNameException(fieldName);
	}
	
	
	protected void throwIllegalNameException(String fieldName) {
		throw new IllegalArgumentException("Invalid field name reference " + fieldName);
	}
	
	/**
	 * Return the variable assigned to this field reference
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * Return the property of the variable of this field reference
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FieldReference [variable=" + variable + ", property="
				+ property + "]";
	}
}
