/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.List;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXLabel;
import org.msh.xview.components.XView;
import org.msh.xview.swing.XViewUtils;
import org.msh.xview.swing.ui.fields.NumberFieldUI;

/**
 * Define visual swing components from the {@link XView} library
 * 
 * @author Ricardo Memoria
 *
 */
public abstract class ComponentUI<E extends XView> extends ViewUI<E>{

	private static final int MESSAGE_PADDING = 4;

	private JComponent component;
	private JXLabel lblMessages;
	
	private Boolean forceNewRow;

	
	/**
	 * Create the instance of the swing component
	 * @return
	 */
	protected abstract JComponent createComponent();
	
	/**
	 * Return the swing component that will be used to display its
	 * content in a form 
	 * @return
	 */
	public JComponent getComponent() {
		if (component == null) {
			initializeComponent();
		}
		return component;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void setParent(ViewUI parent) {
		super.setParent(parent);
		// if the view parent is changed, so the component 
		// must be remove from the parent as well
		disposeComponent();
	}
	
	
	/** {@inheritDoc}
	 */
	@Override
	protected void handleParentChanged() {
		super.handleParentChanged();
		disposeComponent();
	}
	
	/**
	 * Dispose the component, removing it from the parent and
	 * forcing the view to create it again
	 */
	protected void disposeComponent() {
		if (component != null) {
			if (component.getParent() != null) {
				component.getParent().remove(component);
			}
			
			component = null;
		}

		// is message label displayed ?
		if (lblMessages != null) {
			// remove it from the form
			if (lblMessages.getParent() != null) {
				component.getParent().remove(lblMessages);
			}
			lblMessages = null;
		}
	}


	/**
	 * Wrap method to <code>XView.isForceNewRow()</code>, but check if the view is null
	 * @return true if view will start in a new line
	 */
	public boolean isForceNewRow() {
		if (forceNewRow != null) {
			return forceNewRow;
		}

		return getView() != null? getView().isForceNewRow(): false;
	}

	
	/**
	 * Indicate if component must force a new row, or null to use the
	 * value from the view
	 * @param value is a boolean value
	 */
	public void setForceNewRow(Boolean value) {
		this.forceNewRow = value;
	}
	
	
	/**
	 * Wrap method to <code>XView.getColSpan()</code>
	 * @return the column span of getView(), or 1 if getView() returns null
	 */
	public int getColSpan() {
		return getView() != null? getView().getColSpan(): 1;
	}
	

	/**
	 * Create a list of child view components to be displayed by its component 
	 * @return List of {@link ComponentUI} objects
	 */
	public List<ComponentUI> createChildComponentList() {
		return XViewUtils.createChildComponentList(this);
	}
	
	
	/**
	 * Initialize the component, creating it and adding it to its parent
	 */
	protected void initializeComponent() {
		JComponent parentcomp = getParentComponent();
		if ((parentcomp == null) && (getParent() != null)) {
			throw new RuntimeException("No parent component defined to viewUI " + getId());
		}

		component = createComponent();
		if (parentcomp != null) {
			parentcomp.add(component);
		}
	}

	
	/**
	 * Return the parent component where this view component will lay on
	 * @return instance of {@link JComponent}
	 */
	protected JComponent getParentComponent() {
		ViewUI view = getParent();
		while (view != null) {
			if (view instanceof ComponentUI) {
				return ((ComponentUI)view).getComponent();
			}
			view = view.getParent();
		}
		return null;
	}

	
	
	/**
	 * Set the width of the view
	 * @param width is the new width of the component
	 */
	public abstract void setWidth(int width);
	
	/**
	 * Return the width of the view in pixels
	 * @return int value
	 */
	public int getWidth() {
		return getComponent().getWidth();
	};
	

	/**
	 * Return the preferred width of the component
	 * @return int value
	 */
	public int getPreferredWidth() {
		return (int)getComponent().getPreferredSize().getWidth();
	}
	
	/**
	 * Return the minimum width of the component
	 * @return minimum width in pixels
	 */
	public int getMinWidth() {
		return (int)getComponent().getMinimumSize().getWidth();
	}
	
	/**
	 * Return the maximum width of the component. If 0 is returned, this
	 * information will be ignored
	 * @return the maximum width in pixels, or 0 if there is no maximum width
	 */
	public int getMaxWidth() {
		return (int)getComponent().getMaximumSize().getWidth();
	}

	
	/**
	 * Return true if a point is inside a view
	 * @param x is the horizontal position in relation to the component container of the view
	 * @param y is the vertical position in relation to the component container of the view
	 * @return true if the point is inside the view
	 */
	public boolean isPointInView(int x, int y) {
		Point p = getComponent().getLocation();
		return ( (x >= p.getX()) && (x <= p.getX() + getWidth()) &&
				 (y >= p.getY()) && (y <= p.getY() + getHeight()) );
	}

	
	protected abstract void doComponentUpdate();


    @Override
    protected boolean isComponentVisible() {
        JComponent comp = getComponent();
        return comp != null? comp.isVisible(): false;
    }

    /** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		// check if just this view is being updated
		boolean bSingleUpdate = !getRootView().isUpdating();
		boolean visible = getComponent().isVisible();
		Dimension d = null;
		if (bSingleUpdate) {
			// save state
			d = getComponent().getSize();
		}

		// update visible state
		boolean newVisible = isVisible();
		getComponent().setVisible(newVisible);

		// component is visible?
		if (newVisible) {
			// update the state of the component
			doComponentUpdate();
		}

		// check if state has changed
		if (bSingleUpdate) {
			Dimension d2 = getComponent().getSize();
			if ((!d.equals(d2)) || (visible != newVisible)) {
				notifyStateChanged();
			}
		}
	}


	/**
	 * Update the width of the message to be displayed according to the left space of the layout
	 * @param width is the maximum width that the message can use
	 */
	protected void updateMessagesWidth(int width) {
		if (lblMessages == null) {
			return;
		}
		
		XViewUtils.adjustHeight(lblMessages, width);
	}
	
	/**
	 * Update the label to display the messages
	 */
	protected void doMessageUpdate() {
		// check about messages to display
		if (hasMessages()) {
			// create label to display messages
			if (lblMessages == null) {
				lblMessages = new JXLabel();
				lblMessages.setLineWrap(true);
				lblMessages.setFont( lblMessages.getFont().deriveFont(Font.BOLD));
				lblMessages.setForeground(new Color(255, 0, 0));
			}
			
			lblMessages.setText(getDisplayMessages());
			getParentComponent().add(lblMessages);
		}
		else {
			// there is no message to display
			// message label is visible ?
			if (lblMessages != null) {
				// remove it from the parent and destroy it
				lblMessages.getParent().remove(lblMessages);
				lblMessages = null;
			}
		}
	}


	/**
	 * Update the position of the component in the parent container
	 * @param x is the x position
	 * @param y is the y position
	 */
	public void setComponentLocation(int x, int y) {
		getComponent().setLocation(x, y);
		if (lblMessages != null) {
			lblMessages.setLocation(x, y + getComponent().getHeight());
		}
	}

	/**
	 * Return the height of the view in pixels
	 * @return int value
	 */
	public int getHeight() {
		int height = getComponent().getHeight();
		if (lblMessages != null) { 
			height += lblMessages.getHeight() + MESSAGE_PADDING;
		}
		return height;
	}
	
	/**
	 * Return the component that displays the messages
	 * @return instance of {@link JXLabel}
	 */
	public JXLabel getMessagesComponent() {
		return lblMessages;
	}

	/**
	 * Return true if the view can grow its width
	 * @return boolean value
	 */
	public boolean isAutoGrow() {
		return true;
	}
	
	
	/**
	 * Notify the form that the size of this component has changed and
	 * that its layout must be updated as soon as possible
	 */
	public void notifySizeChanged() {
		if (!isUpdating()) {
			getFormContext().getFormUI().updateLayout();
		}
	}
	
	
	/**
	 * Return the background color in use in the form, i.e, the background
	 * color of the component in the root form
	 * @return instance of {@link Color}
	 */
	public Color getBackgroundColor() {
		return ((ComponentUI)getRootView()).getComponent().getBackground();
	}
}
