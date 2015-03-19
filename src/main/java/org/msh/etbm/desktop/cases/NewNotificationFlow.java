package org.msh.etbm.desktop.cases;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.eventbus.EventBusService;
import org.springframework.stereotype.Component;

/**
 * Controller class that control the flow of a new case/suspect notification. This class
 * is responsible for displaying all the form sequences during the process of a new notification
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class NewNotificationFlow {

	private CaseClassification classification;

	/**
	 * Start the notification of a new case. The classification selection dialog window is
	 * displayed so the user will be able to select the case/suspect classification - TB, DR-TB
	 * or MNT case - after the selection, a dialog window to select patient name is displayed and, 
	 * on success, the notification form is finally displayed.
	 *  
	 * @param diagnosisType is the type of patient to be notified - Suspect or Confirmed
	 * @param onsave is a callback function that is called if the notification is saved
	 */
	public void start(DiagnosisType diagnosisType) {
		classification = ClassificationSelectionDlg.execute(diagnosisType);
		
		if (classification == null) {
			return;
		}
		
		Patient p = NewNotificationDlg.execute(diagnosisType);
		
		if (p != null) {
			System.out.println(p.getId());
			String s;
			if (diagnosisType == DiagnosisType.CONFIRMED)
				 s = App.getMessage("cases.new");
			else s = App.getMessage("cases.newsusp");
			
			s = App.getMessage(classification.getKey()) + " - " + s;

			NewCasePanel pnl = new NewCasePanel();
			pnl.setPatient(p);
			TbCase tbcase = new TbCase();
			tbcase.setDiagnosisType(diagnosisType);
			tbcase.setClassification(classification);
			tbcase.setPatient(p);
			pnl.setTbcase(tbcase);
			MainWindow.instance().addPanel(pnl, s, true);
			
			// notify about new case
			EventBusService.raiseEvent(AppEvent.NEW_CASE, tbcase);
		}
	}


	/**
	 * Static method to return the singleton instance of this class
	 * @return instance of {@link NewNotificationFlow}
	 */
	public static NewNotificationFlow instance() {
		return App.getComponent(NewNotificationFlow.class);
	}

	/**
	 * Return the case classification selected
	 * @return instance of {@link CaseClassification}
	 */
	public CaseClassification getClassification() {
		return classification;
	}
}
