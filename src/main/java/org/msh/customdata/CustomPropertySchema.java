/**
 * 
 */
package org.msh.customdata;

/**
 * Store metadata information about a custom property
 * 
 * @author Ricardo Memoria
 *
 */
public class CustomPropertySchema {

	private String name;
	private String fieldname;
	private String type;
	private Integer size;
	private boolean required;

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
	/**
	 * @return the fieldname
	 */
	public String getFieldname() {
		return fieldname;
	}
	/**
	 * @param fieldname the fieldname to set
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
}
