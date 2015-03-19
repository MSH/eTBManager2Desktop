package org.msh.springframework.persistence;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Abstract class with support for an easier implementation of transaction callback
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public abstract class ActionCallback<E> implements TransactionCallback{
	private E data;

	public ActionCallback(E data) {
		super();
		this.data = data;
	}

	public ActionCallback() {
		super();
	}

	public abstract void execute(E data);
	
	@Override
	public Object doInTransaction(TransactionStatus status) {
		execute(data);
		return null;
	}
}