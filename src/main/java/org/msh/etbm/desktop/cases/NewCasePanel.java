package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.cases.MedicalExaminationServices;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.FormManager;
import org.msh.xview.swing.SwingFormContext;

/**
 * Swing panel that display the new case/suspect notification form. The
 * form is displayed using the xview library
 * 
 * @author Ricardo Memoria
 *
 */
public class NewCasePanel extends JPanel implements Refreshable {
	private static final long serialVersionUID = -402826428913223520L;
	
//	private CaseHome caseHome;
	private Patient patient;
	private TbCase tbcase;
	private SwingFormContext formContext;
	
	/**
	 * Create the panel.
	 */
	public NewCasePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 204, 204));
		add(panel, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton(Messages.getString("form.cancel")); //$NON-NLS-1$
		btnCancel.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE, btnCancel));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cancel();
			}
		});
		
		JButton btnSave = new JButton(Messages.getString("form.save")); //$NON-NLS-1$
		btnSave.setIcon(new AwesomeIcon(AwesomeIcon.ICON_OK, btnSave));
		btnSave.setDefaultCapable(true);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveData();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(241, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
/*		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(4, 4, 4, 4));
		panel_1.setBackground(new Color(204, 204, 204));
		add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel(Messages.getString("NewCasePanel.lblNewLabel.text")); //$NON-NLS-1$
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(394, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
*/	}


	/**
	 * Cancel the operation and close the new case form
	 */
	protected void Cancel() {
		MainWindow.instance().removePanel(this);
	}


	/**
	 * Validate and save the new case when user clicks on the save button
	 */
	protected void saveData() {
		if (!formContext.validate())
			return;

		// save tbcase inside a transaction
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				TbCase tbcase = (TbCase)formContext.getValue("tbcase");
				Patient p = (Patient)formContext.getValue("patient");
                tbcase.setPatient(p);
				System.out.println(tbcase.getId());
//				Patient p = tbcase.getPatient();
				System.out.println(p.getId());
				System.out.println(p.getBirthDate());
				
				if (p.getId() != null) {
					p = App.getEntityManager().merge(p);
					tbcase.setPatient(p);
					System.out.println(p.getBirthDate());
				}
				
				CaseServices.instance().save(tbcase);

				// save medical examination
				MedicalExamination medexam = (MedicalExamination)formContext.getValue("medicalexamination");
				if (DiagnosisType.CONFIRMED.equals(tbcase.getDiagnosisType()) && medexam != null) {
					MedicalExaminationServices srv = App.getComponent(MedicalExaminationServices.class);
					medexam.setTbcase(tbcase);
					srv.save(medexam);
				}
			}
		});

		// after saving it, close its panel and open its detail form
		GuiUtils.openCaseDetailPage( (Integer)formContext.getDataModel().getValue("tbcase.id") );

		MainWindow.instance().removePanel(this);

		EventBusService.raiseEvent(AppEvent.NEW_CASE_SAVED, tbcase);
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.Refreshable#refresh()
	 */
	public void refresh() {
		if (tbcase == null)
			throw new IllegalArgumentException("TB Case not created for " + getClass().toString());

		FormManager formManager = (FormManager)App.getComponent("formManager");
		formContext = (SwingFormContext)formManager.createFormAdapter("casenew");
        ((SwingFormContext)formContext).getFormUI().setPreferredWidth(800);

		formContext.setValue("patient", patient);
		formContext.setValue("tbcase", tbcase);
		
		MedicalExamination medexam = new MedicalExamination();
		formContext.setValue("medicalexamination", medexam);

		JScrollPane scrollpane = new JScrollPane(formContext.getFormUI().getComponent());
		scrollpane.setBorder(new EmptyBorder(scrollpane.getInsets()));
		
		add(scrollpane, BorderLayout.CENTER);
		
		formContext.getFormUI().update();
	}


	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}


	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}


	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}


	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
}
