/**
 * 
 */
package org.msh.customdata;

import java.util.HashSet;
import java.util.Set;

/**
 * Store in-memory meta data information about a custom object schema
 * 
 * @author Ricardo Memoria
 *
 */
public class CustomObjectSchema {

	private String id;
	private String tableName;
	private String parentTable;
	private String objectClassName;
	private Class objectClass;
	private Set<CustomPropertySchema> properties = new HashSet<CustomPropertySchema>();

	
	/**
	 * Search for a property by its name
	 * @param propname is the name of the property
	 * @return instance of {@link CustomPropertySchema} related to the given name or null, if not found
	 */
	public CustomPropertySchema getPropertyByName(String propname) {
		for (CustomPropertySchema prop: properties) {
			if (prop.getName().equals(propname)) {
				return prop;
			}
		}
		return null;
	}
	
	/**
	 * @return the properties
	 */
	public Set<CustomPropertySchema> getProperties() {
		return properties;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the parentTable
	 */
	public String getParentTable() {
		return parentTable;
	}

	/**
	 * @param parentTable the parentTable to set
	 */
	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Set<CustomPropertySchema> properties) {
		this.properties = properties;
	}

	/**
	 * @return the objectClassName
	 */
	public String getObjectClassName() {
		return objectClassName;
	}

	/**
	 * @param objectClassName the objectClassName to set
	 */
	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	/**
	 * @return the objectClass
	 */
	public Class getObjectClass() {
		return objectClass;
	}

	/**
	 * @param objectClass the objectClass to set
	 */
	public void setObjectClass(Class objectClass) {
		this.objectClass = objectClass;
	}
}
