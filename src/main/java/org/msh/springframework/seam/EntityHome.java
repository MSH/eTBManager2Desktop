package org.msh.springframework.seam;

import javax.persistence.EntityManager;



/**
 * Base class for Home objects of JPA entities.
 * 
 * @author Gavin King
 *
 */
public class EntityHome<E> extends Home<EntityManager, E>
{
   private static final long serialVersionUID = -3140094990727574632L;
   
   
   /**
    * Returns true if the entity instance is managed
    */
   public boolean isManaged()
   {
      return getInstance()!=null && 
            getEntityManager().contains( getInstance() );
   }

   /**
    * Flush any changes made to the managed entity instance to the underlying
    * database. 
    * <br />
    * If the update is successful, a log message is printed, a 
    * {@link javax.faces.application.FacesMessage} is added and a transaction 
    * success event raised.
    * 
    * @see Home#updatedMessage()
    * @see Home#raiseAfterTransactionSuccessEvent()
    * 
    * @return "updated" if the update is successful
    */
   public String update()
   {
      joinTransaction();
      getEntityManager().flush();
      updatedMessage();
      raiseAfterTransactionSuccessEvent();
      return "updated";
   }
   
   /**
    * Persist unmanaged entity instance to the underlying database. 
    * If the persist is successful, a log message is printed, a 
    * {@link javax.faces.application.FacesMessage } is added and a transaction 
    * success event raised.
    * 
    * @see Home#createdMessage()
    * @see Home#raiseAfterTransactionSuccessEvent()
    * 
    * @return "persisted" if the persist is successful
    */
   public String persist()
   {
      getEntityManager().persist( getInstance() );
      getEntityManager().flush();
//      assignId( PersistenceProvider.instance().getId( getInstance(), getEntityManager() ) );
      createdMessage();
      raiseAfterTransactionSuccessEvent();
      return "persisted";
   }
   
   /**
    * Remove managed entity instance from the Persistence Context and the 
    * underlying database.
    * If the remove is successful, a log message is printed, a 
    * {@link javax.faces.application.FacesMessage} is added and a transaction 
    * success event raised.
    * 
    * @see Home#deletedMessage()
    * @see Home#raiseAfterTransactionSuccessEvent()
    * 
    * @return "removed" if the remove is successful
    */
   public String remove()
   {
      getEntityManager().remove( getInstance() );
      getEntityManager().flush();
      deletedMessage();
      raiseAfterTransactionSuccessEvent();
      return "removed";
   }
   
   /**
    * Implementation of {@link Home#find() find()} for JPA
    * 
    * @see Home#find()
    */
   @Override
   public E find()
   {
      if (getEntityManager().isOpen())  
      {
         E result = loadInstance();
         if (result==null) 
         {
            result = handleNotFound();
         }
         return result;
      }
      else 
      {
         return null;
      }
   }

   /**
    * Utility method to load entity instance from the {@link EntityManager}. 
    * Called by {@link #find()}.
    * <br />
    * Can be overridden to support eager fetching of associations.
    * 
    * @return The entity identified by {@link Home#getEntityClass() getEntityClass()}, 
    * {@link Home#getId() getId()}
    */
   protected E loadInstance() 
   {
      return getEntityManager().find(getEntityClass(), getId());
   }

   /**
    * Implementation of {@link Home#joinTransaction() joinTransaction()} for
    * JPA.
    */
   @Override
   protected void joinTransaction()
   {
/*      if ( getEntityManager().isOpen() )
      {
         try
         {
            Transaction.instance().enlist( getEntityManager() );
         }
         catch (SystemException se)
         {
            throw new RuntimeException("could not join transaction", se);
         }
      }
*/   }

   /**
    * The Seam Managed Persistence Context used by this Home component
    */
   public EntityManager getEntityManager()
   {
      return getPersistenceContext();
   }
   
   /**
    * The Seam Managed Persistence Context used by this Home component.
    */
   public void setEntityManager(EntityManager entityManager)
   {
      setPersistenceContext(entityManager);
   }
   
   /**
    * The name the Seam component managing the Persistence Context.
    * <br />
    * Override this or {@link #getEntityManager()} if your persistence context
    * is not named <code>entityManager</code>.
    */
   @Override
   protected String getPersistenceContextName()
   {
      return "entityManager";
   }
   
   /**
    * Implementation of {@link Home#getEntityName() getEntityName()} for JPA
    * 
    * @see Home#getEntityName()
    */
   @Override
   protected String getEntityName()
   {
	   return "entityManager";
/*      try
      {
         return PersistenceProvider.instance().getName(getInstance(), getEntityManager());
      }
      catch (IllegalArgumentException e) 
      {
         // Handle that the passed object may not be an entity
         return null;
      }
*/   }
   
}
