/**
 * 
 */
package org.msh.etbm.test.desktop.customdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.msh.customdata.CustomObjectDAO;
import org.msh.customdata.CustomObjectSchema;
import org.msh.customdata.CustomPropertySchema;
import org.msh.customdata.SchemaConfiguration;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.test.desktop.AppTestStartup;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * This is a test of the custom property library. The custom property allows an
 * object to include extra properties not declared in the class by using an
 * object to store its property values in a way like a map of string and objects.
 * <p/>
 * 
 * CRUD operations are implemented using the {@link CustomObjectDAO} class.
 * It contains 4 basic operations - load, save, remove and validate.
 * 
 * @author Ricardo Memoria
 *
 */
public class CrudTest {

	// the key of a given case in the database
	public static final int CASE_ID = 1;
	
	private CustomObjectSchema schema;

	// values used along the test
	private String email = "rmemoria@test.com";
	private Integer age = 30;
	private Boolean registrationReq = Boolean.TRUE;

	private String newEmail = "ricardo@terra.com.br";
	private Integer newAge = 20;

	
	/**
	 * Test the insert, load, update and delete operation of the custom object
	 */
	@Test
	public void crudTest() {
		AppTestStartup.instance().initialize();
		createSchema();
		
		// insert value
		insertTest();

		loadTest();
		
		updateTest();
		
		deleteTest();
	}

	
	/**
	 * Test the insert operation
	 */
	public void insertTest() {
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				TbCase tbcase = App.getEntityManager().find(TbCase.class, CASE_ID);
				assertNotNull(tbcase);
				CustomObjectDAO dao = new CustomObjectDAO();

				tbcase.getCustomProperties().setValue("email", email);
				tbcase.getCustomProperties().setValue("age", age);
				tbcase.getCustomProperties().setValue("registrationRequired", registrationReq);
				
				dao.save(tbcase);
			}
		});
	}

	
	/**
	 * Test the operation to load information
	 */
	public void loadTest() {
		// check if value was saved
	 	Object[] vals = (Object[])App.getEntityManager()
			.createNativeQuery("select id, email, age, registrationRequired from tbcasexx where id=:id")
			.setParameter("id", CASE_ID)
			.getSingleResult();
	 	
	 	assertNotNull(vals);
	 	assertEquals(vals.length, 4);
	 	assertEquals(vals[0], CASE_ID);
	 	assertEquals(vals[1], email);
	 	assertEquals(vals[2], age);
	 	assertEquals(vals[3], registrationReq);
	 	
	 	// check if value is properly restored using the CustomObjectDAO
	 	TbCase tbcase = App.getEntityManager().find(TbCase.class, CASE_ID);
	 	CustomObjectDAO dao = new CustomObjectDAO();
	 	dao.load(tbcase);
	 	assertEquals(email, tbcase.getCustomProperties().getValue("email"));
	 	assertEquals(age, tbcase.getCustomProperties().getValue("age"));
	 	assertEquals(registrationReq, tbcase.getCustomProperties().getValue("registrationRequired"));
	}


	/**
	 * Test the update operation, and later load again the entity to check if
	 * fields were really updated
	 */
	public void updateTest() {
		// update entity
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
			 	CustomObjectDAO dao = new CustomObjectDAO();
				TbCase tbcase = App.getEntityManager().find(TbCase.class, CASE_ID);
				dao.load(tbcase);

				tbcase.getCustomProperties().setValue("email", newEmail);
				tbcase.getCustomProperties().setValue("age", newAge);
				dao.save(tbcase);
				
				assertEquals(tbcase.getCustomProperties().getValue("email"), newEmail);
				assertEquals(tbcase.getCustomProperties().getValue("age"), newAge);
			}
		});
		
		// load and check values
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				// check if value is properly restored
			 	TbCase tbcase = App.getEntityManager().find(TbCase.class, CASE_ID);
			 	CustomObjectDAO dao = new CustomObjectDAO();
			 	dao.load(tbcase);
			 	assertEquals(tbcase.getCustomProperties().getValue("email"), newEmail);
			 	assertEquals(tbcase.getCustomProperties().getValue("age"), newAge);
			 	assertEquals(tbcase.getCustomProperties().getValue("registrationRequired"), registrationReq);
			}
		});
	}

	
	/**
	 * Test the exclusion of the property data
	 */
	protected void deleteTest() {
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
			 	CustomObjectDAO dao = new CustomObjectDAO();
				TbCase tbcase = App.getEntityManager().find(TbCase.class, CASE_ID);
				dao.load(tbcase);
				dao.delete(tbcase);
			}
		});
	}


	/**
	 * Create a basic schema. The schema defines the accepted properties of an object
	 */
	protected void createSchema() {
		// check if schema was already created
		if (schema != null) {
			return;
		}

		schema = new CustomObjectSchema();
		schema.setTableName("tbcasexx");
		schema.setParentTable("tbcase");
		schema.setObjectClassName(TbCase.class.getName());
		schema.setObjectClass(TbCase.class);
		SchemaConfiguration.instance().addSchema(schema);
		
		// define the property "email"
		CustomPropertySchema prop = new CustomPropertySchema();
		prop.setFieldname("email");
		prop.setName("email");
		prop.setType("string");
		prop.setSize(100);
		prop.setRequired(true);
		schema.getProperties().add(prop);

		// define the property "age"
		prop = new CustomPropertySchema();
		prop.setFieldname("age");
		prop.setName("age");
		prop.setType("int");
		schema.getProperties().add(prop);

		// define the property "registrationRequired"
		prop = new CustomPropertySchema();
		prop.setFieldname("registrationRequired");
		prop.setName("registrationRequired");
		prop.setType("boolean");
		schema.getProperties().add(prop);
	}
}
