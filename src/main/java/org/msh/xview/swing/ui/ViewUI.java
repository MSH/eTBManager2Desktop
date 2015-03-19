/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.swing.JComponent;

import org.msh.xview.FormDataModel;
import org.msh.xview.FormMessage;
import org.msh.xview.components.XContainer;
import org.msh.xview.components.XValidationRule;
import org.msh.xview.components.XView;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.event.FormEvent;


/**
 * Base class for a representation of an {@link XView} class in a swing application.
 * The ViewUI manage a {@link JComponent} that is used to display the content of
 * its assigned {@link XView} (or other ViewUI objects, when the viewUI is a
 * representation of the {@link ContainerUI}).
 * 
 * @author Ricardo Memoria
 *
 * @param <E> is the type of {@link XView} element that will be handled by the Swing view
 */
public abstract class ViewUI<E extends XView> {
	
	public enum ViewMode {REGULAR, COMPACT};

	// just for the controls that don't have a view
	private String id;
	private E view;
	private ViewUI parent;
	private List<ViewUI> children;

	// used when user force a read only state
	private Boolean readOnly;
	
	// only used if XView#isReadOnly returns null (no expression implemented)
	private ValueExpression expReadOnly;
	
	// only used if XView#isVisible returns null (no expression implemented); 
	private ValueExpression expVisible;

	private List<FormMessage> messages;
	
	private int updatingCount;
	
	// list of validation rules
	private List<ValidationRuleUI> rules;
	
	private ViewMode mode;

	
	/**
	 * Set the {@link XView} instance assigned to this component.
	 * Just called during creation time. 
	 * @param view
	 */
	public void setView(E view) {
		this.view = view;
		mode = ViewMode.REGULAR;
	}
	
	/**
	 * Return the {@link XView} object related to this UI component
	 * @return the instance of {@link XView} class
	 */
	public E getView() {
		return view;
	}

	
	protected abstract void doUpdate();
	
	/**
	 * Update the state of the component. Create the control, update its value, children and content.
	 */
	public void update() {
		// is updating already?
		if (isUpdating()) {
			return;
		}

		// if it's not visible, so it's not necessary to update
		boolean bVisible = isVisible();
		if (!bVisible) {
			return;
		}
		
		
		beginUpdate();
		try {
			if (children != null) {
				for (ViewUI view: children) {
					view.update();
				}
			}
			
			// call the view update
			doUpdate();
			
			// true if all views are being updated
			boolean updateChain = (getParent() != null) && (getParent().isUpdating());
			// if it's not in a update chain, check if state changed
			if (!updateChain) {
				if (bVisible != isVisible()) {
					notifyStateChanged();
				}
			}
		}
		finally {
			finishUpdate();
		}
	};

	
	/**
	 * Start updating the view
	 */
	protected void beginUpdate() {
		updatingCount++;
	}

