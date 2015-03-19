package org.msh.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event bus service that register observers to specific events
 * 
 * @author Ricardo Memoria
 *
 */
public class EventBusService {

	private static final EventBusService instance = createBusService();
	
	private Map<Object, List<EventBusListener>> observers = new HashMap<Object, List<EventBusListener>>();

	/**
	 * Singleton constructor of the observers
	 * @return
	 */
	private static EventBusService createBusService() {
		return new EventBusService();
	}

	
	/**
	 * Add a new observer to the specified event 
	 * @param event
	 * @param handler
	 */
	private void addObserver(Object event, EventBusListener handler) {
		List<EventBusListener> handlers = observers.get(event);
		if (handlers == null)
			handlers = new ArrayList<EventBusListener>();
		handlers.add(handler);
		observers.put(event, handlers);
	}
	
	/**
	 * Remove an observer from the list of event handlers
	 * @param handler
	 */
	private void removeObserver(EventBusListener handler) {
		for (Object event: observers.keySet()) {
			List<EventBusListener> handlers = observers.get(event);
			handlers.remove(handler);
		}
	}

	
	/**
	 * Notify all observers about an specific event
	 * @param event
	 * @param data
	 */
	private void notifyObservers(Object event, Object... data) {
		List<EventBusListener> handlers = observers.get(event);
		if (handlers == null)
			return;

		for (EventBusListener handler: handlers) {
			handler.handleEvent(event, data);
		}
	}
	
	/**
	 * Register a new event handler to observe to an specific event
	 * @param event
	 * @param handler
	 */
	public static void observeEvent(Object event, EventBusListener handler) {
		instance.addObserver(event, handler);
	}
	
	/**
	 * Remove observer from an event handler
	 * @param handler
	 */
	public static void removeObserverHandler(EventBusListener handler) {
		instance.removeObserver(handler);
	}
	
	/**
	 * Raise an event including the specified data on that
	 * @param event
	 * @param data
	 */
	public static void raiseEvent(Object event, Object... data) {
		instance.notifyObservers(event, data);
	}
}
