package org.msh.etbm.services.cases;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.ExamDST;
import org.msh.etbm.entities.ExamDSTResult;
import org.msh.etbm.entities.enums.DstResult;
import org.msh.etbm.entities.enums.ExamStatus;
import org.msh.xview.FormContext;
import org.springframework.stereotype.Component;

@Component
public class ExamDSTServices extends LaboratoryExamServices<ExamDST> {

	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServicesImpl#save(org.msh.xview.FormContext)
	 */
	@Override
	public void save(ExamDST exam) {
		EntityManager em = App.getEntityManager();

		//If status is not performed, clear results
		if(!exam.getStatus().equals(ExamStatus.PERFORMED)){
			exam.setDateRelease(null);
			exam.setComments(null);
			exam.setNumContaminated(0);
			exam.setNumResistant(0);
			exam.setNumSusceptible(0);
			exam.setMethod(null);

			while (0 < exam.getResults().size()){
				ExamDSTResult res = exam.getResults().get(0);
				exam.getResults().remove(0);
				if (em.contains(res))
					em.remove(res);
			}

		}else {

			// remove the items that has no result
			int i = 0;
			while (i < exam.getResults().size())
				if (exam.getResults().get(i).getResult() == DstResult.NOTDONE) {
					ExamDSTResult res = exam.getResults().get(i);
					exam.getResults().remove(i);
					if (em.contains(res))
						em.remove(res);
				} else i++;

			// update number of items for reports
			exam.setNumContaminated(0);
			exam.setNumResistant(0);
			exam.setNumSusceptible(0);
			for (ExamDSTResult res : exam.getResults()) {
				switch (res.getResult()) {
					case CONTAMINATED:
						exam.setNumContaminated(exam.getNumContaminated() + 1);
						break;
					case RESISTANT:
						exam.setNumResistant(exam.getNumResistant() + 1);
						break;
					case SUSCEPTIBLE:
						exam.setNumSusceptible(exam.getNumSusceptible() + 1);
						break;
				}
			}
		}

		super.save(exam);
	}

	/** {@inheritDoc}
	 * @see org.msh.etbm.services.core.EntityServicesImpl#validate(org.msh.xview.FormContext)
	 */
	@Override
	public boolean validate(ExamDST exam, FormContext form) {
		// check if there is any result
		if(ExamStatus.PERFORMED.equals(exam.getStatus())) {
			boolean error = true;
			for (ExamDSTResult res : exam.getResults())
				if (res.getResult() != DstResult.NOTDONE) {
					error = false;
					break;
				}
			if (error) {
				form.addMessage(null, "At least one result must be informed");
				return false;
			}
		}

		return super.validate(exam, form);
	}

}
