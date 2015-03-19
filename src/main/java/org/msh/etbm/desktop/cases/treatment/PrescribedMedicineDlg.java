/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Dimension;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.common.GenericFormDialog;
import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.PrescribedMedicine;
import org.msh.etbm.entities.Source;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.cases.treatment.TreatmentServices;
import org.msh.xview.FormDataModel;

/**
 * Dialog window to edit a prescribed medicine of the patient treatment
 * @author Ricardo Memoria
 *
 */
public class PrescribedMedicineDlg extends GenericFormDialog {
	private static final long serialVersionUID = 9208128075691707554L;

	private Integer caseId;
	private Integer prescribedMedID;
	private PrescribedMedicine prescribedMedicine;
	
	/**
	 * Default constructor
	 */
	public PrescribedMedicineDlg() {
		super("prescribedmedicine_edt");
	}

	
	/**
	 * Add a new medicine to the given case by its ID
	 * @param tbcaseid is the ID of the case to include the medicine
	 * @return instance of the {@link PrescribedMedicine} included
	 */
	public PrescribedMedicine openNewForm(Integer tbcaseid) {
		this.caseId = tbcaseid;
		if (showModal())
			return prescribedMedicine;
		else return null;
	}
	
	
	/**
	 * Show the form for editing of the prescribed medicine and return true
	 * if the medicine was successfully changed
	 * @param pm instance of {@link PrescribedMedicine} object
	 * @return true if it was successfully changed
	 */
	public boolean openEditingForm(Integer prescribedMedID) {
		this.prescribedMedID = prescribedMedID;
		return showModal();
	}
	
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean showModal() {
		setFormSize(new Dimension(680,420));
		getForm().getFormUI().setWidth(650);
		setTitle(App.getMessage("PrescribedMedicine") + " - " + App.getMessage("form.edit"));
		return super.showModal();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void initFormData(FormDataModel dataModel) {
		EntityManager em = App.getEntityManager();
		if (prescribedMedID != null) {
			 prescribedMedicine = em.find(PrescribedMedicine.class, prescribedMedID);
			 caseId = prescribedMedicine.getTbcase().getId();
		}
		else prescribedMedicine = new PrescribedMedicine();
		
		dataModel.setValue("prescribedMedicine", prescribedMedicine);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected boolean saveFormData(FormDataModel dataModel) {
		PrescribedMedicine pm = (PrescribedMedicine)dataModel.getValue("prescribedMedicine"); 

		EntityManager em = App.getEntityManager();
		TreatmentServices srv = App.getComponent(TreatmentServices.class);

		Medicine med = em.merge(pm.getMedicine());
		Source source = em.merge(pm.getSource());

		// is a new medicine to be included ?
		TbCase tbcase = CaseServices.instance().findEntity(caseId);

		// if it's editing the prescribed medicine, so remove it first
		if (prescribedMedID != null) {
			pm = App.getEntityManager().merge(pm);
			srv.removePrescribedMedicine(pm);
		}
		
		// save prescribed medicine
		prescribedMedicine = srv.addPrescribedMedicine(tbcase, 
				med, 
				pm.getPeriod(), 
				pm.getDoseUnit(), 
				pm.getFrequency(), 
				source, 
				pm.getComments());

		return true;
	}

}
