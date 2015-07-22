package org.msh.springframework.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.DeletedEntity;
import org.msh.etbm.entities.Synchronizable;
import org.msh.etbm.entities.SynchronizationData;
import org.msh.springframework.seam.QueryParser;

public class EntityManagerImplWrapper implements EntityManager {

	private EntityManager entityManager;
	
	
	public EntityManagerImplWrapper(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public void clear() {
		entityManager.clear();
	}

	public void close() {
		entityManager.close();
	}

	public boolean contains(Object entity) {
		return entityManager.contains(entity);
	}

	public Query createNamedQuery(String hql) {
		return entityManager.createNamedQuery(hql);
	}

	public Query createNativeQuery(String sql) {
		return entityManager.createNativeQuery(sql);
	}

	public Query createNativeQuery(String arg0, Class arg1) {
		return entityManager.createNativeQuery(arg0, arg1);
	}

	public Query createNativeQuery(String arg0, String arg1) {
		return entityManager.createNativeQuery(arg0, arg1);
	}

	public Query createQuery(String arg0) {
		return createElQuery(arg0);
	}

	public <T> T find(Class<T> arg0, Object arg1) {
		return entityManager.find(arg0, arg1);
	}

	public void flush() {
		entityManager.flush();
	}

	public Object getDelegate() {
		return entityManager.getDelegate();
	}

	public FlushModeType getFlushMode() {
		return entityManager.getFlushMode();
	}

	public <T> T getReference(Class<T> arg0, Object arg1) {
		return entityManager.getReference(arg0, arg1);
	}

	public EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}

	public boolean isOpen() {
		return entityManager.isOpen();
	}

	public void joinTransaction() {
		entityManager.joinTransaction();
	}

	public void lock(Object arg0, LockModeType arg1) {
		entityManager.lock(arg0, arg1);
	}

	public <T> T merge(T arg0) {
		return entityManager.merge(arg0);
	}

	/** {@inheritDoc}
	 */
	public void persist(Object obj) {
		// check if entity must store information about its change
		if ((obj instanceof Synchronizable) && (!App.instance().isSynchronizing())) {
			SynchronizationData data = ((Synchronizable)obj).getSyncData();
			if (data == null)
				data = new SynchronizationData();
			// is an existing entity or a new entity entered by the user (no server id)?
			if ((entityManager.contains(obj)) || (data.getServerId() == null)) {
				data.setChanged(true);
				((Synchronizable)obj).setSyncData(data);
			}
		}
		entityManager.persist(obj);
	}

	/** {@inheritDoc}
	 */
	public void refresh(Object arg0) {
		entityManager.refresh(arg0);
	}

	public void remove(Object obj) {
		// check if it's a synchronized entity
		if ((obj instanceof Synchronizable) && (!App.instance().isSynchronizing())) {
			SynchronizationData data = ((Synchronizable)obj).getSyncData();
			if (data.getServerId() != null) {
				DeletedEntity ent = new DeletedEntity();
				ent.setEntityId(data.getServerId());
				ent.setEntityName(obj.getClass().getSimpleName());
				App.getEntityManager().persist(ent);
			}
		}
		
		entityManager.remove(obj);
	}

	public void setFlushMode(FlushModeType arg0) {
		entityManager.setFlushMode(arg0);
	}

	
	protected Query createElQuery(String ejbql) {
		if ( ejbql.indexOf('#')>0 )
		{
			QueryParser qp = new QueryParser(ejbql);
			Query query = entityManager.createQuery( qp.getEjbql() );
			for (int i=0; i<qp.getParameterValueBindings().size(); i++)
			{
				query.setParameter( 
						QueryParser.getParameterName(i), 
						qp.getParameterValueBindings().get(i).getValue() 
						);
			}
			return query;
		}
		else
		{
			return entityManager.createQuery(ejbql);
		}
	}
}
