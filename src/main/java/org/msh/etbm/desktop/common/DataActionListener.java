package org.msh.etbm.desktop.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Default implementation of an {@link ActionListener} where a data object is passed as 
 * parameter to be recovered later by the action handler.
 * 
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public abstract class DataActionListener<E> implements ActionListener {

	private E data;
	
	public DataActionListener(E data) {
		this.data = data;
	}
	
	public void actionPerformed(ActionEvent e) {
		actionPerformed(e, data);
	}

	public abstract void actionPerformed(ActionEvent e, E data);
}
