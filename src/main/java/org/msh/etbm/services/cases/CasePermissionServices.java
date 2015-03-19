/**
 * 
 */
package org.msh.etbm.services.cases;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.ValidationState;
import org.msh.etbm.services.login.UserSession;
import org.springframework.stereotype.Component;

/**
 * Component to check user permissions about a case
 * @author Ricardo Memoria
 *
 */
@Component
public class CasePermissionServices {
	
	/**
	 * Check if role name suffix is allowed to the case according to its classification
	 * @param sufixName
	 * @return
	 */
	public boolean checkRoleBySuffix(TbCase tbcase, String suffixName) {
		CaseClassification cla = tbcase.getClassification();
		
		if (cla == null)
			 return false;
		else return UserSession.instance().hasRole(cla.toString() + "_" + suffixName);
	}


	/**
	 * Check if the case can be validated
	 * @return true if can be validated
	 */
	public boolean canValidate(TbCase tbcase) {
		if (tbcase.getId() == null)
			return false;
		ValidationState vs = tbcase.getValidationState();
		return ((vs == ValidationState.WAITING_VALIDATION) || (vs == ValidationState.PENDING_ANSWERED)) && (checkRoleBySuffix(tbcase, "CASE_VALIDATE") && (isWorkingUnit(tbcase)));
	}

	
	/**
	 * Check if user can create a new issue for the case
	 * @return
	 */
	public boolean canCreateIssue(TbCase tbcase) {
		return (checkRoleBySuffix(tbcase, "CASE_VALIDATE") && (tbcase.getValidationState() != ValidationState.VALIDATED) && (isWorkingUnit(tbcase)));
	}

	
	/**
	 * Check if user can answer a pending case
	 * @return
	 */
	public boolean canAnswerIssue(TbCase tbcase) {
		return ((tbcase.getId() != null) && (tbcase.getValidationState() == ValidationState.PENDING) && (isWorkingUnit(tbcase)));
	}
	
	/**
	 * Check if the case can be transfered to another health unit
	 * @return true if can be transfered
	 */
	public boolean canTransferOut(TbCase tbcase) {
		return (tbcase.isOpen()) && (tbcase.getState() == CaseState.ONTREATMENT) && (checkRoleBySuffix(tbcase, "CASE_TRANSFER")) && (isWorkingUnit(tbcase));
	}
	
	/**
	 * Check if user can execute the transfer in command
	 * @return
	 */
	public boolean canTransferIn(TbCase tbcase) {
		return (tbcase.isOpen()) && (tbcase.getState() == CaseState.TRANSFERRING) && (checkRoleBySuffix(tbcase, "CASE_TRANSFER")) && (isWorkingUnit(tbcase));
	}
	
	/**
	 * Check if user can view the exams of the case
	 * @return
	 */
	public boolean canViewExams(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_EXAMS");
	}
	
	/**
	 * Check if user can view the treatment of the case
	 * @return
	 */
	public boolean canViewTreatment(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_TREAT");
	}
	
	/**
	 * Check if user can view the treatment calendar of the case
	 * @return
	 */
	public boolean canViewTreatmentCalendar(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_INTAKEMED");
	}
	
	/**
	 * Check if user can view the drugogram of the case
	 * @return
	 */
	public boolean canViewDrugogram(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_DRUGOGRAM");
	}
	
	/**
	 * Check if user can close the case
	 * @return
	 */
	public boolean canClose(TbCase tbcase) {
		return (tbcase.isOpen()) && (checkRoleBySuffix(tbcase, "CASE_CLOSE")) && isWorkingUnit(tbcase);
	}
	
	/**
	 * Check if user can reopen the case
	 * @return
	 */
	public boolean canReopen(TbCase tbcase) {
		return (!tbcase.isOpen()) && (checkRoleBySuffix(tbcase, "CASE_REOPEN")) && isWorkingUnit(tbcase);
	}

	
	/**
	 * Check if user can view the case
	 * @return
	 */
	public boolean canViewCase(TbCase tbcase) {
		if (tbcase.getId() == null)
			 return false;
		else return checkRoleBySuffix(tbcase, "CASE_VIEW");
	}

