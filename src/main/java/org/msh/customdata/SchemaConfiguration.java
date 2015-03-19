/**
 * 
 */
package org.msh.customdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Store information about the configuration of the custom data. It contains
 * type handlers and custom object schemas
 * 
 * @author Ricardo Memoria
 *
 */
public class SchemaConfiguration {
	
	// instantiate the singleton class
	private static final SchemaConfiguration _instance = new SchemaConfiguration();

	private List<TypeHandler> typeHandlers = new ArrayList<TypeHandler>();
	private List<CustomObjectSchema> schemas = new ArrayList<CustomObjectSchema>();

	
	/**
	 * Default and private constructor, to garantee its a singleton
	 */
	private SchemaConfiguration() {
		super();
	}
	
	/**
	 * Add a type handler to the list of handlers
	 * @param typeHandler implementation of the {@link TypeHandler} interface
	 */
	public void addTypeHandler(TypeHandler typeHandler) {
		typeHandlers.add(typeHandler);
	}
	
	/**
	 * Add a new schema to the list of schemas
	 * @param schema instance of {@link CustomObjectSchema}
	 */
	public void addSchema(CustomObjectSchema schema) {
		schemas.add(schema);
	}
	
	/**
	 * Search a custom object schema from its schema ID
	 * @param schemaid
	 * @return instance of {@link CustomObjectSchema} or null if not found
	 */
	public CustomObjectSchema getSchema(String schemaid) {
		for (CustomObjectSchema schema: schemas) {
			if (schema.getId().equals(schemaid)) {
				return schema;
			}
		}
		return null;
	}

	
	/**
	 * Search for a custom object schema by its object class
	 * @param clazz is the class of the object that contains the custom properties
	 * @return instance of {@link CustomObjectSchema} or null if not found
	 */
	public CustomObjectSchema getSchemaByObjectClass(Class clazz) {
		for (CustomObjectSchema schema: schemas) {
			if (schema.getObjectClass() == clazz) {
				return schema;
			}
		}
		return null;
	}
	
	/**
	 * Return the singleton instance of the class
	 * @return
	 */
	public static final SchemaConfiguration instance() {
		return _instance;
	}
}
