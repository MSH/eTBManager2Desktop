package org.msh.xview.swing.ui.fields;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Laboratory;
import org.msh.etbm.entities.PersonNameComponent;
import org.msh.etbm.entities.Tbunit;



/**
 * Register and create field editors available in a form. This is the base
 * class in use by the forms to instantiate the field controllers that will
 * support different editors according to the data type
 * 
 * @author Ricardo Memoria
 *
 */
public class FieldComponentFactory {

	private static final FieldComponentFactory instance_ = new FieldComponentFactory();

	private List<FieldComponentUIInfo> editors = new ArrayList<FieldComponentUIInfo>();
	
	
	/**
	 * Default constructor. Protected to avoid that more than one instance is created
	 */
	protected FieldComponentFactory() {
		super();
		registerEditor(String.class, StringFieldUI.class);
		registerEditor(Object.class, "combo", ComboFieldUI.class);
		registerEditor(Object.class, "radio", RadioFieldUI.class);
		registerEditor(Enum.class, ComboFieldUI.class);
		registerEditor(Integer.class, NumberFieldUI.class);
		registerEditor(int.class, NumberFieldUI.class);
		registerEditor(Double.class, NumberFieldUI.class);
		registerEditor(double.class, NumberFieldUI.class);
		registerEditor(Float.class, NumberFieldUI.class);
		registerEditor(float.class, NumberFieldUI.class);
		registerEditor(Date.class, DateFieldUI.class);
		registerEditor(String.class, "text", TextFieldUI.class);
//		registerEditor(Boolean.class, "yesno", YesNoFieldUI.class);
		registerEditor(boolean.class, "yesno", YesNoFieldUI.class);
		registerEditor(Boolean.class, CheckboxFieldUI.class);
		registerEditor(boolean.class, CheckboxFieldUI.class);
		registerEditor(PersonNameComponent.class, PersonNameFieldUI.class);
		registerEditor(Integer.class, "combo", NumberComboFieldUI.class);
		registerEditor(int.class, "combo", NumberComboFieldUI.class);
		registerEditor(Long.class, "combo", NumberComboFieldUI.class);
		registerEditor(long.class, "combo", NumberComboFieldUI.class);
		registerEditor(Tbunit.class, UnitFieldUI.class);
		registerEditor(Laboratory.class, LaboratoryFieldUI.class);
		registerEditor(AdministrativeUnit.class, AdminUnitFieldUI.class);
		registerEditor(Object.class, "combo", ComboFieldUI.class);
		registerEditor(Object.class, ReadOnlyUI.class);

		// general handlers (mostly used in custom properties)
		registerEditor(Object.class, "string", StringFieldUI.class);
		registerEditor(Object.class, "yesno", YesNoFieldUI.class);
		registerEditor(Object.class, "int", NumberFieldUI.class);
		registerEditor(Object.class, "long", NumberFieldUI.class);
		registerEditor(Object.class, "double", NumberFieldUI.class);
		registerEditor(Object.class, "float", NumberFieldUI.class);
		registerEditor(Object.class, "tbunit", UnitFieldUI.class);
		registerEditor(Object.class, "laboratory", LaboratoryFieldUI.class);
		registerEditor(Object.class, "adminunit", AdminUnitFieldUI.class);

		
/*
		// this is the default editor
		registerEditor(Object.class, ReadOnlyEditor.class);
*/
	}
	
