package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.PanelKey;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.components.JTransactionalButton;
import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.Gender;
import org.msh.etbm.services.cases.CaseCloseService;
import org.msh.etbm.services.cases.CasePermissionServices;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.swing.SwingFormContext;

/**
 * Display the details of a case into a panel, splitting the panel in two areas - the left
 * panel, containing the commands, and the central panel, containing the data of the case
 * 
 * @author Ricardo Memoria
 *
 */
public class CaseDetailPanel extends JPanel implements Refreshable, PanelKey, EventBusListener {
	private static final long serialVersionUID = 5096175116788284010L;

	private Integer tbcaseId;
	private TbCase tbcase;
	private JPanel pnlContent;
	private JPanel pnlLeft;
	private JLabel txtName;
	private JLabel txtState;
	private JPanel pnlCaseDetails;
	private JPanel pnlExams;
	private JPanel pnlMedExams;
	private JPanel pnlTreatment;
	private JPanel pnlOthers;
	private JPanel pnlEdit;
	
	private SwingFormContext formData;

	private JButton btnTags;
	private JButton btnReopen;
	private JButton btnCloseCase;
	private JButton btnDeleteCase;
	private JButton btnSuspectFollowup;
	
	private JTabbedPane tabsCase;
	
	/**
	 * Create the panel.
	 */
	public CaseDetailPanel() {
		setLayout(new BorderLayout(0, 0));
		
		pnlLeft = new JPanel();
		add(pnlLeft, BorderLayout.WEST);
		
		btnDeleteCase = new JTransactionalButton(Messages.getString("form.casedelete"));
		btnDeleteCase.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE_SIGN, btnDeleteCase));
		btnDeleteCase.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
				deleteCase();
			}
		});
		
		btnSuspectFollowup = new JTransactionalButton(Messages.getString("cases.suspect.followup"));
		btnSuspectFollowup.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CHEVRON_RIGHT, btnSuspectFollowup));
		btnSuspectFollowup.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
				suspectFollowup();
			}
		});
		
		btnCloseCase = new JButton(Messages.getString("cases.close") + "...");
		btnCloseCase.setIcon(new AwesomeIcon(AwesomeIcon.ICON_POWER_OFF, btnCloseCase));
		btnCloseCase.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
				closeCase();
			}
		});
		
		btnReopen = new JTransactionalButton(Messages.getString("cases.reopen"));
		btnReopen.setIcon(new AwesomeIcon(AwesomeIcon.ICON_FOLDER_OPEN, btnReopen));
		btnReopen.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
				reopenCase();
			}
		});
		
		btnTags = new JButton(Messages.getString("admin.tags"));
		GroupLayout gl_pnlLeft = new GroupLayout(pnlLeft);
		gl_pnlLeft.setHorizontalGroup(
			gl_pnlLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLeft.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLeft.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSuspectFollowup, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDeleteCase, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCloseCase, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnReopen, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnTags, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(30, Short.MAX_VALUE))
		);
		gl_pnlLeft.setVerticalGroup(
			gl_pnlLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLeft.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnSuspectFollowup)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDeleteCase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCloseCase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnReopen)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTags)
					.addContainerGap(360, Short.MAX_VALUE))
		);
		pnlLeft.setLayout(gl_pnlLeft);
		
		pnlContent = new JPanel();
		pnlContent.setBackground(Color.WHITE);
		add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		pnlContent.add(panel, BorderLayout.NORTH);
		
		txtName = new JLabel("txtName");
		txtName.setVerticalAlignment(SwingConstants.TOP);
		txtName.setFont(UiConstants.h2Font);
		
		txtState = new JLabel("New label");
		txtState.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtState)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtName)
						.addComponent(txtState))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		tabsCase = new JTabbedPane(JTabbedPane.TOP);
		tabsCase.addChangeListener(new ChangeListener() {
		    @Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tab = (JTabbedPane)e.getSource();
				updateTabChanged(tab);
			}
		});
		tabsCase.setBorder(new EmptyBorder(4, 4, 4, 4));
		pnlContent.add(tabsCase, BorderLayout.CENTER);
		
		pnlCaseDetails = new JPanel();
		pnlCaseDetails.setBackground(Color.WHITE);
		tabsCase.addTab(Messages.getString("cases.details.case"), null, pnlCaseDetails, null);
		tabsCase.setMnemonicAt(0, 0);
		pnlCaseDetails.setLayout(new BorderLayout(0, 0));
		
		pnlEdit = new JPanel();
		pnlCaseDetails.add(pnlEdit, BorderLayout.NORTH);
		pnlEdit.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnEditCase = new JButton(Messages.getString("form.edit") + "..."); //$NON-NLS-1$
		btnEditCase.setIcon(new AwesomeIcon(AwesomeIcon.ICON_EDIT, btnEditCase));
		btnEditCase.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
				editCase();
			}
		});
		pnlEdit.add(btnEditCase);
		
		pnlExams = new JPanel();
		tabsCase.addTab(Messages.getString("cases.details.exams"), null, pnlExams, null); //$NON-NLS-1$
		tabsCase.setMnemonicAt(1, 1);
		
		pnlMedExams = new JPanel();
		tabsCase.addTab(Messages.getString("cases.details.medexam"), null, pnlMedExams, null);
		tabsCase.setMnemonicAt(2, 2);
		
		pnlTreatment = new JPanel();
		tabsCase.addTab(Messages.getString("cases.details.treatment"), null, pnlTreatment, null);
		tabsCase.setMnemonicAt(3, 3);
		
		pnlOthers = new JPanel();
		tabsCase.addTab(Messages.getString("cases.details.otherinfo"), null, pnlOthers, null);
		tabsCase.setMnemonicAt(4, 4);

