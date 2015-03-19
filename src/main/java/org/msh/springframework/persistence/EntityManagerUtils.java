package org.msh.springframework.persistence;

import javax.persistence.EntityManager;

import org.hibernate.proxy.HibernateProxy;
import org.msh.etbm.desktop.app.App;
import org.msh.xview.FormContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Utilities functions to support transaction and shared entity manager operations
 * @author Ricardo Memoria
 *
 */
public class EntityManagerUtils {

	private static int counter;
	
	/**
	 * Execute a code inside a transaction
	 * @param callback
	 */
	static public void doInTransaction(ActionCallback callback) {
		JpaTransactionManager tx = (JpaTransactionManager) App.getComponent("transactionManager");
		TransactionTemplate tmp = new TransactionTemplate(tx);
		System.out.println("Start transaction " + ++counter);
		tmp.execute(callback);
		System.out.println("Finish transaction " + counter--);
	}
	
	/**
	 * Look for variables of the form that are hibernate entities, and 
	 * merge then into the current {@link EntityManager}
	 * @param frm form to search for variables
	 */
	static public void refreshFormVariables(FormContext frm) {
		for (String varname: frm.getDataModel().getVariables()) {
			Object obj = frm.getDataModel().getValue(varname);
			if (obj instanceof HibernateProxy) {
				obj = App.getEntityManager().merge(obj);
				frm.getDataModel().setValue(varname, obj);
			}
		}
	}
}
