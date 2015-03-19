/**
 * 
 */
package org.msh.etbm.desktop.common;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Simple adapter of the {@link AncestorListener} to avoid some key types and
 * unused methods implementation
 * @author Ricardo Memoria
 *
 */
public class AncestorAdapter implements AncestorListener {

	/** {@inheritDoc}
	 */
	@Override
	public void ancestorAdded(AncestorEvent event) {
	}

	/** {@inheritDoc}
	 */
	@Override
	public void ancestorRemoved(AncestorEvent event) {
	}

	/** {@inheritDoc}
	 */
	@Override
	public void ancestorMoved(AncestorEvent event) {
	}

}
