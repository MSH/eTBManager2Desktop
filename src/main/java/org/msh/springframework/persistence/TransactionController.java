/**
 * 
 */
package org.msh.springframework.persistence;

import org.msh.etbm.desktop.app.App;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Class that controls the life cycle of a JPA transaction
 * 
 * @author Ricardo Memoria
 *
 */
public class TransactionController extends DefaultTransactionDefinition {
	private static final long serialVersionUID = 5261903547264674905L;

	private JpaTransactionManager tx;
	private TransactionStatus status;
	
	
	/**
	 * Return true if the transaction is active
	 * @return boolean value
	 */
	public boolean isActive() {
		return status != null;
	}

	/**
	 * start a new database transaction
	 */
	public void startTransaction() {
		if (status != null)
			throw new IllegalAccessError("Transaction was already started");
		status = getTransactionManager().getTransaction(this);
	}
	
	
	/**
	 * Commit a transaction
	 */
	public void commitTransaction() {
		if (status == null)
			throw new IllegalAccessError("No on-going status");
		getTransactionManager().commit(status);
		status = null;
	}
	
	/**
	 * Rollback an on-going transaction
	 */
	public void rollbackTransaction() {
		if (status == null)
			throw new IllegalAccessError("No on-going status");
		getTransactionManager().rollback(status);
		status = null;
	}
	
	/**
	 * Return the transaction manager in use
	 * @return instance of {@link JpaTransactionManager}
	 */
	protected JpaTransactionManager getTransactionManager() {
		if (tx == null) {
			tx = (JpaTransactionManager) App.getComponent("transactionManager");
		}
		return tx;
	}
}
