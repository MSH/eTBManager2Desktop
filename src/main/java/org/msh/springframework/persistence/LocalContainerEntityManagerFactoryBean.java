package org.msh.springframework.persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.msh.etbm.desktop.databases.DatabaseManager;

public class LocalContainerEntityManagerFactoryBean extends org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean {

	
	/** {@inheritDoc}
	 * @see org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean#createNativeEntityManagerFactory()
	 */
	@Override
	protected EntityManagerFactory createNativeEntityManagerFactory()
			throws PersistenceException {
		// define the path for the database
		Map props = new HashMap();

		// create the database, if necessary, and get the database directory 
		DatabaseManager dbman = DatabaseManager.instance();
		if (!dbman.isDatabaseCreated()) {
			dbman.createDatabase();
		}
		File file = new File (dbman.getDatabaseDir(), "/etbmanager"); 
			
		String s = "jdbc:hsqldb:file:" + file.getAbsolutePath();
		props.put("hibernate.connection.url", s);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory( getPersistenceUnitName(), props );
		return new EntityManagerFactoryWrapper(emf);
	}


}
