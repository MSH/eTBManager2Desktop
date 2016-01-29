/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;

import javax.el.ValueExpression;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.swingx.JXLabel;
import org.msh.core.Displayable;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.FieldReference;
import org.msh.xview.components.XField;
import org.msh.xview.swing.XViewUtils;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.FieldUI;
import org.msh.xview.swing.ui.ViewUI;


/**
 * Abstract editor for all field editor that wants to display a custom editor
 * in the form field
 * 
 * @author Ricardo Memoria
 *
 */
public abstract class FieldComponentUI extends ComponentUI<XField>{
	
	public static final String HANDLER_INT = "int";
	public static final String HANDLER_FLOAT = "float";
	public static final String HANDLER_LONG = "long";
	public static final String HANDLER_DOUBLE = "double";

    public static final int READONLY_MIN_WIDTH = 32;
	
	private JXLabel lblReadOnly;
	private int columnWidth;
	
	private ValueExpression expRequired;
	private ValueExpression expOptions;
	
		
	/**
	 * Create a component to display the content of the field when it's read-only
	 * @return instance of {@link JComponent}
	 */
	protected JComponent createReadOnlyComponent() {
		lblReadOnly = new JXLabel();
		lblReadOnly.setLineWrap(true);
		lblReadOnly.setVerticalAlignment(SwingConstants.TOP);
		lblReadOnly.setFont( UiConstants.fieldValue );
		lblReadOnly.setMinimumSize(new Dimension(READONLY_MIN_WIDTH, 16));
		return lblReadOnly;
	};

	
	/**
	 * Create the component to edit the field
	 * @return instance of {@link JComponent}
	 */
	protected abstract JComponent createEditComponent();
	