	/**
	 * Create an instance of the field editor by its type to be edited
	 * @param type
	 * @return
	 */
	public FieldComponentUI createFieldComponentUI(Class type, String handler) {
		if (type == null) {
			type = Object.class;
		}
		Class editorClass = findEditComponentClass(type, handler);
		if (editorClass == null) {
			throw new RuntimeException("No factory found for field component of type " + type.getName() + " and handler = " + handler);
		}

		try {
			return (FieldComponentUI)editorClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Register a new editor (edit and read-only class) for a given type
	 * @param type the class type to be registered
	 * @param compEdit the AWT component responsible for editing a value of the given type
	 * @param compReadonly the AWT component responsible for displaying a value of the given type
	 */
	public void registerEditor(Class type, Class<? extends FieldComponentUI> editorClass) {
		editors.add( new FieldComponentUIInfo(type, editorClass, null));
	}

	
	/**
	 * Register a new editor (edit and read-only class) for a given type
	 * @param type the class type to be registered
	 * @param compEdit the AWT component responsible for editing a value of the given type
	 * @param compReadonly the AWT component responsible for displaying a value of the given type
	 */
	public void registerEditor(Class type, String handler, Class<? extends FieldComponentUI> editorClass) {
		editors.add( new FieldComponentUIInfo(type, editorClass, handler));
	}
	
	
	/**
	 * Return the class of the AWT component to be used to edit 
	 * a value of the given class type
	 * @param type
	 * @return null if edit component class was not found
	 */
	public Class<? extends FieldComponentUI> findEditComponentClass(Class type, String handler) {
		FieldComponentUIInfo info = findEditor(type, handler);
		return info == null? null: info.getEditorClass();
	}


	/**
	 * Find information of an editor for a given class type. The editor must be previously 
	 * registered with the <code>registerEditor()</code>
	 * @param type
	 * @return null if editor information was not found
	 */
	protected FieldComponentUIInfo findEditor(Class type, String handler) {
		if (handler == null) {
			// find for editor with same class
			for (FieldComponentUIInfo fld: editors)
				if ((fld.getEditorType() == type) && (fld.getHandler() == null))
					return fld;

			// find for editor with assignable class
			for (FieldComponentUIInfo fld: editors)
				if ((fld.getEditorType().isAssignableFrom(type)) && (fld.getHandler() == null))
					return fld;
		}
		else {
			// find for editor with same class and handler
			for (FieldComponentUIInfo fld: editors)
				if ((fld.getEditorType() == type) && (handler.equals(fld.getHandler())))
					return fld;

			// find for editor with assignable class and handler
			for (FieldComponentUIInfo fld: editors)
				if ((fld.getEditorType().isAssignableFrom(type)) && (handler.equals(fld.getHandler())))
					return fld;
		}

		return null;
	}


	/**
	 * Return the singleton instance of the {@link FieldComponentFactory} class
	 * @return instance of {@link FieldComponentFactory}
	 */
	public static FieldComponentFactory instance() {
		return instance_;
	}
	
	
	/**
	 * Store information about a field editor and its associated types
	 * @author Ricardo Memoria
	 *
	 */
	public class FieldComponentUIInfo {
		private Class editorType;
		
		private Class<? extends FieldComponentUI> editorClass;
		
		private String handler;

		/**
		 * Default constructor
		 * @param editorType is the type of data being supported by the editor
		 * @param editorClass is the field editor class
		 * @param handler is an optional handler of the editor
		 */
		public FieldComponentUIInfo(Class editorType, Class<? extends FieldComponentUI> editorClass, String handler) {
			super();
			this.editorType = editorType;
			this.editorClass = editorClass;
			this.handler = handler;
		}

		/**
		 * Return the editor type
		 * @return the editorType
		 */
		public Class getEditorType() {
			return editorType;
		}

		/**
		 * Change the editor type
		 * @param editorType the editorType to set
		 */
		public void setEditorType(Class editorType) {
			this.editorType = editorType;
		}

		/**
		 * Return the editor class
		 * @return the editorClass
		 */
		public Class<? extends FieldComponentUI> getEditorClass() {
			return editorClass;
		}

		/**
		 * Change the editor class
		 * @param editorClass the editorClass to set
		 */
		public void setEditorClass(Class<? extends FieldComponentUI> editorClass) {
			this.editorClass = editorClass;
		}

		/**
		 * Return the handler
		 * @return the handler
		 */
		public String getHandler() {
			return handler;
		}

		/**
		 * Change the handler
		 * @param handler the handler to set
		 */
		public void setHandler(String handler) {
			this.handler = handler;
		}
	}
}