//		pnlDrugogram = new JPanel();
//		tabsCase.addTab(Messages.getString("cases.details.report1"), null, pnlDrugogram, null);
//		tabsCase.setMnemonicAt(5, 5);
	}


	/**
	 * Called when user clicks on the delete button. A confirmation window is displayed to the
	 * user confirm deleting the case, and once it's confirmed it's deleted  
	 */
	protected void deleteCase() {
		if (JOptionPane.showConfirmDialog(this, Messages.getString("form.confirm_remove"), Messages.getString("form.casedelete"), JOptionPane.YES_NO_OPTION) == 1)
			return;

		App.getEntityManager().createQuery("delete from TbCase where id = :id")
			.setParameter("id", tbcaseId)
			.executeUpdate();

		// notify system about case deleted
		EventBusService.raiseEvent(AppEvent.CASE_DELETED, tbcase);

		// remove itself from the main window
		MainWindow.instance().removePanel(this);
	}


	/**
	 * Called when the user clicks on the close case button. Displays the close case dialog
	 * and refreshes the content if case is closed
	 */
	protected void closeCase() {
		if (CloseCaseDlg.execute(tbcaseId)) {
			EventBusService.raiseEvent(AppEvent.CASE_MODIFIED, tbcase);
			refresh();
		}
	}

	
	/**
	 * Start the suspect follow-up process. Just available if the case is a suspect
	 */
	protected void suspectFollowup() {
		SuspectFollowupDlg dlg = new SuspectFollowupDlg();
		if (dlg.open(tbcase)) {
			refresh();
		}
	}
	
	/**
	 * Called when the user switch from one tab to another
	 * @param tab is the instance of {@link JTabbedPane} that was just changed
	 */
	protected void updateTabChanged(JTabbedPane tab) {
		int mn = tab.getMnemonicAt(tab.getSelectedIndex());
		JPanel pnl = (JPanel)tab.getComponentAt(tab.getSelectedIndex());
		if (pnl.getComponentCount() == 0) {
			JPanel content;
			switch (mn) {
			case 1: // exams
				content = new TabExamsPanel();
				break;

			case 2: // medical examination
				content = new TabMedExamsPanel();
				break;
				
			case 3: // treatment panel
				content = new TabTreatmentPanel();
				break;
				
			case 4: // other information
				content = new TabOtherInfoPanel();
				break;

			default:
				content = null;
				break;
			}

			if (content != null) {
				pnl.setLayout(new BorderLayout(0, 0));
				pnl.setBackground(new Color(255,255,255));
				CaseTabPanel casePanel = (CaseTabPanel)content;
				// update its content
				casePanel.setCaseId(getTbcaseId());
				casePanel.refresh();

				pnl.add(content, BorderLayout.CENTER);
			}
		}
		else {
			// check if the tab panel to be displayed must be refreshed
			Component comp = pnl.getComponent(0);
			if (comp instanceof CaseTabPanel) {
				CaseTabPanel tabPanel = (CaseTabPanel)comp;
				if (tabPanel.isRefreshRequired())
					tabPanel.refresh();
			}
		}
	}


	/**
	 * Open the edit dialog window to change the case data
	 */
	protected void editCase() {
		TbCase tbcase = (TbCase)formData.getDataModel().getValue("tbcase");
		if (CaseEditDlg.execute(tbcase.getId())) {
			refresh();
			EventBusService.raiseEvent(AppEvent.CASE_MODIFIED, tbcase);
		}
	}

	/**
	 * Called when the user click on the reopen case button. If confirmed, the case
	 * is reopened and the content page is refreshed
	 */
	protected void reopenCase() {
		if (JOptionPane.showConfirmDialog(this, Messages.getString("cases.reopen.confirm"), Messages.getString("cases.reopen"), JOptionPane.YES_NO_OPTION) == 1)
			return;

		// refresh case data with the entity manager
		tbcase = App.getEntityManager().merge(tbcase);

		// reopens the case
		CaseCloseService srv = App.getComponent(CaseCloseService.class);
		srv.reopenCase(tbcase);
		
		// refresh content
		refresh();

		// notify the system about case modified
		EventBusService.raiseEvent(AppEvent.CASE_MODIFIED, tbcase);
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.Refreshable#refresh()
	 */
	@Override
	public void refresh() {
		boolean hasCase = tbcaseId != null;
		
		pnlContent.setVisible(hasCase);
		pnlLeft.setVisible(hasCase);
		
		if (!hasCase)
			return;
		
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				refreshInTransaction();
			}
		});

		// set all panels must be refreshed when displayed to the user
		for (Component comp: tabsCase.getComponents()) {
			if (((Container)comp).getComponentCount() > 0)
				comp = ((Container)comp).getComponent(0);
			if (comp instanceof CaseTabPanel) {
				((CaseTabPanel)comp).setRefreshRequired(true);
			}
		}

		// get the selected panel
		Component comp = tabsCase.getComponentAt(tabsCase.getSelectedIndex());
		if (((Container)comp).getComponentCount() > 0) {
			comp = ((Container)comp).getComponent(0);
			if (comp instanceof CaseTabPanel)
				((CaseTabPanel)comp).refresh();
		}
	}


	/**
	 * Refresh the content of the form, but must be inside a transaction. 
	 * Never call this method, but use refresh() method instead
	 */
	protected void refreshInTransaction() {
		if (formData == null) {
			formData = GuiUtils.createForm("casenew");
			formData.getFormUI().setReadOnly(true);

			JPanel pnl = (JPanel)formData.getFormUI().getComponent();
//			pnl.setBackground(new Color(255,255,255));
			JScrollPane scroll = new JScrollPane(pnl);
			scroll.setBorder(new EmptyBorder(0,0,0,0));
			
			pnlCaseDetails.add(scroll, BorderLayout.CENTER);
		}
		
		formData.getDataModel().clear();
		CaseServices caseSrv = App.getComponent(CaseServices.class);
		tbcase = caseSrv.findEntity(tbcaseId);
		
		pnlEdit.setVisible(tbcase.isOpen());
		
		formData.getDataModel().setValue("tbcase", tbcase);
		formData.getDataModel().setValue("medicalexamination", new MedicalExamination());
		formData.getFormUI().setPreferredWidth(600);
		
		formData.getFormUI().setBackgroundColor(Color.WHITE);
		formData.getFormUI().update();

		String s = "<html>" + tbcase.getPatient().getFullName() + "<br/><span style='font-size:11px'>" + tbcase.getDisplayCaseNumber() + "</span></html>";
		txtName.setText(s);
		
		updateLeftPanel();
		
		String res;
		if (tbcase.getPatient().getGender() == Gender.MALE)
			 res = "male_32x32.png";
		else res = "female_32x32.png";
		txtName.setIcon(new ImageIcon(CaseDetailPanel.class.getResource("/resources/images/" + res)));
		
		txtState.setText(Messages.getString(tbcase.getState().getKey()));
		if ((tbcase.getState() == CaseState.ONTREATMENT) || (tbcase.getState() == CaseState.TRANSFERRING))
			 txtState.setForeground(new Color(0xff6600));
		else txtState.setForeground(new Color(34, 139, 34));

		CasePermissionServices perms = App.getComponent(CasePermissionServices.class);
		// check permission of the buttons
		btnCloseCase.setVisible( perms.canClose(tbcase) );
		btnReopen.setVisible( perms.canReopen(tbcase) );
		btnDeleteCase.setVisible( perms.canDelete(tbcase) );
		btnTags.setVisible( false); //caseCtrl.isCanTagCase() );
		btnSuspectFollowup.setVisible( tbcase.isSuspect() && tbcase.isOpen());
	}

	/**
	 * Update buttons in the left panel
	 */
	private void updateLeftPanel() {
		//
	}

	/**
	 * Return the case being displayed
	 * @return instance of {@link TbCase} class
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getKey() {
		return tbcase != null? GuiUtils.getTbCasePanelKey(tbcase): null;
	}

	/**
	 * Return the ID number of the case being displayed
	 * @return the tbcaseId
	 */
	public Integer getTbcaseId() {
		return tbcaseId;
	}

	/**
	 * Set the ID number of the case being displayed. The content of the panel is
	 * not updated. To update the content, the method <code>refresh()</code> must
	 * be called
	 * @param tbcaseId is the ID of the case
	 */
	public void setTbcaseId(Integer tbcaseId) {
		this.tbcaseId = tbcaseId;
	}


	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		refresh();
	}
}
