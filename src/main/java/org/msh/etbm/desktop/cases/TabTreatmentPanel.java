package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.cases.treatment.ChangeTreatPeriodDlg;
import org.msh.etbm.desktop.cases.treatment.PrescribedMedicineDlg;
import org.msh.etbm.desktop.cases.treatment.StartIndividualizedTreatmentDlg;
import org.msh.etbm.desktop.cases.treatment.StartStandardTreatmentDlg;
import org.msh.etbm.desktop.cases.treatment.TreatFollowupController;
import org.msh.etbm.desktop.cases.treatment.TreatmentTablePanel;
import org.msh.etbm.desktop.common.AncestorAdapter;
import org.msh.etbm.desktop.common.JButtonEx;
import org.msh.etbm.desktop.common.JWarningLabel;
import org.msh.etbm.desktop.common.MigLayoutPanel;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.treatment.TreatmentServices;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.LocaleDateConverter;

/**
 * Display information about the treatment of the case.
 * The treatment is split in two parts - The regimen and the
 * medicines intake during the phases of the treatment.
 * 
 * @author Ricardo Memoria
 *
 */
public class TabTreatmentPanel extends CaseTabPanel implements EventBusListener {
	private static final long serialVersionUID = -3102392041204371564L;

	// actions of the treatment popup menu
	private static final String ACT_PERIOD_EDT = "period-edt";
	private static final String ACT_UNDO_TREATMENT = "undo-treat";
	private static final String ACT_ADD_MEDICINE = "add-medicine";
	private static final String ACT_TRANSFER_OUT = "transfer-out";
	private static final String ACT_CHANGE_REGIMEN = "change-regimen";
	private static final String ACT_START_STANDARD = "start-standard";
	private static final String ACT_START_INDIVIDUALIZED = "start-individualized";
	
	private TbCase tbcase;
	private JButton btnOptions;
	
	/**
	 * The table display the regimen of the treatment
	 */
	private TreatmentTablePanel treatTable;
	
	private TreatFollowupController treatFollowupController = new TreatFollowupController();


	private JPanel pnlNoTreatment;
	private MigLayoutPanel pnlTreatment;
	private JPopupMenu menuTreat;
	private JPopupMenu menuStartTreat;
	
	/**
	 * Default constructor
	 */
	public TabTreatmentPanel() {
		super();
		setLayout(new BorderLayout(4, 4));
		// set the event handler to refresh the treatment when necessary
		EventBusService.observeEvent(AppEvent.TREATMENT_REFRESH, this);
		addAncestorListener(new AncestorAdapter() {
			@Override
			public void ancestorRemoved(AncestorEvent event) {
				removeEventBusListener();
			}
		});
	}

