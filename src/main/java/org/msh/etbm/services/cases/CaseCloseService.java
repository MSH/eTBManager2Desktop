package org.msh.etbm.services.cases;

import java.util.Date;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.services.cases.treatment.TreatmentPeriodUtilities;
import org.msh.utils.date.Period;
import org.msh.xview.FormContext;
import org.springframework.stereotype.Component;


/**
 * Service class to open and close a case
 * @author Ricardo Memoria
 *
 */
@Component
public class CaseCloseService {

/*	private Date date;
	private CaseState state;
	private String comment;
	private TbCase tbcase;
	
*/	
	private static final CaseState[] outcomesMDR = {
		CaseState.CURED,
		CaseState.TREATMENT_COMPLETED,
		CaseState.FAILED,
		CaseState.DIED,
		CaseState.DEFAULTED,
		CaseState.NOT_EVALUATED,
		CaseState.OTHER
	};

	private static final CaseState[] outcomesTB = {
		CaseState.CURED,
		CaseState.TREATMENT_COMPLETED,
		CaseState.FAILED,
		CaseState.DIED,
		CaseState.DEFAULTED,
		CaseState.NOT_EVALUATED,
		CaseState.OTHER
	};

	private static final CaseState[] suspectOutcomes = {
		CaseState.NOT_TB,
		CaseState.DIED,
		CaseState.DEFAULTED,
		CaseState.OTHER
	};

	/**
	 * Closes an opened case, setting its outcome
	 * @param tbcase instance of the {@link TbCase} to be closed
	 * @param outcomeDate date to be closed
	 * @param state {@link CaseState} identifying the outcome
	 * @param comments the comment if available
	 * @return
	 */
	public void closeCase(TbCase tbcase, Date outcomeDate, CaseState state, String comments) {
		if ((tbcase.getTreatmentPeriod() != null) && (!tbcase.getTreatmentPeriod().isEmpty())) {
			TreatmentPeriodUtilities srv = App.getComponent(TreatmentPeriodUtilities.class);
			srv.cropTreatmentPeriod(tbcase, new Period(tbcase.getTreatmentPeriod().getIniDate(), outcomeDate));
		}
		
		EntityManager em = App.getEntityManager();
		// add TB case to the entityManager
		tbcase = em.merge(tbcase);

		tbcase.setOutcomeDate(outcomeDate);

		tbcase.setState(state);
		if (state.equals(CaseState.OTHER))
			 tbcase.setOtherOutcome(comments);
		else tbcase.setOtherOutcome(null);

		CaseServices srv = CaseServices.instance();
		srv.save(tbcase);

//		Events.instance().raiseEvent("case.close");
	}

	/**
	 * Reopen a closed case
	 * @return
	 */
	public void reopenCase(TbCase tbcase) {
		if ((tbcase.getTreatmentPeriod() == null) || (tbcase.getTreatmentPeriod().isEmpty()))
			 tbcase.setState(CaseState.WAITING_TREATMENT);
		else tbcase.setState(CaseState.ONTREATMENT);
		
		tbcase.setOtherOutcome(null);

		CaseServices.instance().save(tbcase);
//		App.getEntityManager().persist(tbcase);
//		caseHome.updateCaseTags();
	}


	/**
	 * Return the available outcomes of a case, according to its classification (TB or DR-TB)
	 * @return Array of {@link CaseState} enumerations
	 */
	public CaseState[] getOutcomes(CaseClassification cla) {
		if (cla == CaseClassification.DRTB)
			 return outcomesMDR;
		else return outcomesTB;
	}


	/**
	 * Return the possible suspect outcomes if it's not confirmed
	 * @return Array of {@link CaseState} enumeration
	 */
	public CaseState[] getSuspectoutcomes() {
		return suspectOutcomes;
	}


	
}