	/**
	 * Check if case data can be displayed (patient data and clinical view)
	 * @return
	 */
	public boolean canViewCaseData(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_DATA");
	}
		
	/**
	 * Check if user can view additional information of the case
	 * @return
	 */
	public boolean canViewAditionalInfo(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_ADDINFO");
	}
	
	/**
	 * Check if user can edit case data
	 * @return
	 */
	public boolean canEditCaseData(TbCase tbcase) {
		return (tbcase.isOpen()) && checkRoleBySuffix(tbcase, "CASE_DATA_EDT") && (isWorkingUnit(tbcase));
	}
	
	/**
	 * Check if user can delete a case
	 * @return
	 */
	public boolean canDelete(TbCase tbcase) {
		if(tbcase.getValidationState().equals(ValidationState.VALIDATED))
			return (tbcase.isOpen()) && checkRoleBySuffix(tbcase, "CASE_DEL_VAL") && (isWorkingUnit(tbcase));
		else
			return (tbcase.isOpen()) && checkRoleBySuffix(tbcase, "CASE_DATA_EDT") && (isWorkingUnit(tbcase));
	}
	
	/**
	 * Check if user can start treatment of the case
	 * @return
	 */
	public boolean canStartTreatment(TbCase tbcase) {
		CaseState st = tbcase.getState();
		return tbcase.isOpen() && (tbcase.getId() != null) && (st != null) && (st.ordinal() < CaseState.ONTREATMENT.ordinal()) && (canEditTreatment(tbcase));
	}
	
	/**
	 * Check if user can edit treatment of the case
	 * @return
	 */
	public boolean canEditTreatment(TbCase tbcase) {
		return tbcase.isOpen() && (tbcase.isOpen()) && (checkRoleBySuffix(tbcase, "CASE_TREAT_EDT") && (isWorkingUnit(tbcase)));
	}

	/**
	 * Check if user can edit the treatment follow-up calendar of the case
	 * @return
	 */
	public boolean canEditTreatmentCalendar(TbCase tbcase) {
		return tbcase.isOpen() && (tbcase.isOpen()) && (checkRoleBySuffix(tbcase, "CASE_INTAKEMED_EDT") && (isWorkingUnit(tbcase)));
	}
	
	/**
	 * Check if user can edit the exams of the case
	 * @return
	 */
	public boolean canEditExams(TbCase tbcase) {
		return tbcase.isOpen() && checkRoleBySuffix(tbcase, "CASE_EXAMS_EDT") && isWorkingUnit(tbcase);
	}

	/**
	 * Check if user can edit additional information of the case
	 * @return
	 */
	public boolean canEditAdditionalInfo(TbCase tbcase) {
		return tbcase.isOpen() && checkRoleBySuffix(tbcase, "CASE_ADDINFO_EDT") && isWorkingUnit(tbcase);
	}

	/**
	 * Check if user is working on its working unit. It depends on the case state and the user profile.
	 * 1) If user can play activities of all other units, so it's the working unit;
	 * 2) If case is waiting for treatment, the user unit is compared to the case notification unit;
	 * 3) If case is on treatment, the user unit is compared to the case treatment unit;
	 * @return
	 */
	public boolean isWorkingUnit(TbCase tbcase) {
		UserWorkspace ws = (UserWorkspace)App.getComponent("userWorkspace");
		if (ws.isPlayOtherUnits())
			return true;

		Tbunit ownerUnit = tbcase.getOwnerUnit();

		if (ownerUnit != null)
			return (ownerUnit.getId().equals(ws.getTbunit().getId()));

		return true;
	}
	
	/**
	 * Check if user can add comments to the case
	 * @return
	 */
	public boolean canAddComments(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_COMMENTS") && isWorkingUnit(tbcase);
	}
	
	/**
	 * Check if user can tag the case
	 * @return
	 */
	public boolean canTagCase(TbCase tbcase) {
		return checkRoleBySuffix(tbcase, "CASE_TAG") && isWorkingUnit(tbcase);
	}

	
}
