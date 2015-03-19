package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.core.EntityServices;
import org.msh.xview.FormDataModel;
	
/**
 * Generic dialog window to edit entities related to a case 
 * (for example, exams, medical consultation, contacts, etc.)
 *  
 * @author Ricardo Memoria
 *
 * @param <E> Entity class to be edited
 */
public class CaseDataEditDlg<E> extends GenericFormDialog {

	private static final long serialVersionUID = 7761950603719264159L;

	private String entityVariableName;
	private Class<? extends EntityServices> entityServiceClass;
	private EntityServices<E> entityService;
	/**
	 * ID of the case, just available in a new entity
	 */
	private Integer caseId;
	/**
	 * ID of the entity being edited
	 */
	private Integer entityId;


	/**
	 * Dialog class constructor
	 * @param formName is the name of the form
	 * @param entityVariableName is the entity variable name
	 * @param entityServiceClass is the class that implements the {@link EntityServices} for the entity
	 */
	public CaseDataEditDlg(String formName, String entityVariableName, Class<? extends EntityServices> entityServiceClass) {
		super(formName);
		this.entityVariableName = entityVariableName;
		this.entityServiceClass = entityServiceClass;
	}


	/**
	 * Open the form for a new entity
	 * @param caseid
	 * @return
	 */
	public E openNew(Integer caseid) {
		// initialize the variables
		this.caseId = caseid;
		this.entityId = null;

		setTitle(Messages.getString("form.new") + " - " + getEntityTitle());
		
		if (!showModal())
			return null;
		
		return (E)getForm().getValue(getEntityVariableName());
	}

	
	/**
	 * Open the form for editing the entity given by its entity ID
	 * @param entityId is the ID of the entity
	 * @return instance of the entity edited or null if the user canceled the operation
	 */
	public E openEdit(Integer entityId) {
		this.entityId = entityId;
		this.caseId = null;

		setTitle(Messages.getString("form.edit") + " - " + getEntityTitle());
		
		if (!showModal())
			return null;
		
		return (E)getForm().getValue(getEntityVariableName());
	}
	

	/**
	 * Return the implementation of the {@link EntityServices} for the entity handled in this dialog window
	 * @return implementation of {@link EntityServices} interface, or null if the service is not available
	 */
	protected EntityServices<E> getEntityService() {
		if (entityService == null) {
			if (getEntityServiceClass() == null)
				throw new IllegalAccessError("No entityServiceName given");

			Object comp = App.getComponent(getEntityServiceClass());

			if (!(comp instanceof EntityServices))
				throw new IllegalAccessError("Instance " + getEntityServiceClass().toString() + " doesn't implement " + EntityServices.class.getName());
			
			entityService = (EntityServices<E>)comp;
		}
		
		return entityService;
	}
	


	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		// case ID was defined ?
		if (caseId != null) {
			// initialize the case into the form
			CaseServices srv = App.getComponent(CaseServices.class);
			dataModel.setValue("tbcase", srv.findEntity(caseId));
		}

		// entity ID was defined ?
		if (entityId != null) {
			// get variable name and value
			String varname = getEntityVariableName();
			Object entity = getEntityService().findEntity(entityId);
			dataModel.setValue(varname, entity);
		}
		else getForm().setValue(getEntityVariableName(), getEntityService().newEntity());
		
		getForm().getFormUI().setPreferredWidth(600);
	}


	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
//		getForm().getFormUI().applyValues();
		if (!getForm().validate())
			return false;
		
		E entity = (E)getForm().getDataModel().getValue(getEntityVariableName());
		entity = App.getEntityManager().merge(entity);
		getEntityService().save(entity);
		return true;
	}


	/**
	 * Return the entity name to be displayed in the title of the window
	 * @return String value
	 */
	public String getEntityTitle() {
		Class clazz = getEntityService().getEntityClass();
		return Messages.getString(clazz.getSimpleName());
	}
	
	/**
	 * Initialize the case and show the form to be edited. The form is displayed in modal type
	 * @return the instance of the entity being edited, or null if the user canceled the operation
	 */
/*	protected E openForm() {
		getClientContent().setLayout(new BorderLayout());
		getClientContent().add( getForm().getScrollPaneForm(), BorderLayout.CENTER );

		Dimension d = getFormDimension();
		if (d != null) {
			int w = (int)d.getWidth() + 70;
			if (w < 500)
				w = 600;
			int h = (int)d.getHeight() + 95;
			if (h > 480)
				h = 480;
			if (h < 300)
				h = 300;
			setSize((int)d.getWidth() + 70, h);
		}

		setResizable(true);
		getOkButton().setText( Messages.getString("form.save") );

		if (!showModal())
			return null;

		E entity = (E)getForm().getDataModel().getValue(getEntityVariableName());
		return entity;
	}
*/
	/**
	 * Return the dimension that form will be displayed to the user
	 * @return String value
	 */
/*	protected Dimension getFormDimension() {
		return getForm().getFormUI().getComponent().getSize();
	}
*/




	/**
	 * Return the name of the variable being edited. This name is used to get a reference of 
	 * the variable in the data model of the form context
	 * @return String value
	 */
	public String getEntityVariableName() {
		return entityVariableName;
	}

	/**
	 * @return the entityServiceClass
	 */
	public Class<? extends EntityServices> getEntityServiceClass() {
		return entityServiceClass;
	}

	/**
	 * @return the caseId
	 */
	public Integer getCaseId() {
		return caseId;
	}


	/**
	 * @return the entityId
	 */
	public Integer getEntityId() {
		return entityId;
	}

}
