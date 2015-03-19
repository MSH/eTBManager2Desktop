package org.msh.etbm.desktop.common;

import javax.swing.JPanel;

import org.msh.eventbus.EventBusService;
import org.msh.eventbus.EventBusListener;

public abstract class ClientPanel extends JPanel implements Refreshable, EventBusListener {
	private static final long serialVersionUID = 303467374405053699L;

	/**
	 * Called when the panel is activated, i.e, it's displayed to the user.
	 * <br/>
	 * A panel may be already created, but activated later or reactivated
	 */
	public void activate() {
		
	}
	
	
	/**
	 * Called when the panel is deactivated (hidden)
	 */
	public void deactivate() {
		
	}

	/**
	 * Panel is being closed, and this method must free some resources
	 */
	public void closing() {
		EventBusService.removeObserverHandler(this);
	}
	
	/**
	 * Called when panel wants to handle specific events
	 * @param event
	 * @param data
	 */
	public void handleEvent(Object event, Object... data) {
		System.out.println("Event " + event + " not handled on " + getClass().toString());
	}
}