	/**
	 * Finish updating the view 
	 */
	protected void finishUpdate() {
		updatingCount--;
		if (updatingCount < 0) {
			throw new RuntimeException("No updating started for this end of update");
		}
	}
	
	
	/**
	 * Return true if the view is updating its state (size, position, etc)
	 * @return boolean value
	 */
	public boolean isUpdating() {
		return updatingCount > 0;
	}

	
	/**
	 * Clear all messages displayed by this view and its inner views
	 */
	public void clearMessages() {
		messages = null;
	}

	
	/**
	 * Check the rules during validation of the view
	 * @return true if valid, or false if validation rules failed
	 */
	protected boolean validateRules() {
		initializeRules();
		// validate the rules, if available
		if (rules != null) {
			for (ValidationRuleUI rule: rules) {
				if ((rule.isEnabled()) && (!rule.isValid())) {
					String msg = rule.getMessage();
					if (msg == null)
						 msg = "Validation failed";
					getFormContext().addMessage(this, msg);
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Validate the UI element and return true if it was successfully validated
	 * @return true if the view content is successfully validated
	 */
	public boolean validate() {
		clearMessages();
		
		if (!isVisible()) {
			return true;
		}
		
		boolean result = true;

		// validate the children, if available
		if (children != null) { 
			for (ViewUI<XView> view: children) {
				if (!view.validate()) {
					result = false;
				}
			}
		}
		
		return result;
	}

	/**
	 * Return the ID of the component
	 * @return String value
	 */
	public String getId() {
		return view != null? view.getId(): id;
	}

	
	/**
	 * Change the id of the view, but only if there is no xview assigned to it
	 * @param id the new id of the component, in String type
	 */
	public void setId(String id) { 
		this.id = id;
	}
	
	/**
	 * Return true if this view contains other views, or false if it doesn't store other
	 * views as its children. Default value is false. It returns true if the <code>getChildren()</code>
	 * field is initialized with views
	 * @return boolean value
	 */
	public boolean isContainer() {
		return children != null;
	}


	/**
	 * Set the parent view of this view
	 * @param view instance of {@link ViewUI}
	 */
	protected void setParent(ViewUI view) {
		parent = view;
		handleParentChanged();
	}

	
	/**
	 * Called when a parent component (not directly the own parent) was removed
	 * from the list of components 
	 */
	protected void handleParentChanged() {
		if (!isContainer()) {
			return;
		}
		for (ViewUI view: getChildren()) {
			view.handleParentChanged();
		}
	}
	
	
	/**
	 * Add a child to this view
	 * @param view instance of {@link ViewUI} to be added as a child
	 */
	public void addChild(ViewUI view) {
		if (children == null) {
			children = new ArrayList<ViewUI>();
		}
		
		children.add(view);
		view.setParent(this);
	}


	/**
	 * add a new view to the container
	 * @param index is the 0-based position of the view in the list of children
	 * @param view is the view to be included in the list
	 */
	public void addChild(int index, ViewUI view) {
		if (children == null) {
			children = new ArrayList<ViewUI>();
		}
		
		children.add(index, view);
		view.setParent(this);
	}


	/**
	 * Remove a child view from this view
	 * @param view instance of {@link ViewUI} to be removed from the children list
	 */
	public void removeChild(ViewUI view) {
		if (children == null) {
			return;
		}
		
		children.remove(view);
		view.setParent(null);
	}
	
	/**
	 * Remove all child views
	 */
	public void removeAllChildren() {
		if (children != null) {
			for (ViewUI view: children) {
				view.setParent(null);
			}
			children = null;
		}
	}

	/**
	 * Return the list of child views
	 * @return List of {@link ViewUI} objects
	 */
	public List<ViewUI> getChildren() {
		return children;
	}

	/**
	 * Return the parent view, i.e, the view that this one belongs to
	 * @return the parent view, or null, if it's the root view
	 */
	public ViewUI getParent() {
		return parent;
	}


	/**
	 * Add a message to the component
	 * @param msg the message to be added to the view
	 */
	public FormMessage addMessage(String msg) {
		if (messages == null)
			messages = new ArrayList<FormMessage>();
		
		FormMessage formMsg = new FormMessage(getId(), msg);
		messages.add(formMsg);
		return formMsg;
	}
	
	
	/**
	 * Return the list of messages to be displayed, if any available
	 * @return list of {@link FormMessage} instances
	 */
	public List<FormMessage> getMessages() {
		return messages;
	}
	
	/**
	 * Return true if there are messages to display
	 * @return boolean value
	 */
	public boolean hasMessages() {
		return (messages != null) && (messages.size() > 0);
	}

	/**
	 * Check if component is read-only
	 * @return true if it's a read-only component
	 */
	public boolean isReadOnly() {
		boolean b = false;
		if (getParent() != null) {
			b = getParent().isReadOnly();

			// if it's read-only, it's not necessary to continue
			if (b)
				return b;
		}

		// check if a read only value was set
		if (readOnly != null) {
			return readOnly;
		}
		
		// expression was compiled ?
		if (expReadOnly == null) {
			if (getView() == null) {
				return false;
			}
	
			String exp = getView().getReadOnly();
			if (exp == null)
				return false;
			
			expReadOnly = getDataModel().createValueExpression(exp, Boolean.class);
		}
		
		return (Boolean)expReadOnly.getValue(getELContext());
	}


	/**
	 * Set if the view is read-only or editable
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}


	/**
	 * Return the data model represented by {@link FormDataModel} in use
	 * by this view element. The standard operation is to delegate to
	 * the parent the data model to be used
	 *  
	 * @return instance of the {@link FormDataModel} used by this view
	 */
	public FormDataModel getDataModel() {
		return parent != null? parent.getDataModel(): null;
	}

	
	/**
	 * Return the context to execute the expressions
	 * @return instance of {@link ELContext}
	 */
	public ELContext getELContext() {
		return getDataModel().getELContext();
	}


	/**
	 * Return the root view of this view. In general, the view
	 * returned is the form
	 * 
	 * @return instance of the {@link ViewUI}, which is the root view
	 */
	public ViewUI getRootView() {
		ViewUI view = getParent();

		// if the parent view is null, so it's the root view
		if (view == null)
			return this;

		while (view.getParent() != null)
			view = view.getParent();

		return view;
	}

	
	/**
	 * Check if component is visible. The check is first done in the {@link XView} visible property
	 * (which has the preference). If it returns null value, so the method returns the value of visible variable
	 * @return
	 */
	public boolean isVisible() {
		if ((getParent() != null) && (!getParent().isVisible()))
			return false;

		if (view == null) {
			return true;
		}
		
		if (expVisible == null) {
			// check if there is any expression implemented
			String exp = view.getVisible();
			
			if (exp == null) {
				return true;
			}

			expVisible = getDataModel().createValueExpression(exp, Boolean.class);
		}
		
		return (Boolean)expVisible.getValue(getELContext());
	}

	
	/**
	 * Called when the parent background color changes
	 * @param color is the background color
	 */
	protected void notifyParentBGColorChange(Color color) {
		// do nothing
	}

	
	/**
	 * Create the child views of this container using the information
	 * in the {@link XView} instance of the container 
	 */
	public void createChildren() {
		children = null;
		XView xview = (XView)getView();

		// there is any view structure linked to this container?
		if ((xview == null) || (!(xview instanceof XContainer)))
			return;
		children = new ArrayList<ViewUI>();
		
		XContainer container = (XContainer)xview;

		for (XView childview: container.getViews()) {
			ViewUI viewui = ViewUIFactory.createUI(childview);
			addChild(viewui);
			viewui.createChildren();
		}
	}

	
	/**
	 * Return the instance of the {@link SwingFormContext} in use by this view
	 * @return
	 */
	public SwingFormContext getFormContext() {
		if (getParent() != null)
			return getParent().getFormContext();
		return null;
	}
	
	/**
	 * Search for a component by its id. It also searches for it recursively
	 * @param id
	 * @return the instance of {@link ViewUI} or null if not found
	 */
	public ViewUI findComponentById(String id) {
		if (id.equals(getId()))
			return this;

		// component is a container ?
		if (!isContainer()) {
			return null;
		}

		// search in the child views
		for (ViewUI view: getChildren()) {
			ViewUI v = view.findComponentById(id);
			if (v != null) {
				return v;
			}
		}
		
		return null;
	}


	/**
	 * Does the same as <code>update()</code>, but checks if the component state
	 * change, like dimension or visibility. If it changed, alerts the parent about that
	 */
	public void updateAndNotifyChange() {
		boolean bVisible = isVisible();
		update();
		
		if (isVisible() != bVisible) {
			notifyStateChanged();
		}
	}
	
	/**
	 * Called to notify the parent about changes in the state of the component, 
	 * like size and visibility
	 */
	public void notifyStateChanged() {
		ViewUI parent = getParent();
		if (parent != null) {
			parent.notifyStateChanged();
		}
	}
	
	/**
	 * Update the layout of the component - it's
	 */
	protected void updateLayout() {
		if (children != null) {
			for (ViewUI view: children) {
				view.updateLayout();
			}
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + getView().getId() + "]";
	}


	/**
	 * Return true if the component contain validation rules
	 * @return boolean value
	 */
	public boolean hasValidationRules() {
		return (rules != null) && (rules.size() > 0);
	}
	
	/**
	 * Initialize the list of rules to be validated in the view
	 * @param view instance of {@link XView}
	 */
	protected void initializeRules() {
		E view = getView();
		if ((view.getRules() == null) || (view.getRules().size() == 0)) {
			return;
		}

		// create the rules
		if (rules == null) {
			rules = new ArrayList<ValidationRuleUI>();
		}

		FormDataModel dataModel = getDataModel();
		for (XValidationRule rule: view.getRules()) {
			rules.add(new ValidationRuleUI(dataModel, rule));
		}
	}

	
	/**
	 * Return messages to be displayed to the user
	 * @return String value
	 */
	public String getDisplayMessages() {
		if (!hasMessages()) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		for (FormMessage msg: getMessages()) {
			if (builder.length() > 0) {
				builder.append('\n');
			}
			builder.append(msg.getMessage());
		}
		return builder.toString();
	}

	/**
	 * @return the mode
	 */
	public ViewMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(ViewMode mode) {
		this.mode = mode;
	}
	

	/**
	 * Raise an event that will be sent to the event handlers in the form context
	 * @param event is the name of the event
	 * @param data is the data that this view is handling
	 */
	public void raiseEvent(String event, Object data) {
		FormEvent evt = new FormEvent(this, data, event);
		getFormContext().notifyEvent(evt);
	}
}
