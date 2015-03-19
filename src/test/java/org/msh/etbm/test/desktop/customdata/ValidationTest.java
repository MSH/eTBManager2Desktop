/**
 * 
 */
package org.msh.etbm.test.desktop.customdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.msh.customdata.CustomObjectDAO;
import org.msh.customdata.CustomObjectSchema;
import org.msh.customdata.CustomPropertySchema;
import org.msh.customdata.SchemaConfiguration;
import org.msh.customdata.ValidationException;

/**
 * Validate the custom properties of an object. The validation is done by
 * creating a custom object schema {@link CustomObjectSchema}. Once it is
 * defined and included in the schema configuration {@link SchemaConfiguration},
 * objects can be validated using the {@link CustomObjectDAO#validate(org.msh.customdata.CustomObject) method}
 * <p/>
 * 
 * In this test, it's used a very simple customer class, and 3 properties are
 * included in the objects and validated against its schema.
 * 
 * @author Ricardo Memoria
 *
 */
public class ValidationTest {

	// the object schema
	private CustomObjectSchema schema;


	/**
	 * Simple validation of the properties
	 */
	@Test
	public void testSimpleValidation() {
		createSchema();

		Customer customer = new Customer();
		customer.getCustomProperties().setValue("email", "ricardo@rmemoria.com.br");
		customer.getCustomProperties().setValue("age", 20);
		customer.getCustomProperties().setValue("registrationRequired", true);
		CustomObjectDAO dao = new CustomObjectDAO();
		dao.validate(customer);
	}


	/**
	 * Check required fields
	 */
	@Test(expected=ValidationException.class)
	public void testRequiredField() {
		createSchema();

		Customer customer = new Customer();
		// e-mail is not informed
		customer.getCustomProperties().setValue("age", 20);
		customer.getCustomProperties().setValue("registrationRequired", true);
		CustomObjectDAO dao = new CustomObjectDAO();
		dao.validate(customer);
	}


	/**
	 * Test properties that are not valid
	 */
	@Test(expected=ValidationException.class)
	public void testInvalidProperty() {
		createSchema();

		Customer customer = new Customer();
		customer.getCustomProperties().setValue("email", "ricardo@rmemoria.com.br");
		customer.getCustomProperties().setValue("invalid-property", 1);
		CustomObjectDAO dao = new CustomObjectDAO();
		dao.validate(customer);
	}


	/**
	 * Test removing properties from the custom object
	 */
	@Test
	public void clearTest() {
		createSchema();

		Customer customer = new Customer();
		customer.getCustomProperties().setValue("email", "ricardo@rmemoria.com.br");
		customer.getCustomProperties().setValue("prop2", 50.0f);
		customer.getCustomProperties().setValue("prop3", new Integer(10));
		customer.getCustomProperties().setValue("prop4", "this is a simple property");

		// check if values are there
		assertEquals(50.0f, customer.getCustomProperties().getValue("prop2"));
		assertEquals(new Integer(10), customer.getCustomProperties().getValue("prop3"));

		// check size of the properties
		assertEquals(customer.getCustomProperties().getProperties().size(), 4);
		
		customer.getCustomProperties().deleteProperty("prop4");
		assertEquals(customer.getCustomProperties().getProperties().size(), 3);
		assertNull(customer.getCustomProperties().getValue("prop4"));

		// remove all properties
		customer.getCustomProperties().clear();
		assertEquals(customer.getCustomProperties().getProperties().size(), 0);
	}


	/**
	 * Create a basic schema
	 */
	protected void createSchema() {
		schema = new CustomObjectSchema();
		schema.setTableName("customerxx");
		schema.setParentTable("customer");
		schema.setObjectClassName(Customer.class.getName());
		schema.setObjectClass(Customer.class);
		SchemaConfiguration.instance().addSchema(schema);
		
		CustomPropertySchema prop = new CustomPropertySchema();
		prop.setFieldname("email");
		prop.setName("email");
		prop.setType("string");
		prop.setSize(100);
		prop.setRequired(true);
		schema.getProperties().add(prop);

		prop = new CustomPropertySchema();
		prop.setFieldname("age");
		prop.setName("age");
		prop.setType("int");
		schema.getProperties().add(prop);

		prop = new CustomPropertySchema();
		prop.setFieldname("registrationRequired");
		prop.setName("registrationRequired");
		prop.setType("boolean");
		schema.getProperties().add(prop);
	}
}
