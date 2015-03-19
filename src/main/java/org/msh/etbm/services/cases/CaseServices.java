package org.msh.etbm.services.cases;

import java.util.Date;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Address;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.ValidationState;
import org.msh.etbm.services.core.EntityServicesImpl;
import org.msh.utils.date.DateUtils;
import org.msh.xview.FormContext;
import org.springframework.stereotype.Component;

/**
 * Expose services to handle common operations of a case
 * @author Ricardo Memoria
 *
 */
@Component
public class CaseServices extends EntityServicesImpl<TbCase> {

	/**
	 * Return the singleton instance of the component
	 * @return instance of {@link CaseServices}
	 */
	public static CaseServices instance() {
		return App.getComponent(CaseServices.class);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServices#validate(org.msh.xview.FormContext)
	 */
	@Override
	public boolean validate(TbCase tbcase, FormContext form) {
		return true;
	}

	

	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServices#save(org.msh.xview.FormContext)
	 */
	@Override
	public void save(TbCase tbcase) {
		Patient p = tbcase.getPatient();

		// it's a new case ?
		if (tbcase.getId() == null) {
			tbcase.setState(CaseState.WAITING_TREATMENT);
			tbcase.setValidationState(ValidationState.WAITING_VALIDATION);
			tbcase.setOwnerUnit(tbcase.getNotificationUnit());

			Address notifAddress = tbcase.getNotifAddress();
			Address curAddress = tbcase.getCurrentAddress();
			curAddress.copy(notifAddress);

			// set the patient's workspace
			p.setWorkspace((Workspace)App.getComponent("defaultWorkspace"));
		}
		
		updatePatientAge(tbcase);

		// set patient must be synchronized
		setEntityToSync(tbcase.getPatient());
		// save patient
		EntityManager em = getEntityManager();
		em.persist(tbcase.getPatient());

		super.save(tbcase);
	}


	/**
	 * Update the patient's age according to his birth date and the diagnosis date 
	 */
	protected void updatePatientAge(TbCase tbcase) {
		Date dtBirth = tbcase.getPatient().getBirthDate();
		Date dtDiag = tbcase.getRegistrationDate();
		if ((dtBirth == null) || (dtDiag == null))
			return;
		
		int age = DateUtils.yearsBetween(dtBirth, dtDiag);
		tbcase.setAge(age);
	}


}