	/**
	 * Remove the event bust listener when the panel is destroyed
	 */
	private void removeEventBusListener() {
		EventBusService.removeObserverHandler(this);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void refreshInTransaction(TbCase tbcase) {
		this.tbcase = tbcase;
		// is on treatment ?
		if ((tbcase.getTreatmentPeriod() != null) && (tbcase.getTreatmentPeriod().getIniDate() != null)) {
			showTreatmentPanel();
		}
		else {
			showNoTreatmentPanel();
		}
	}

	/**
	 * Display the panel that shows the case has no treatment
	 */
	private void showNoTreatmentPanel() {
		if (pnlNoTreatment == null) {
			pnlNoTreatment = new JPanel();
			pnlNoTreatment.setLayout(null);

			JLabel txtMsg = new JWarningLabel(Messages.getString("cases.details.notreatment"));
			txtMsg.setBounds(20, 10, 300, 20);
			pnlNoTreatment.add(txtMsg);
			
			JButton btn = new JButtonEx(Messages.getString("cases.details.starttreatment"), AwesomeIcon.ICON_CHEVRON_DOWN);
			pnlNoTreatment.add(btn);
			btn.setLocation(20, 50);
			btn.setSize(btn.getPreferredSize());
			pnlNoTreatment.setBounds(0, 0, 400, 80);

			btn.addActionListener(new ActionListener() {
		    	@Override
				public void actionPerformed(ActionEvent e) {
			    	JComponent comp = (JComponent)e.getSource();
					menuStartTreat.show(comp, 0, comp.getHeight() + 1);
				}
			});
			
			menuStartTreat = new JPopupMenu();
			ActionListener actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EntityManagerUtils.doInTransaction(new ActionCallback<ActionEvent>(e) {
						@Override
						public void execute(ActionEvent e) {
							String action = e.getActionCommand();
							if (ACT_START_STANDARD.equals(action)) {
								startStandardRegimen();
								return;
							}
							if (ACT_START_INDIVIDUALIZED.equals(action)) {
								startIndividualizedRegimen();
								return;
							}
						}
					});
				}
			};
			JMenuItem it = new JMenuItem(Messages.getString("regimens.standard") + "...");
			it.setActionCommand(ACT_START_STANDARD);
			it.addActionListener(actionListener);
			menuStartTreat.add(it);

			it = new JMenuItem(Messages.getString("regimens.individualized") + "...");
			it.setActionCommand(ACT_START_INDIVIDUALIZED);
			it.addActionListener(actionListener);

			menuStartTreat.add(it);
			
		}
		
		removeAll();
		pnlTreatment = null;

