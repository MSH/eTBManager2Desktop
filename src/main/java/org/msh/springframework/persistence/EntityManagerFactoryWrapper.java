package org.msh.springframework.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerFactoryWrapper implements EntityManagerFactory {

	private EntityManagerFactory delegate;
	
	public EntityManagerFactoryWrapper(EntityManagerFactory delegate) {
		super();
		this.delegate = delegate;
	}

	public void close() {
		delegate.close();
	}

	public EntityManager createEntityManager() {
		return new EntityManagerImplWrapper( delegate.createEntityManager() );
	}

	public EntityManager createEntityManager(Map arg0) {
		return new EntityManagerImplWrapper( delegate.createEntityManager(arg0) );
	}

	public boolean isOpen() {
		return delegate.isOpen();
	}
}
