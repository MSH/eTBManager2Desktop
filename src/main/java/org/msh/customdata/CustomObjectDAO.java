/**
 * 
 */
package org.msh.customdata;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.msh.etbm.desktop.app.App;




/**
 * Default data access object for CRUD operations in custom objects
 *  
 * @author Ricardo Memoria
 *
 */
public class CustomObjectDAO {

	/**
	 * Save the content of the object
	 * @param obj
	 */
	public void save(CustomObject obj) {
		CustomProperties data = obj.getCustomProperties();

		// check if there is any change
		StateControl state = (StateControl)data;
		
		if (!state.isNew()) {
			boolean changed = state != null ? state.isChanged(): true;
			if (changed) {
				update(obj);
			}
		}
		else {
			insert(obj);
			state.setNew(false);
		}

		// update object state
		state.commitChanges();
	}
	
	/**
	 * Insert the properties of the custom object in the database 
	 * @param id is the primary key to be used in the custom object
	 * @param obj
	 */
	protected void insert(CustomObject obj) {
		CustomObjectSchema schema = findSchema(obj);
		validate(obj);

		// create the SQL query
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + schema.getTableName());
		sql.append(" (id");
		String sqlValues = "";
		for (CustomPropertySchema prop: schema.getProperties()) {
			sql.append(',');
			sqlValues += ',';
			sql.append(prop.getFieldname());

			sqlValues += ":" + prop.getFieldname();
		}
		sql.append(") VALUES (:id");
		sql.append(sqlValues);
		sql.append(')');
		EntityManager em = App.getEntityManager();
		Query qry = em.createNativeQuery(sql.toString());

		CustomProperties data = obj.getCustomProperties();

		// set the values of the properties
		qry.setParameter("id", obj.getCustomPropertiesId());
		for (CustomPropertySchema prop: schema.getProperties()) {
			Object value = data.getValue(prop.getFieldname());
			qry.setParameter(prop.getFieldname(), value);
		}

		// execute the query
		qry.executeUpdate();
	}
	
	
	/**
	 * Update the information of the custom properties in the database table
	 * @param obj
	 */
	protected void update(CustomObject obj) {
		CustomObjectSchema schema = findSchema(obj);
		validate(obj);

		// create the SQL UPDATE instruction
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(schema.getTableName());
		sql.append(" SET ");

		boolean bFirst = true;
		for (CustomPropertySchema prop: schema.getProperties()) {
			if (bFirst) {
				bFirst = false;
			}
			else {
				sql.append(',');
			}
			sql.append(prop.getFieldname());
			sql.append("=:");
			sql.append(prop.getFieldname());
		}
		sql.append(" WHERE id=:id");
		
		// create query
		CustomProperties data = obj.getCustomProperties();
		Query query = App.getEntityManager().createNativeQuery(sql.toString());
		query.setParameter("id", obj.getCustomPropertiesId());
		for (CustomPropertySchema prop: schema.getProperties()) {
			query.setParameter(prop.getFieldname(), data.getValue(prop.getName()));
		}
		query.executeUpdate();
	}
	
	
	/**
	 * Delete the information of the custom properties from the database
	 * @param obj
	 */
	public void delete(CustomObject obj) {
		CustomObjectSchema schema = findSchema(obj);

		// create the SQL DELETE instruction
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(schema.getTableName());
		sql.append(" WHERE id=:id");
		
		Query query = App.getEntityManager().createNativeQuery(sql.toString());
		query.setParameter("id", obj.getCustomPropertiesId());
		query.executeUpdate();
	}
	
	
	/**
	 * Load information from the table to the object properties
	 * @param obj instance of the custom object to load information from
	 */
	public void load(CustomObject obj) {
		CustomObjectSchema schema = findSchema(obj);

		// create SQL SELECT instruction
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		boolean bFirst = true;
		for (CustomPropertySchema prop: schema.getProperties()) {
			if (bFirst) {
				bFirst = false;
			}
			else {
				sql.append(',');
			}
			sql.append(prop.getFieldname());
		}
		sql.append(" FROM ");
		sql.append(schema.getTableName());
		sql.append(" WHERE id=:id");

		// load data from table
		Query query = App.getEntityManager().createNativeQuery(sql.toString());
		query.setParameter("id", obj.getCustomPropertiesId());
		List<Object[]> lst = query.getResultList();

		CustomProperties data = obj.getCustomProperties();
		data.clear();
		if (lst.size() > 0) {
			int index = 0;
			Object[] values = lst.get(0);
			for (CustomPropertySchema prop: schema.getProperties()) {
				Object value = values[index];
				data.setValue(prop.getName(), value);
				index++;
			}
		}
		
		// check if there is any change
		StateControl state = (StateControl)data;
		state.setNew(false);
	}
	
	
	/**
	 * Validate custom properties of the object
	 * @param schemaId
	 * @param obj
	 */
	public void validate(CustomObject obj) {
		CustomObjectSchema schema = findSchema(obj);

		CustomProperties data = obj.getCustomProperties();
		
		// check if properties in the object are correct
		for (String propname: data.getProperties()) {
			CustomPropertySchema propSchema = schema.getPropertyByName(propname);
			if (propSchema == null) {
				throw new ValidationException(propname, "Invalid property");
			}
		}
		
		// check required values
		for (CustomPropertySchema propSchema: schema.getProperties()) {
			if (propSchema.isRequired()) {
				Object value = data.getValue(propSchema.getName());
				if (value == null) {
					throw new ValidationException(propSchema.getName(), "Value cannot be null");
				}
			}
		}
	}
	
	
	/**
	 * Search an object schema by its class.
	 * @param obj is the object to search a schema class from
	 * @return instance of {@link CustomObjectSchema}. If schema is not found, an exception is thrown
	 */
	protected CustomObjectSchema findSchema(Object obj) {
		CustomObjectSchema schema = SchemaConfiguration.instance().getSchemaByObjectClass(obj.getClass());
		if (schema == null) {
			throw new RuntimeException("No schema found for class " + obj.getClass());
		}
		return schema;
	}
}
