package org.msh.etbm.services.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Synchronizable;
import org.msh.etbm.entities.SynchronizableEntity;
import org.msh.etbm.entities.SynchronizationData;
import org.msh.etbm.entities.WSObject;
import org.msh.etbm.entities.Workspace;
import org.msh.xview.FormContext;

/**
 * Default implementation of the {@link EntityServices} interface
 * 
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public class EntityServicesImpl<E> implements EntityServices<E> {

	private Class entityClass;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.msh.xview.VariableController#validate(org.msh.xview.FormContext)
	 */
	@Override
	public boolean validate(E entity, FormContext form) {
		return true;
	}


	/**
	 * Update entity status indicating it must be synchronized with the server
	 * @param ent instance of {@link SynchronizableEntity}
	 */
	protected void setEntityToSync(Synchronizable ent) {
		SynchronizationData data = ent.getSyncData();
		if (data == null) {
			data = new SynchronizationData();
			ent.setSyncData(data);
		}
		data.setChanged(true);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.msh.xview.VariableController#save(org.msh.xview.FormContext)
	 */
	@Override
	public void save(E entity) {
		EntityManager em = App.getEntityManager();
		if (entity instanceof WSObject) {
			WSObject wsobj = (WSObject) entity;
			if (wsobj.getWorkspace() == null) {
				Workspace ws = (Workspace) App.getEntityManager().merge(
						App.getComponent("defaultWorkspace"));
				wsobj.setWorkspace(ws);
			}
		}

		// update syncronization status
		if (entity instanceof SynchronizableEntity) {
			SynchronizableEntity syncEnt = (SynchronizableEntity)entity;
			setEntityToSync(syncEnt);
		}
		
		em.persist(entity);
		em.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.msh.xview.VariableController#refresh()
	 */
/*	@Override
	public void refresh() {
		if (getId() == null)
			return;
		Object entityId = getId();
		setId(entityId);
		setInstance(App.getEntityManager().merge(getInstance()));
	}
*/

	/** {@inheritDoc}
	 */
	@Override
	public Class<E> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				if (paramType.getActualTypeArguments().length == 2) {
					// likely dealing with -> new
					// EntityHome<Person>().getEntityClass()
					if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
						throw new IllegalArgumentException(
								"Could not guess entity class by reflection");
					}
					// likely dealing with -> new Home<EntityManager, Person>()
					// { ... }.getEntityClass()
					else {
						entityClass = (Class<E>) paramType
								.getActualTypeArguments()[1];
					}
				} else {
					// likely dealing with -> new PersonHome().getEntityClass()
					// where PersonHome extends EntityHome<Person>
					entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
				}
			} else {
				throw new IllegalArgumentException(
						"Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServices#getInstance()
	 */
	@Override
	public E findEntity(Object id) {
		return App.getEntityManager().find(getEntityClass(), id);
	}
	
	
	/**Create an instance of the entity
	 * @return
	 */
	protected E createEntityInstance()  {
		try {
			Class clazz = getEntityClass();
			return (E)clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServices#delete()
	 */
	@Override
	public void delete(Object id) {
		/*getEntityManager()
			.createQuery("delete from " + getEntityClass().getName() + " where id = :id")
			.setParameter("id", id)
			.executeUpdate();*/

		Object o = getEntityManager()
					.createQuery(" from " + getEntityClass().getName() + " where id = :id")
					.setParameter("id", id)
					.getSingleResult();
		getEntityManager().remove(o);
	}

	
	/**
	 * Return the instance of the {@link EntityManager} in use
	 * @return
	 */
	protected EntityManager getEntityManager() {
		return App.getEntityManager();
	}

	/** {@inheritDoc}
	 */
	@Override
	public E newEntity() {
		try {
			Class<E> clazz = getEntityClass();
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
