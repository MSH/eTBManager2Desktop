package org.msh.xview.swing.event;

import org.msh.xview.swing.ui.ViewUI;

/**
 * Data structure with information about a form event
 * 
 * @author Ricardo Memoria
 *
 */
public class FormEvent {

	private ViewUI view;
	private Object data;
	private String event;
	
	/**
	 * Default constructor
	 * @param view is the instance of {@link ViewUI} that generated the event
	 * @param data is the data object related to the view (may be null depending on the view)
	 * @param event is the name of the event generated
	 */
	public FormEvent(ViewUI view, Object data, String event) {
		super();
		this.view = view;
		this.data = data;
		this.event = event;
	}

	/**
	 * @return the view
	 */
	public ViewUI getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(ViewUI view) {
		this.view = view;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}
}
