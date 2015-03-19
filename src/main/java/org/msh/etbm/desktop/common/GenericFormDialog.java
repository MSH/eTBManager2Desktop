/**
 * 
 */
package org.msh.etbm.desktop.common;

import java.awt.BorderLayout;
import java.awt.Dimension;

import org.msh.etbm.desktop.app.Messages;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.FormDataModel;
import org.msh.xview.components.XForm;
import org.msh.xview.swing.SwingFormContext;

/**
 * Define a generic form for displaying of an {@link XForm} defined in the
 * system resources as an XML form. The name of the form is given as argument
 * of the constructor
 * 
 * @author Ricardo Memoria
 *
 */
public abstract class GenericFormDialog extends GenericDialog {
	private static final long serialVersionUID = -9032464528016970651L;

	private SwingFormContext form;
	private String formName;
	private boolean formSaved;
	private Dimension formSize;

	/**
	 * Indicate that the whole form execution will be done inside a transaction
	 */
	private boolean inTransaction;

	
	/**
	 * Default constructor, where the name of the form is passed as argument
	 * @param formName is the form name
	 */
	public GenericFormDialog(String formName) {
		super();
		this.formName = formName;
	}

	
	/** {@inheritDoc}
	 */
	@Override
	public boolean showModal() {
		// adjust layout
		getClientContent().setLayout(new BorderLayout());
		getClientContent().add( getForm().getScrollPaneForm(), BorderLayout.CENTER );
		setResizable(true);
		getOkButton().setText( Messages.getString("form.save") );

		final ActionCallback initCallback = new ActionCallback() {
			@Override
			public void execute(Object data) {
				initFormData(getForm().getDataModel());
				// update the form content
				getForm().getFormUI().update();
			}
		};

		// form must be executed inside a transaction ?
		if (isInTransaction()) {
			// execute the whole form inside a transaction
			EntityManagerUtils.doInTransaction(new ActionCallback() {
				@Override
				public void execute(Object data) {
					initCallback.execute(data);
					showModalExecution();
				}
			});
			
			return isConfirmed();
		}
		else {
			// initialize the form data inside a database transaction
			EntityManagerUtils.doInTransaction(initCallback);
			return showModalExecution();
		}
		
	}
	
	
	/**
	 * Piece of code to show the form in a modal mode
	 * @return result of showModal operation
	 */
	private boolean showModalExecution() {
		// adjust form size
		setSize(getFormSize());

		// show form
		return super.showModal();
	}

	
	/**
	 * Initialize the data to be used inside the form. This is the right place to set
	 * form variables and load the data from the database. This method is called internally by
	 * the form when it's being displayed to the user. It is also called inside a database
	 * transaction
	 * @param dataModel is the instance of the {@link FormDataModel} in use in the form
	 */
	protected abstract void initFormData(FormDataModel dataModel);
	
	/**
	 * Save the form data. It's called when the user clicks on the save button. This method is called
	 * inside a database transaction, but entities used in the form are deattached 
	 * @return true if data was successfully saved and the form will be closed, otherwise return false and the form
	 * will not be closed 
	 */
	protected abstract boolean saveFormData(FormDataModel dataModel);
	
	/**
	 * Return the size of the dialog automatically calculated according to the size of the form
	 * @return instance of {@link Dimension} class
	 */
	protected Dimension getFormSize() {
		if (formSize != null)
			return formSize;

		Dimension d = getForm().getFormUI().getComponent().getSize();

		int w = (int)d.getWidth() + 60;
		if (w < 500)
			w = 600;
		int h = (int)d.getHeight() + 110;
		if (h > 480)
			h = 480;
		if (h < 300)
			h = 300;
		d.setSize(w, h);

		return d;
	}


	/**
	 * @return the form
	 */
	public SwingFormContext getForm() {
		if (form == null)
			form = createForm();
		return form;
	}

	/**
	 * Return the form corresponding to the given form name
	 * @return instance of the {@link SwingFormContext}
	 */
	protected SwingFormContext createForm() {
		String name = getFormName();
		
		if (name == null)
			throw new IllegalArgumentException("No form name defined");

		return GuiUtils.createForm(name);
	}


	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}


	/** {@inheritDoc}
	 */
	@Override
	public boolean save() {
		if (!getForm().validate())
			return false;

		ActionCallback saveFormAction = new ActionCallback() {
			@Override
			public void execute(Object data) {
				getForm().applyValues(false);
				formSaved = saveFormData(getForm().getFormUI().getDataModel());
				if (!formSaved)
					getForm().getFormUI().update();
			}
		};

		// is already inside a transaction
		if (inTransaction)
			saveFormAction.execute(null);
		else 	// save the form data inside a transaction
			EntityManagerUtils.doInTransaction(saveFormAction);

		return formSaved;
	}


	/**
	 * @return the inTransaction
	 */
	public boolean isInTransaction() {
		return inTransaction;
	}


	/**
	 * @param inTransaction the inTransaction to set
	 */
	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}


	/**
	 * @param formSize the formSize to set
	 */
	public void setFormSize(Dimension formSize) {
		this.formSize = formSize;
	}

}