		add(pnlNoTreatment, BorderLayout.CENTER);
	}


	/**
	 * Called when user clicks on the menu item to start an individualized regimen
	 */
	protected void startIndividualizedRegimen() {
		tbcase = App.getEntityManager().merge(tbcase);
		if (StartIndividualizedTreatmentDlg.execute(tbcase.getId())) {
			requestCaseRefresh();
		}
	}


	/**
	 * Called when the user clicks on the menu item to start a standard regimen
	 */
	protected void startStandardRegimen() {
		tbcase = App.getEntityManager().merge(tbcase);
		if (StartStandardTreatmentDlg.execute(tbcase.getId())) {
			requestCaseRefresh();
		}
	}


	/**
	 * Show the panel with treatment information
	 */
	private void showTreatmentPanel() {
		if (pnlTreatment == null)
			createTreatmentPanel();
		treatTable.setTbcase(tbcase);
		treatTable.refresh();
		treatFollowupController.setTbcase(tbcase);
		btnOptions.setVisible(tbcase.isOpen());
		removeAll();
		JScrollPane pnl = new JScrollPane(pnlTreatment, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(pnl);
	}

	
	/**
	 * Create the treatment panel
	 */
	private void createTreatmentPanel() {
		pnlTreatment = new MigLayoutPanel("wrap 4", "[][]50[][grow]");
//		if (tbcase.isOpen()) {
			addTreatmentPopupMenu();
//		}
		
		// add information about the treatment
		if (tbcase.getRegimenIni() != null) {
			pnlTreatment.addFieldLabel("@TbCase.regimenIni");
			pnlTreatment.addLabel(tbcase.getRegimenIni().toString());
		}

		pnlTreatment.addFieldLabel("@TbCase.regimen");
		if (tbcase.getRegimen() != null)
			 pnlTreatment.addLabel(tbcase.getRegimen().toString(), "wrap");
		else pnlTreatment.addLabel("@regimens.individualized", "wrap");
		
		pnlTreatment.addFieldLabel("@TbCase.iniTreatmentDate");
		pnlTreatment.addLabel(LocaleDateConverter.getDisplayDate(tbcase.getTreatmentPeriod().getIniDate(), false));
		
		pnlTreatment.addFieldLabel("@TbCase.endTreatmentDate");
		pnlTreatment.addLabel(LocaleDateConverter.getDisplayDate(tbcase.getTreatmentPeriod().getEndDate(), false) + " (" 
				+ LocaleDateConverter.getAsLength(tbcase.getTreatmentPeriod()) + ")");

		treatTable = new TreatmentTablePanel();
		treatTable.setTbcase(tbcase);
		pnlTreatment.add(treatTable, "span 4,growx,w ::1200");

		treatFollowupController.setTbcase(tbcase);
		treatFollowupController.addComponents(pnlTreatment);
	}


	
	/**
	 * Add the menu editor of the treatment
	 */
	protected void addTreatmentPopupMenu() {
		pnlTreatment.addLabel("@Regimen").setFont(UiConstants.h3Font);

		btnOptions = new JButtonEx(Messages.getString("form.options"), AwesomeIcon.ICON_CHEVRON_DOWN);
		btnOptions.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
		    	JComponent comp = (JComponent)e.getSource();
				menuTreat.show(comp, 0, comp.getHeight() + 1);
			}
		});
		pnlTreatment.add(btnOptions, "span3,align right,wrap");
		
		menuTreat = new JPopupMenu();
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				if (ACT_UNDO_TREATMENT.equals(action)) {
					undoTreatment();
					return;
				}
				if (ACT_ADD_MEDICINE.equals(action)) {
					addMedicine();
					return;
				}
				if (ACT_CHANGE_REGIMEN.equals(action)) {
					changeRegimen();
					return;
				}
				if (ACT_PERIOD_EDT.equals(action)) {
					changePeriod();
					return;
				}
				if (ACT_TRANSFER_OUT.equals(action)) {
					transferOut();
					return;
				}
			}
		};
		JMenuItem it = new JMenuItem(Messages.getString("cases.treat.undo"));
		it.setActionCommand(ACT_UNDO_TREATMENT);
		it.addActionListener(actionListener);
		menuTreat.add(it);

		it = new JMenuItem(Messages.getString("cases.regimens.changeperiod") + "...");
		it.setActionCommand(ACT_PERIOD_EDT);
		it.addActionListener(actionListener);
		menuTreat.add(it);

		it = new JMenuItem(Messages.getString("cases.move") + "...");
		it.setActionCommand(ACT_TRANSFER_OUT);
		it.addActionListener(actionListener);
		menuTreat.add(it);

		it = new JMenuItem(Messages.getString("Regimen.add") + "...");
		it.setActionCommand(ACT_ADD_MEDICINE);
		it.addActionListener(actionListener);
		menuTreat.add(it);

		it = new JMenuItem(Messages.getString("cases.regimens.change") + "...");
		it.setActionCommand(ACT_CHANGE_REGIMEN);
		it.addActionListener(actionListener);
		menuTreat.add(it);
	}
	
	/**
	 * 
	 */
	protected void transferOut() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Display the dialog window to change the treatment period
	 */
	protected void changePeriod() {
		ChangeTreatPeriodDlg dlg = new ChangeTreatPeriodDlg();
		if (dlg.execute(getCaseId())) {
			refresh();
		}
	}


	/**
	 * 
	 */
	protected void changeRegimen() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Called when the user clicks the command to include a new medicine 
	 */
	protected void addMedicine() {
		PrescribedMedicineDlg dlg = new PrescribedMedicineDlg();
		if (dlg.openNewForm(tbcase.getId()) != null)
			refresh();
	}


	/**
	 * Called when the user select the "Undo treatment" command in the treatment menu
	 */
	protected void undoTreatment() {
		// user confirms the operation ?
		if (JOptionPane.showConfirmDialog(this, App.getMessage("cases.treat.undo.confirm"), 
				App.getMessage("cases.treat.undo"),
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;

		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				// run the undo operation 
				TreatmentServices srv = App.getComponent(TreatmentServices.class);
				tbcase = App.getEntityManager().merge(tbcase);
				srv.undoTreatment(tbcase);
			}
		});

		requestCaseRefresh();
	}


	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if ((event == AppEvent.TREATMENT_REFRESH) && (tbcase.getId().equals(data[0])))
			refresh();
	}
	

}
