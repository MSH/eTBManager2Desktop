package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.msh.xview.components.FieldReference;
import org.msh.xview.components.XField;
import org.msh.xview.components.XField.CharCase;
import org.msh.xview.components.XButton;
import org.msh.xview.components.XColumn;
import org.msh.xview.components.XContainer;
import org.msh.xview.components.XCustom;
import org.msh.xview.components.XForm;
import org.msh.xview.components.XHeader1;
import org.msh.xview.components.XHeader2;
import org.msh.xview.components.XHeader3;
import org.msh.xview.components.XLabel;
import org.msh.xview.components.XParagraph;
import org.msh.xview.components.XRegion;
import org.msh.xview.components.XRepeat;
import org.msh.xview.components.XSection;
import org.msh.xview.components.XTable;
import org.msh.xview.components.XTable.RowSelection;
import org.msh.xview.components.XText;
import org.msh.xview.components.XValidationRule;
import org.msh.xview.components.XView;

/**
 * Read the content of an XML file containing a form structure
 * 
 * @author Ricardo Memoria
 *
 */
public class XmlFormReader {

	private static final String TABLE_CHECKBOX_COLUMN_FIELD = "checkbox-column-field";
	private static final String TABLE_ROW_SELECTION = "row-selection";
	private static final String CONTAINER_VALUES = "values";
	private static final String CONTAINER_VAR = "var";
	
	private int idcounter;