	/**
	 * Update the content of a read-only component
	 */
	protected void updateReadOnlyComponent() {
		JXLabel lbl = (JXLabel)getComponent();
		String s = getDisplayText();
		lbl.setText(s);
	}
	
	
	/**
	 * Update the content of the component used for editing
	 */
	protected abstract void updateEditComponent();
	
	
	/**
	 * Get the field value in the data model
	 * @return
	 */
	public Object getValue() {
		FieldReference fld = getView().getField();
		
		if (fld == null)
			return null;

		return getDataModel().getValue(fld.getFieldName());
	}

	
	/**
	 * Set the value of the field in the data model assigned to this component
	 * @param value is the field value to be set 
	 */
	public void setValue(Object value) {
		beginUpdate();
		try {
			FieldReference fld = getView().getField();
			
			if (fld == null) {
				throw new RuntimeException("No field reference found to component " + getId());
			}
			
			getDataModel().setValue(fld.getFieldName(), value);
		} finally {
			finishUpdate();
		}
	}

	
	/**
	 * Return the display text of the value
	 * @return String value
	 */
	public String getDisplayText() {
		Object obj = getValue();
		return getDisplayString(obj);
	}

	
	/**
	 * Update the width of read-only component
	 * @param width is the total width of the component
	 */
	protected void updateReadOnlyWidth(int width) {
		XViewUtils.adjustHeight(lblReadOnly, width);
		// workaround to possible bug in the line wrap
		String s = lblReadOnly.getText();
		lblReadOnly.setText("");
		lblReadOnly.setText(s);
	}

	
	/**
	 * Update the width of editing component
	 * @param width is the total width of the component
	 */
	protected void updateEditWidth(int width) {
		JComponent comp = getComponent();
		if (width < comp.getWidth()) {
			comp.setSize(width, comp.getHeight());
		}
		else {
			comp.setSize(comp.getPreferredSize());
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		columnWidth = width;
		if (isReadOnly()) {
			updateReadOnlyWidth(width);
		}
		else {
			updateEditWidth(width);
		}
		
		if (hasMessages()) {
			updateMessagesWidth(width);
		}
	}

	
	/**
	 * Indicate if there is a label beside this field component
	 * @return true if the label must be displayed, otherwise false (i.e., check boxes)
	 */
	public boolean isLabelVisible() {
		return true;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		if (isReadOnly()) {
			return createReadOnlyComponent();
		}
		else {
			return createEditComponent();
		}
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void doComponentUpdate() {
		if (isReadOnly()) {
			if (lblReadOnly == null) {
				disposeComponent();
			}
			updateReadOnlyComponent();
		}
		else {
			if (lblReadOnly != null) {
				disposeComponent();
				lblReadOnly = null;
			}
			updateEditComponent();
		}

		doMessageUpdate();
	}



	/** {@inheritDoc}
	 * @see org.msh.xview.swing.ui.ElementUI#validate()
	 */
	@Override
	public boolean validate() {
		clearMessages();
		doMessageUpdate();

		if ((!isVisible()) || (isReadOnly())) {
			return true;
		}
		
		if (!super.validate()) {
			doMessageUpdate();
			return false;
		}
		
		// check required value
		Object val = getValue();
		if (isRequired() && (val == null)) {
			addMessage(Messages.getString("javax.faces.component.UIInput.REQUIRED"));
			doMessageUpdate();
			return false;
		}

		// check custom rules
		if (!validateRules()) {
			doMessageUpdate();
			return false;
		}
		
		return true;
	}

	/**
	 * @return the columnWidth
	 */
	public int getColumnWidth() {
		return columnWidth;
	}

	
	/**
	 * Return true if the field is a required one, or false if it's the
	 * value is not required
	 * @return boolean value
	 */
	public boolean isRequired() {
		if (expRequired == null) {
			String s = getView().getRequired();
			
			if (s == null) {
				return false;
			}
			
			expRequired = getDataModel().createValueExpression(s, boolean.class);
		}
		
		return (Boolean)expRequired.getValue(getELContext());
	}

	
	/**
	 * Return the list of options to feed the field
	 * @return
	 */
	public Collection getOptions() {
		String optfield = getView().getOptions();
		if (expOptions == null) {
			if (optfield != null) {
				expOptions = getDataModel().createValueExpression(optfield, Object.class);
			}
		}

		// is there an expression defined?
		if (expOptions != null) {
			Object obj = expOptions.getValue(getELContext());
			if (obj.getClass().isArray()) {
				return Arrays.asList( ((Object[])obj) );
			}
			
			if (obj != null)
				return (Collection)obj;
		}
		
		Object lst;

		if (optfield == null) {
			Class type = getFieldClassType();
			if (!Enum.class.isAssignableFrom(type))
				return null;

			// if type is enum, return the list of values as default
			lst = type.getEnumConstants();
		}
		else lst = getDataModel().getValue(optfield);

		if (lst == null)
			return null;
		
		// is an array? so return it as a list
		if (lst instanceof Object[]) {
			Object[] objs = (Object[])lst;
			return Arrays.asList(objs);
		}

		return (Collection)lst;
	}

	
	/**
	 * Return the class type of the field being edited
	 * @return
	 */
	protected Class getFieldClassType() {
		FieldReference ref = getView().getField();
		Class type = getDataModel().getValueType(ref.getFieldName());
		
		// if no type is defined, check the handler
		if (type == null) {
			String handler = getView().getHandler();
			if (HANDLER_INT.equals(handler)) {
				return Integer.class;
			}
			
			if (HANDLER_LONG.equals(handler)) {
				return Long.class;
			}
			
			if (HANDLER_FLOAT.equals(handler)) {
				return Float.class;
			}
			
			if (HANDLER_DOUBLE.equals(handler)) {
				return Double.class;
			}
		}
		
		return type;
	}
	
	/**
	 * Return the string representation of the given object. If the object
	 * implements the {@link Displayable} interface, so the text is taken from
	 * its interface. If it is an enumeration, the text in taken from its key
	 * message. By the end, if none of the attempts work, the method <code>toString()</code>
	 * is called to return the display text
	 * 
	 * @param obj is the object to get the display text from.
	 * @return String value
	 */
	protected String getDisplayString(Object obj) {
		if (obj == null)
			return "";

		String labelExpr = getView().getOptionLabel();
		// is there a property definition for the label of the options ?
		if (labelExpr != null) {
			try {
				// return the property object
				Object val = PropertyUtils.getProperty(obj, labelExpr);
				return val != null? val.toString(): null;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (obj instanceof Displayable)
			return Messages.getString( ((Displayable)obj).getMessageKey() );
		
		if (obj instanceof Enum)
			return Messages.getString(obj.getClass().getSimpleName() + "." + obj.toString());

		return obj.toString();
	}

	
	/**
	 * Return the {@link ViewUI} component that will replace the default
	 * label view used in the field
	 * @return instance of {@link ViewUI} that will display the field label
	 */
	public ComponentUI createLabelDelegator() {
		return null;
	}
	
	/**
	 * Called when the parent (usually a {@link FieldUI}) hide the label delegator, i.e, 
	 * remove it from the view tree and destroy it. The creator of the label is
	 * informed just to have any clean up action
	 * @param labelDelegator
	 */
	public void disposeLabelDelegator(ComponentUI labelDelegator) {
		
	}

}
