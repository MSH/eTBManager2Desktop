package org.msh.xview.impl;

import java.util.ArrayList;
import java.util.List;

import org.msh.xview.FormContext;
import org.msh.xview.FormDataModel;
import org.msh.xview.FormInterceptor;
import org.msh.xview.FormMessage;
import org.msh.xview.ValueChangeListener;
import org.msh.xview.components.XForm;

/**
 * Base implementation of the {@link FormContext} interface, that may be suitable for any
 * environment or platform
 * 
 * @author Ricardo Memoria
 *
 */
public abstract class AbstractFormContextImpl implements FormContext, ValueChangeListener {

	private XForm form;
	private FormDataModel dataModel;
	private List<FormMessage> messages = new ArrayList<FormMessage>();
	private List<FormInterceptor> interceptors;
	// used during save operation to control which variable controller was already saved
//	private List<VariableController> savedControllers;
	
	public AbstractFormContextImpl() {
		super();
	}

	
	@Override
	public void initialize(XForm form) {
		this.form = form;
		// remove in case it is called twice
		getDataModel().removeChangeListener(this);
		getDataModel().addChangeListener(this);
	}

	
	
	@Override
	public XForm getForm() {
		return form;
	}

	/** {@inheritDoc}
	 * @see org.msh.xview.FormContext#addMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public FormMessage addMessage(String componentId, String message) {
		FormMessage msg = new FormMessage(componentId, message);
		messages.add(msg);
		return msg;
	}


	@Override
	public List<FormMessage> getGlobalMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FormMessage> getMessages() {
		return messages;
	}

	@Override
	public void clearMessages() {
		messages.clear();
	}

	/** {@inheritDoc}
	 * @see org.msh.xview.FormContext#getDataModel()
	 */
	@Override
	public FormDataModel getDataModel() {
		if (dataModel == null) {
			dataModel = new DefaultFormDataModel();
		}
		return dataModel;
	}

	
	/**
	 * Factory that create a implementation of the {@link FormDataModel} interface
	 * @return implementation of the {@link FormDataModel}
	 */
	protected FormDataModel createDataModel() {
		return new DefaultFormDataModel();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getValue(String valueref) {
		return getDataModel().getValue(valueref);
	}

	
	/** {@inheritDoc}
	 */
	@Override
	public void setValue(String valueref, Object value) {
		getDataModel().setValue(valueref, value);
	}

	/**
	 * Set the data model in use
	 * @param dataModel
	 */
	public void setDataModel(FormDataModel dataModel) {
		if (this.dataModel != null) {
			getDataModel().removeChangeListener(this);
		}
		this.dataModel = dataModel;
		
		if (this.dataModel != null) {
			this.dataModel.addChangeListener(this);
		}
	}



	@Override
	public void addFormInterceptor(FormInterceptor interceptor) {
		if (interceptors == null)
			interceptors = new ArrayList<FormInterceptor>();
		interceptors.add(interceptor);
	}

	@Override
	public void removeFormInterceptor(FormInterceptor interceptor) {
		if (interceptors == null)
			return;
		interceptors.remove(interceptor);
	}

	/**
	 * Call all interceptos registered indicating that the form is
	 * about to be updated
	 */
	public void callBeforeUpdateInterceptors() {
		if (interceptors == null)
			return;

		for (FormInterceptor interceptor: interceptors)
			interceptor.beforeFormUpdate(this);
	}

	/**
	 * Call all interceptos registered indicating that the form was
	 * just updated
	 */
	public void callAfterUpdateInterceptors() {
		if (interceptors == null)
			return;

		for (FormInterceptor interceptor: interceptors)
			interceptor.afterFormUpdate(this);
	}
	
	/** {@inheritDoc}
	 * @see org.msh.xview.FormContext#save()
	 */
/*	@Override
	public boolean save() {
		// validate the form
		if (!validate())
			return false;

		// apply values to the data model
		applyValues(false);
		
		FormDataModel dataModel = getDataModel();
		// get the list of variables in use in the form
		Set<String> vars = dataModel.getVariables();

		// invoke all controllers to save the data, respecting its dependencies
		savedControllers = new ArrayList<VariableController>();
		for (String varname: vars) {
			VariableController controller = dataModel.getController(varname);
			saveController(varname, controller);
		}
		return true;
	}
*/

	/**
	 * Save the data managed by the controller, but before checks the dependencies
	 * with other controllers
	 * 
	 * @param varname
	 * @param controller
	 */
/*	private boolean saveController(String varname, VariableController controller) {
		if (savedControllers.contains(controller)) 
			return true;

		Map<String, String> map = VariableCreator.instance().getLinksFromVar(varname);
		// save dependencies before save the controller
		for (String field: map.keySet()) {
			String othervarname = map.get(field);

			// check if the variable is managed by the data model
			if (getDataModel().containsVariable(othervarname)) {
				// check if there is a controller to this variable
				VariableController otherctrl = getDataModel().getController(othervarname);
				// check if the value of the controller is the same of the variable
				Object value = getDataModel().getValue(field);
				if ((otherctrl != null) && (otherctrl.getInstance() == value))
					saveController(othervarname, otherctrl);
			}
		}
		
		// after checking all dependencies, save the data in the controller
		boolean res = controller.save(this);
		
		if (res)
			savedControllers.add(controller);

		return res;
	}
*/
}