	public XForm read(String xml) {
		try {
			Document doc = DocumentHelper.parseText(xml);
			
			return read(doc);
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read an XML document
	 * @param doc
	 * @return
	 */
	public XForm read(Document doc) {
		Element el = doc.getRootElement();
		assertElementName(el, "form");
		idcounter = 1;
		
		XForm form = (XForm)createViewByElement(el);
		form.refreshDependencies();
/*		Element elSections = el.element("layout");

		if (elSections != null)
			for (Element elsec: (List<Element>)elSections.elements()) {
				form.add( readSection(elsec) );
			}
*/
		return form;
	}

	
	/**
	 * Create a view by its tag name
	 * 
	 * @param el is the XML element
	 * @return instance of the {@link XView} class representing the form view,
	 * or return null if the tag name is not supported
	 */
	protected XView createViewByElement(Element el) {
		String tagname = el.getName();

		if ("form".equals(tagname))
			return readForm(el);
		
		if ("section".equals(tagname)) {
			return readSection(el);
		}

		if ("field".equals(tagname))
			return readField(el);

		if ("table".equals(tagname))
			return readTable(el);
		
		if ("custom".equals(tagname))
			return readCustom(el);
		
		if ("h1".equals(tagname))
			return readText(el, new XHeader1());
		
		if ("h2".equals(tagname))
			return readText(el, new XHeader2());
		
		if ("h3".equals(tagname))
			return readText(el, new XHeader3());

		if ("label".equals(tagname))
			return readText(el, new XLabel());
		
		if ("p".equals(tagname))
			return readText(el, new XParagraph());
		
		if ("repeat".equals(tagname))
			return readRepeat(el);
		
		if ("region".equals(tagname))
			return readRegion(el);
		
		if ("button".equals(tagname))
			return readButton(el);
		
		if ("column".equals(tagname))
			return readColumn(el);

		return null;
	}
	
	
	/**
	 * Read attributes of a custom element
	 * @param el element pointing to the XML data of the custom
	 * @return instance of {@link XCustom} class
	 */
	private XCustom readCustom(Element el) {
		XCustom custom = new XCustom();
		readView(el, custom);
		return custom;
	}

	/**
	 * Read form attributes by its XML element
	 * @param frm
	 * @param el
	 * @return
	 */
	protected XForm readForm(Element el) {
		XForm frm = new XForm();
		readView(el, frm);
		Integer colcount = readIntAttribute(el, "column-count");
		frm.setColumnCount(colcount != null? colcount: 1);
		frm.setValidator( readStringAttribute(el, "validator"));
		frm.setAction( readStringAttribute(el, "action"));
//		frm.setTitle( readStringAttribute(el, "title") );
		return frm;
	}


	/**
	 * Read the content of a repeat tag
	 * @param el
	 * @return instance of the {@link XRepeat} class
	 */
	protected XRepeat readRepeat(Element el) {
		XRepeat rep = new XRepeat();
		rep.setVar( readStringAttribute(el, CONTAINER_VAR));
		rep.setValues( readStringAttribute(el, CONTAINER_VALUES));
		readView(el, rep);
		return rep;
	}
	
	
	/**
	 * Read the content of a column tag inside an {@link XColumn} object
	 * @param el is the XML element node
	 * @return instance of {@link XColumn} class
	 */
	protected XColumn readColumn(Element el) {
		XColumn col = new XColumn();
		readView(el, col);
		col.setTitle( readStringAttribute(el, "title"));
		return col;
	}
	
	/**
	 * Read the content of a region tag
	 * @param el
	 * @return
	 */
	protected XRegion readRegion(Element el) {
		XRegion reg = new XRegion();
		readView(el, reg);
		return reg;
	}

	
	/**
	 * Read the content of a button tag
	 * @param el
	 * @return
	 */
	protected XButton readButton(Element el) {
		XButton btn = new XButton();
		readView(el, btn);
		btn.setLabel( readStringAttribute(el, "label") );
		btn.setAction( readStringAttribute(el, "action"));
		btn.setIcon( readStringAttribute(el, "icon"));
		return btn;
	}
	
	/**
	 * Read common attributes of a view
	 * @param el
	 * @param view
	 */
	protected void readView(Element el, XView view) {
		view.setId( readStringAttribute(el, "id") );
		view.setVisible( readStringAttribute(el, "visible") );
		view.setReadOnly( readStringAttribute(el, "readonly") );
		Integer colspan = readIntAttribute(el, "colspan");
		view.setColSpan( colspan == null? 1: colspan );
		view.setForceNewRow( "true".equalsIgnoreCase( readStringAttribute(el, "force-new-row")) );

		// read the parameters of the view
		readParameters(el, view);
		
		readValidationRules(view, el);
	
		// specify unique ID of a component
		if (view.getId() == null) {
			view.setId("comp" + idcounter);
			idcounter++;
		}

		// read child views
		List children = el.elements();

		if (children != null)
			for (Element elsec: (List<Element>)children) {
				XView child = createViewByElement(elsec);
				// child element is a view ?
				if (child != null) {
					if (!(view instanceof XContainer))
						throw new IllegalArgumentException("View cannot have child views: " + view.getClass().getCanonicalName());
					((XContainer)view).add( child );
				}
			}
	}
	
/*
	protected void readFormElement(Element el, XElement comp) {
		readView(el, comp);
		comp.setColumnSpan( "true".equalsIgnoreCase( readStringAttribute(el, "colspan")) );
		comp.setForceNewRow( "true".equalsIgnoreCase( readStringAttribute(el, "force-new-row")) );
	}
*/	
	/**
	 * Read a sequence of param tags defined as children of the view
	 * @param el
	 * @param view
	 */
	private void readParameters(Element el, XView view) {
		for (Element elparam: (List<Element>)el.elements("param")) {
			String name = readStringAttribute(elparam, "name");
			String value = readStringAttribute(elparam, "value");
			view.setParameter(name, value);
		}
	}

	/**
	 * Read section from XML
	 * @param el
	 * @return
	 */
	private XSection readSection(Element el) {
		XSection sec = new XSection();
		readView(el, sec);
		sec.setTitle( readStringAttribute(el, "title"));
		Integer count = readIntAttribute(el, "column-count");
		sec.setColumnCount( count != null? count: 1 );
		String showBorder = readStringAttribute(el, "show-border");
		sec.setShowBorder( (showBorder == null) || ("true".equals(showBorder)) || ("1".equals(showBorder)) );
/*
		List<Element> elcomps = el.elements();
		if (elcomps != null) 
			for (Element elcomp: elcomps) {
				XView comp = null;
				String name = elcomp.getName();

				if ("section".equals(name))
					comp = readSection(elcomp);
				else
				if ("text".equals(name))
					comp = readTextComponent(elcomp);
				else
				if ("field".equals(name)) {
					comp = new XField();
					readFieldComponent(elcomp, (XField)comp);
				}
				else
				if ("table".equals(name))
					comp = readTable(elcomp);
				
				if (comp == null)
					throw new IllegalArgumentException("Element not expected: " + name);
				
				sec.add(comp);
			}
*/		return sec;
	}


	/**
	 * Create and read a new {@link XField} component from an XML element
	 * @param el
	 * @return
	 */
	private XField readField(Element el) {
		XField comp = new XField();
		readView(el, comp);
		comp.setHint( readStringAttribute(el, "hint"));
		comp.setRequired( readStringAttribute(el, "required"));
		comp.setLabel( readStringAttribute(el, "label"));
		comp.setMaxChars( readIntAttribute(el, "maxchars"));
		comp.setInputMask( readStringAttribute(el, "input-mask"));
		comp.setWidth( readIntAttribute(el, "width"));
		comp.setHandler( readStringAttribute(el, "handler"));
		comp.setOptions( readStringAttribute(el, "options"));
		comp.setOptionLabel( readStringAttribute(el, "option-label"));
		comp.setLabel( readStringAttribute(el, "label") );
		
		String s = readStringAttribute(el, "char-case");
		if (s != null) {
			if ("UPPER".equals(s))
				comp.setCharCase(CharCase.UPPER);
			else 
				if ("LOWER".equals(s))
					comp.setCharCase(CharCase.LOWER);
				else if ("NORMAL".equals(s))
					comp.setCharCase(CharCase.NORMAL);
					else throw new IllegalArgumentException("Invalid charCase " + s);
		}

		String value = readStringAttribute(el, "value");
		if (value == null) {
			throw new RuntimeException("No value defined to field component " + comp.getId());
		}
		FieldReference fld = new FieldReference( value );
		comp.setField( fld );

		// if options were defined and no handler set, set it to combo as default
		if ((comp.getOptions() != null) && (comp.getHandler() == null)) {
			comp.setHandler("combo");
		}

		return comp;
	}


	/**
	 * Read validation rules that are represented by tags "validation" inside the given element 
	 * @param el
	 * @return the list of validation rules, or null if there is no validation rule
	 */
	protected void readValidationRules(XView view, Element el) {
		List<XValidationRule> lst = null;
		
		List<Element> rules = el.elements("validation");

		for (Element elrule: rules) {
			XValidationRule rule = new XValidationRule(view);
			rule.setEnabledIf( readStringAttribute(elrule, "enabled-if"));
			rule.setRule( readStringAttribute(elrule, "rule"));
			rule.setMessage( readStringAttribute(elrule, "message"));
			if (lst == null)
				lst = new ArrayList<XValidationRule>();
			lst.add(rule);
		}
		view.setRules(lst);
	}

	/**
	 * Read a table tag element from the XML
	 * @param el is instance of the {@link Element} containing the data of the table 
	 * @return instance of the {@link XTable} class containing the table definition
	 */
	protected XTable readTable(Element el) {
		XTable table = new XTable();
		readView(el, table);
		table.setValues( readStringAttribute(el, CONTAINER_VALUES) );
		table.setVar( readStringAttribute(el, CONTAINER_VAR) );
		table.setRowsEditors( "true".equals(readStringAttribute(el, "rows-editor")));
		table.setRowSelection( readEnumAttribute(el, TABLE_ROW_SELECTION, RowSelection.class, RowSelection.NONE));
		table.setRowCheckBoxField( readStringAttribute(el, TABLE_CHECKBOX_COLUMN_FIELD));
		
		if ((table.getRowSelection() == RowSelection.MULTIPLE) && (table.getRowCheckBoxField() == null)) {
			throw new RuntimeException("Property '" + TABLE_CHECKBOX_COLUMN_FIELD + "' not set for multiple row selection");
		}
		return table;
	}

	/**
	 * Read the content of a label tag element
	 * @param elcomp is instance of the {@link Element} containing the label data 
	 * @return instance of the {@link XLabel} class with data of the label definition
	 */
	private XText readText(Element elcomp, XText txt) {
		txt.setText( elcomp.getText());
		readView(elcomp, txt);
		return txt;
	}
	
	

	/**
	 * Parse integer Attribute
	 * @param el is instance of the {@link Element} containing the attribute 
	 * @param attribname is the attribute name of the element
	 * @return {@link Integer} value of the element attribute, or null if no value was found
	 */
	private Integer readIntAttribute(Element el, String attribname) {
		String val = el.attributeValue(attribname);
		if (val == null)
			return null;
		
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro reading attribute " + attribname + ". Expected number but found " + val);
		} 
	}

	
	/**
	 * Assert element is of a specific name. If is not of the specific name 
	 * an {@link IllegalArgumentException} is thrown
	 * @param el is instance of the {@link Element} containing the attribute 
	 * @param name is the attribute name of the element
	 */
	protected void assertElementName(Element el, String name) {
		if (!el.getName().equals(name))
			throw new IllegalArgumentException("Element " + name + " expected");
	}
	
	/**
	 * Read a child element from a main element
	 * @param el instance of the {@link Element} containing the attribute 
	 * @param attribName attribute name of the element
	 * @return String value with the content of the attribute 
	 */
	protected String readStringElement(Element el, String attribName) {
		Element aux = el.element(attribName);
		if (aux == null)
			return null;
		return aux.getText();
	}

	
	/**
	 * Read an attribute value as an enumeration value
	 * @param el instance of the {@link Element} containing the attribute 
	 * @param attribName attribute name of the element
	 * @param clazz is the class of the enumeration to be read
	 * @param defaultValue the default value if no value is read
	 * @return the enumeration value or the default value if no value is found
	 */
	protected <E extends Enum> E readEnumAttribute(Element el, String attribName, Class<E> clazz, E defaultValue) {
		String val = el.attributeValue(attribName);
		if (val == null)
			return defaultValue;

		E enumvalue = stringToEnum(clazz, val);
		if (enumvalue == null)
			throw new IllegalArgumentException("Unexpected value for attribute " + attribName + ": " + val);

		return enumvalue;
	}
	
	/**
	 * Return the enumeration value of a string name. The char case of the name
	 * is ignored to compare values
	 * @param clazz is the enumeration class name
	 * @param name is the name of the item 
	 * @return instance of the enumeration element
	 */
	protected <E extends Enum> E stringToEnum(Class<E> clazz, String name) {
		Enum[] values = clazz.getEnumConstants();
		for (Enum val: values) {
			if (val.toString().equalsIgnoreCase(name))
				return (E)val;
		}
		return null;
	}
	
	/**
	 * Read a string attribute of the XML {@link Element} represented by el
	 *  
	 * @param el the XML {@link Element} containing the attribute 
	 * @param attribname the name of the attribute
	 * @return the string value of the attribute
	 */
	protected String readStringAttribute(Element el, String attribname) {
		String s = el.attributeValue(attribname);
		if ((s != null) && (s.trim().isEmpty()))
			s = null;
		return s;
	}
}
