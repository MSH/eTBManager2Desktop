package org.msh.springframework.seam;

import org.msh.etbm.desktop.app.App;

/**
 * Base class for controller objects which require a persistence
 * context object.
 * 
 * @author Gavin King
 *
 * @param <T> the persistence context class (eg. Session or EntityManager)
 */
public abstract class PersistenceController<T> 
{
   private transient T persistenceContext;
   
   public T getPersistenceContext()
   {
      if (persistenceContext==null)
      {
         persistenceContext = (T) getComponentInstance( getPersistenceContextName() );
      }
      return persistenceContext;
   }

   protected Object getComponentInstance(String name) {
	   return App.getComponent(name);
   }
   
   public void setPersistenceContext(T persistenceContext)
   {
      this.persistenceContext = persistenceContext;
   }

   protected abstract String getPersistenceContextName();

}
