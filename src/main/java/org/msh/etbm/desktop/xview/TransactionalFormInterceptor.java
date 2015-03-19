/**
 * 
 */
package org.msh.etbm.desktop.xview;

import javax.persistence.EntityManager;

import org.hibernate.proxy.HibernateProxy;
import org.msh.etbm.desktop.app.App;
import org.msh.xview.FormContext;
import org.msh.xview.FormInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Form interceptor that execute form updates inside a transaction and merge all entities
 * in the current {@link EntityManager}
 * @author Ricardo Memoria
 *
 */
public class TransactionalFormInterceptor extends DefaultTransactionDefinition implements FormInterceptor {
	private static final long serialVersionUID = -8663087363381421834L;

	private PlatformTransactionManager txManager;
	private TransactionStatus status;

	/** {@inheritDoc}
	 */
	@Override
	public void beforeFormUpdate(FormContext formContext) {
		txManager = (PlatformTransactionManager) App.getComponent("transactionManager");
		status = txManager.getTransaction(this);
		EntityManager em = App.getEntityManager();
		for (String varname: formContext.getDataModel().getVariables()) {
			Object variable = formContext.getDataModel().getValue(varname);
			if (variable instanceof HibernateProxy)
				em.merge(variable);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void afterFormUpdate(FormContext formContext) {
		txManager.commit(status);
	}

}
