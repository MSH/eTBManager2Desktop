package org.msh.xview.swing;

import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.msh.xview.FormMessage;
import org.msh.xview.components.XView;
import org.msh.xview.impl.AbstractFormContextImpl;
import org.msh.xview.swing.event.FormEvent;
import org.msh.xview.swing.event.FormEventHandler;
import org.msh.xview.swing.ui.FormUI;
import org.msh.xview.swing.ui.ViewUI;
import org.msh.xview.swing.ui.ViewUIFactory;

/**
 * Create a form adapter to be used in AWT Swing applications
 * 
 * @author Ricardo Memoria
 *
 */
public class SwingFormContext extends AbstractFormContextImpl {
	
	private FormUI formUI;
	private JScrollPane scrollPanel;
	private boolean applyingValues = false;
	private List<FormEventHandler> formEventHandlers;

	
	/**
	 * Return the instance of the {@link FormUI} that contains the UI elements
	 * of the form
	 * @return instance of {@link FormUI}
	 */
	public FormUI getFormUI() {
		if (formUI == null) {
			formUI = (FormUI)ViewUIFactory.createUI(getForm());
			formUI.initialize(this);
		}
		
		return formUI;
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.FormAdapter#validate()
	 */
	@Override
	public boolean validate() {
		clearMessages();

		FormUI frm = getFormUI();
		if (frm.isReadOnly())
			return true;
		
		boolean result = frm.validate();

		// if was validated by the form, check the controllers
/*		if (result) {
			Set<String> vars = getDataModel().getVariables();
			for (String var: vars) {
				VariableController controller = getDataModel().getController(var);
				if ((controller != null) && (!controller.validate(this)))
					result = false;
			}
		}
*/
		// if there are validation errors, update the whole form
//		if (!result)
		frm.updateLayout();
		
		return result;
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.impl.AbstractFormContextImpl#clearMessages()
	 */
	@Override
	public void clearMessages() {
		super.clearMessages();
		formUI.clearMessages();
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.impl.AbstractFormContextImpl#addMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public FormMessage addMessage(String componentId, String message) {
/*		FormMessage msg = super.addMessage(componentId, message);
		
		if (componentId == null)
			formUI.addMessage(msg);
		else {
			ViewUI<XView> view = formUI.findComponentById(componentId);
			
			if (view != null)
				view.addMessage(msg);
		}
*/		return null;
	}

	
	/**
	 * Add a message to a specific component
	 * @param view
	 * @param message
	 * @return
	 */
	public FormMessage addMessage(ViewUI view, String message) {
		return view.addMessage(message);
	}
	
	
	/**
	 * Return the form inside a {@link ScrollPane}
	 * @return
	 */
	public JScrollPane getScrollPaneForm() {
		if (scrollPanel == null) {
			JPanel pnl = (JPanel)getFormUI().getComponent();
			scrollPanel = new JScrollPane(pnl);
			scrollPanel.setBorder(new EmptyBorder(0,0,0,0));
		}
		
		return scrollPanel;
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.ValueChangeListener#onValueChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void onValueChange(String field, Object oldValue, Object newValue) {
		// check if form was initialized already
		if ((formUI == null) || (!formUI.isInitialized()))
			return;

		// check if field is a variable
		if (field.indexOf('.') == -1) {
			getFormUI().update();
		}
		else {
			// get list of dependent components and update them
			List<XView> views = getForm().getDependencies(field);
			for (XView view: views) {
				ViewUI viewui = getFormUI().findComponentById(view.getId());
				viewui.update();
			}

			// update the layout for components that changed its state
			if (getFormUI().isLayoutRefreshNeeded())
				getFormUI().updateLayout();
		}
	}

	
	/**
	 * Add a new event handler that will receive event notifications that happen inside the form
	 * @param handler instance of {@link FormEventHandler} to receive notifications
	 */
	public void addEventHandler(FormEventHandler handler) {
		if (formEventHandlers == null)
			formEventHandlers = new ArrayList<FormEventHandler>();
		formEventHandlers.add(handler);
	}
	
	
	/**
	 * Remove a form event handler from this context. The event handler must have been 
	 * registered before using the <code>add(handler)</code> method
	 * @param handler instance of {@link FormEventHandler} previously registered
	 */
	public void removeEventHandler(FormEventHandler handler) {
		if (formEventHandlers == null)
			return;
		formEventHandlers.remove(handler);
	}
	
	/**
	 * Notify all event handlers about a new event that was rose by a view 
	 * @param evt is the instance of {@link FormEvent} to raise
	 */
	public void notifyEvent(FormEvent evt) {
		if (formEventHandlers == null)
			return;
		
		// list all event handlers and inform about the event
		for (FormEventHandler handler: formEventHandlers)
			handler.onFormEvent(evt);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isApplyingValues() {
		return applyingValues;
	}


	/** {@inheritDoc}
	 */
	@Override
	public boolean applyValues(boolean validateBefore) {
		// TODO Auto-generated method stub
		return false;
	}
}
