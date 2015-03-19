package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.JWarningLabel;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.components.JTransactionalButton;
import org.msh.etbm.entities.CaseSideEffect;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TbContact;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.cases.ContactServices;
import org.msh.etbm.services.cases.SideEffectServices;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.event.FormEvent;
import org.msh.xview.swing.event.FormEventHandler;

/**
 * Panel containing other information (tab of the detail panel) of a case.
 * <br/>
 * The panel is split in 3 different areas displaying information about 
 * comorbidities, contacts and adverse reactions
 * 
 * @author Ricardo Memoria
 *
 */
public class TabOtherInfoPanel extends CaseTabPanel implements FormEventHandler {
	private static final long serialVersionUID = 1943238765965726204L;

	private JLabel txtComNoResult;
	private JLabel txtContNoResult;
	private JLabel txtSeNoResult;
	
	private SwingFormContext frmComorbidities;
	private SwingFormContext frmSideEffects;

	private JPanel pnlComorbidities;

	private JPanel pnlContacts;
	
	private List<SwingFormContext> contactForms;

	/**
	 * Panel displaying the list of side effects
	 */
	private JPanel pnlSideEffects;

	private GridBagLayout layoutContent;

	private JButton btnNewContact;

	private JButton btnEditComorb;

	private JButton btnNewAR;
	private JButton btnEditAR;
	private JButton btnDelAR;
	
	private CaseSideEffect selectedSideEffect;
	
	/**
	 * Create the panel.
	 */
	public TabOtherInfoPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				handleContentResized();
			}
		});
//		panel.setBorder(new LineBorder(Color.RED, 2));
		scrollPane.setViewportView(panel);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		layoutContent = new GridBagLayout();
		layoutContent.columnWidths = new int[] {200};
		layoutContent.rowHeights = new int[] {30, 40, 30, 40, 30, 40, 1};
		layoutContent.columnWeights = new double[]{1.0};
		layoutContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		panel.setLayout(layoutContent);
		
		// create comorbidites tool bar
		JPanel pnl1 = new JPanel();
		pnl1.setLayout(null);
		pnl1.setLayout(null);
		GridBagConstraints gbc_pnlComorbidities = new GridBagConstraints();
		gbc_pnlComorbidities.fill = GridBagConstraints.BOTH;
		gbc_pnlComorbidities.insets = new Insets(0, 0, 5, 0);
		gbc_pnlComorbidities.gridx = 0;
		gbc_pnlComorbidities.gridy = 0;
		panel.add(pnl1, gbc_pnlComorbidities);
		
		JLabel lblNewLabel = new JLabel(Messages.getString("cases.comorbidities"));
		lblNewLabel.setIcon(new AwesomeIcon(AwesomeIcon.ICON_TABLE, lblNewLabel));
		lblNewLabel.setFont(UiConstants.h3Font);
		lblNewLabel.setBounds(8, 2, 400, 17);
		pnl1.add(lblNewLabel);
		
		btnEditComorb = new JButton(Messages.getString("form.edit") + "/" + Messages.getString("form.new") + "...");
		btnEditComorb.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				editComorbidities();
			}
		});
		btnEditComorb.setBounds(419, 2, 100, 23);
		pnl1.add(btnEditComorb);

		// create comorbidities content panel
		pnlComorbidities = new JPanel();
		pnlComorbidities.setLayout(null);
		pnlComorbidities.setBackground(Color.WHITE);
		
		txtComNoResult = new JWarningLabel(Messages.getString("cases.details.noresultfound"));
		txtComNoResult.setBounds(8, 2, 400, 22);
		pnlComorbidities.add(txtComNoResult);

		GridBagConstraints gbc_pnl2 = new GridBagConstraints();
		gbc_pnl2.insets = new Insets(0, 0, 5, 0);
		gbc_pnl2.fill = GridBagConstraints.BOTH;
		gbc_pnl2.gridx = 0;
		gbc_pnl2.gridy = 1;
		panel.add(pnlComorbidities, gbc_pnl2);

		// title of the contact panel
		JPanel pnl3 = new JPanel();
		pnl3.setLayout(null);
		GridBagConstraints gbc_pnl3 = new GridBagConstraints();
		gbc_pnl3.insets = new Insets(0, 0, 5, 0);
		gbc_pnl3.fill = GridBagConstraints.BOTH;
		gbc_pnl3.gridx = 0;
		gbc_pnl3.gridy = 2;
		panel.add(pnl3, gbc_pnl3);

		JLabel label = new JLabel(Messages.getString("cases.contacts")); //$NON-NLS-1$
		label.setIcon(new AwesomeIcon(AwesomeIcon.ICON_TABLE, label));
		label.setFont(UiConstants.h3Font);
		label.setBounds(10, 6, 302, 17);
		pnl3.add(label);
		
		btnNewContact = new JButton(Messages.getString("form.new") + "...");
		btnNewContact.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				newContactClick();
			}
		});
		btnNewContact.setBounds(417, 2, 75, 23);
		pnl3.add(btnNewContact);

		// panel of contacts
		pnlContacts = new JPanel();
		pnlContacts.setBackground(Color.WHITE);
		pnlContacts.setLayout(null);
		GridBagConstraints gbc_pnl4 = new GridBagConstraints();
		gbc_pnl4.fill = GridBagConstraints.BOTH;
		gbc_pnl4.gridx = 0;
		gbc_pnl4.gridy = 3;
		panel.add(pnlContacts, gbc_pnl4);

		txtContNoResult = new JWarningLabel(Messages.noRecordFound());
		//txtContNoResult.setForeground(new Color(255, 140, 0));
		//txtContNoResult.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtContNoResult.setBounds(8, 5, 400, 22);
		pnlContacts.add(txtContNoResult);

		// title of the side effects
		JPanel pnl5 = new JPanel();
		pnl5.setLayout(null);
		GridBagConstraints gbc_pnl5 = new GridBagConstraints();
		gbc_pnl5.insets = new Insets(0, 0, 5, 0);
		gbc_pnl5.fill = GridBagConstraints.BOTH;
		gbc_pnl5.gridx = 0;
		gbc_pnl5.gridy = 4;
		panel.add(pnl5, gbc_pnl5);

		JLabel label2 = new JLabel(Messages.getString("cases.sideeffects")); //$NON-NLS-1$
		label2.setIcon(new AwesomeIcon(AwesomeIcon.ICON_TABLE, label2));
		label2.setFont(UiConstants.h3Font);
		label2.setBounds(10, 6, 300, 17);
		pnl5.add(label2);
		
		btnNewAR = new JButton(Messages.getString("form.new") + "...");
		btnNewAR.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				newSideEffectClick();
			}
		});
		btnNewAR.setBounds(343, 2, 71, 23);
		pnl5.add(btnNewAR);
		
		btnDelAR = new JButton(Messages.getString("form.remove")); //$NON-NLS-1$
		btnDelAR.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				deleteSideEffect();
			}
		});
		btnDelAR.setBounds(490, 2, 71, 23);
		pnl5.add(btnDelAR);
		
		btnEditAR = new JButton(Messages.getString("form.edit") + "..."); //$NON-NLS-1$
		btnEditAR.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				editSideEffect();
			}
		});
		btnEditAR.setBounds(418, 2, 71, 23);
		pnl5.add(btnEditAR);
		
		// PANEL containing information about the side effects
		pnlSideEffects = new JPanel();
		pnlSideEffects.setBackground(Color.WHITE);
		pnlSideEffects.setLayout(null);
		GridBagConstraints gbc_pnl6 = new GridBagConstraints();
		gbc_pnl6.fill = GridBagConstraints.BOTH;
		gbc_pnl6.gridx = 0;
		gbc_pnl6.gridy = 5;
		panel.add(pnlSideEffects, gbc_pnl6);

		txtSeNoResult = new JWarningLabel(Messages.noRecordFound());
//		txtSeNoResult.setForeground(new Color(255, 140, 0));
//		txtSeNoResult.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtSeNoResult.setBounds(8, 5, 400, 20);
		pnlSideEffects.add(txtSeNoResult);

		add(scrollPane);
	}


	/**
	 * Delete the selected side effect
	 */
	protected void deleteSideEffect() {
		if (selectedSideEffect == null)
			return;

		if (JOptionPane.showConfirmDialog(getParent(), 
				Messages.getString("form.confirm_remove"), 
				Messages.getString("form.remove"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
			return;

		EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(selectedSideEffect.getId()) {
			@Override
			public void execute(Integer data) {
				SideEffectServices srv = App.getComponent(SideEffectServices.class);
				srv.delete(selectedSideEffect.getId());
			}
		});
		setSelectedSideEffect(null);
		refresh();
	}


	/**
	 * Open the form for editing of the selected side effect
	 */
	protected void editSideEffect() {
		if (selectedSideEffect == null)
			return;
		
		SideEffectEditDlg dlg = new SideEffectEditDlg();
		if (dlg.openEdit(selectedSideEffect.getId()) != null)
			refresh();
	}


	/**
	 * Called when the user clicks on the new button of the side effect title bar
	 */
	protected void newSideEffectClick() {
		SideEffectEditDlg dlg = new SideEffectEditDlg();
		if (dlg.openNew(getCaseId()) != null)
			refresh();
	}


	/**
	 * Called when content is resized. Update the position of the
	 * buttons to keep them always aligned to the right side of the 
	 * title bars
	 */
	protected void handleContentResized() {
		int xo = pnlComorbidities.getWidth();
		int w = btnEditComorb.getWidth();
		btnEditComorb.setBounds(xo - w - 6, 2, w, 22);

		w = btnNewContact.getWidth();
		btnNewContact.setBounds(xo - w - 6, 2, w, 22);

		updateComorbiditesButtons();
	}


	private void updateComorbiditesButtons() {
		// get width from the comorbidites bar, because it's the same ad
		int xo = pnlComorbidities.getWidth();
		
		boolean b = selectedSideEffect != null;
		btnDelAR.setEnabled(b);
		btnEditAR.setEnabled(b);

		int w;
//		if (b) {
			w = btnDelAR.getWidth();
			btnDelAR.setBounds(xo - w - 6, 2, w, 22);
			xo -= w + 2;
			w = btnEditAR.getWidth();
			btnEditAR.setBounds(xo - w - 6, 2, w, 22);
			xo -= w + 2; 
//		}
		w = btnNewAR.getWidth();
		btnNewAR.setBounds(xo - w - 6, 2, w, 22);
	}


	/**
	 * Called when the user clicks on the new contact button, in the 
	 * contact title bar
	 */
	protected void newContactClick() {
		ContactEditDlg dlg = new ContactEditDlg();
		if (dlg.openNew(getCaseId()) != null)
			refresh();
	}


	/**
	 * Open the comorbidity window. Called when the user clicks on the 
	 * edit button of the comorbidity title bar 
	 */
	protected void editComorbidities() {
		ComorbiditiesDlg dlg = new ComorbiditiesDlg();
		if (dlg.openForm(getCaseId()))
			refresh();
	}


	/** {@inheritDoc}
	 */
	@Override
	public void refreshInTransaction(TbCase tbcase) {
		refreshComorbidities();
		refreshContacts();
		refreshSideEffects();
	}
	
	
	/**
	 * Refresh the list of comorbidities in the panel by the case
	 */
	protected void refreshComorbidities() {
		TbCase tbcase = CaseServices.instance().findEntity(getCaseId());

		btnEditComorb.setVisible(tbcase.isOpen());

		if (frmComorbidities == null) {
			frmComorbidities = GuiUtils.createForm("comorbidities");
			JPanel pnlcom = (JPanel)frmComorbidities.getFormUI().getComponent();
			pnlcom.setBackground(Color.WHITE);
			pnlComorbidities.add(pnlcom);
			// temporary height
			pnlcom.setBounds(0, 0, pnlComorbidities.getWidth(), 1);
		}
		
		frmComorbidities.setValue("tbcase", tbcase);
		JPanel pnl = (JPanel)frmComorbidities.getFormUI().getComponent();

		int h;
		if (tbcase.getComorbidities().size() == 0) {
			txtComNoResult.setVisible(true);
			pnl.setVisible(false);
			h = 40;
		}
		else {
			txtComNoResult.setVisible(false);
			pnl.setVisible(true);
			System.out.println(tbcase.getComorbidities().size());
			frmComorbidities.getFormUI().update();
			h = (int)pnl.getPreferredSize().getHeight() + 10;
		}
		
		pnl.setSize(800, h);
		layoutContent.rowHeights[1] = h;
	}


	/**
	 * Refresh the list of contacts displayed in the panel
	 */
	protected void refreshContacts() {
		TbCase tbcase = CaseServices.instance().findEntity(getCaseId());
		btnNewContact.setVisible(tbcase.isOpen());

		List<TbContact> contacts = App.getEntityManager()
				.createQuery("from TbContact where tbcase.id = :id order by name")
				.setParameter("id", getCaseId())
				.getResultList();

		pnlContacts.removeAll();

		// no contact found ?
		if (contacts.size() == 0) {
			txtContNoResult.setVisible(true);
			layoutContent.rowHeights[3] = 40;
			pnlContacts.add(txtContNoResult);
			return;
		}
		
		txtContNoResult.setVisible(false);

		contactForms = new ArrayList<SwingFormContext>();
		int y = 4;
		int x = 20;
		
		for (TbContact contact: contacts) {
			// create a contact form
			SwingFormContext frm = GuiUtils.createForm("contact");
			
			frm.getFormUI().setReadOnly(true);
			frm.getFormUI().setPreferredWidth(600);

			// change background color of the contact form
			JPanel pnlContact = (JPanel)frm.getFormUI().getComponent();
			pnlContact.setBackground(Color.WHITE);

			// set the contact to be displayed
			frm.setValue("contact", contact);

			// update settings for this display
			frm.getFormUI().setReadOnly(true);
			frm.getFormUI().setFormPadding(2);
			frm.getFormUI().update();

			// set the contact bar width
			int w = 700;
			// add the header of the contact
			JPanel pnlTitle = new JPanel();
			pnlTitle.setLayout(null);
			pnlTitle.setBounds(x, y, w, 26);
			pnlContacts.add(pnlTitle);

			// display the name of the contact
			JLabel txt = new JLabel(contact.getName()); 
			txt.setFont(UiConstants.h3Font);
			txt.setBounds(6, 4, 300, 17);
			pnlTitle.add(txt);

			// if the case is open, display the buttons to edit and remove the contact
			if (tbcase.isOpen()) {
				JButton btnDel = new JTransactionalButton(Messages.getString("form.remove") + "...");
				pnlTitle.add(btnDel);
				int btnw = (int)btnDel.getPreferredSize().getWidth();
				int xo = w - btnw - 2;
				btnDel.setBounds(xo, 3, btnw, 22);
				btnDel.setActionCommand(contact.getId().toString());
				btnDel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Integer contactId = Integer.parseInt(((JButton)e.getSource()).getActionCommand());
						handleContactDelete( contactId );
					}
				});

				// display the button to edit the contact 
				JButton btnEdit = new JButton(Messages.getString("form.edit") + "...");
				pnlTitle.add(btnEdit);
				btnw = (int)btnEdit.getPreferredSize().getWidth();
				xo = xo - btnw - 6;
				btnEdit.setBounds(xo, 3, btnw, 22);
				btnEdit.setActionCommand(contact.getId().toString());
				btnEdit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Integer contactId = Integer.parseInt(((JButton)e.getSource()).getActionCommand());
						handleContactEdit( contactId );
					}
				});
			}

			// increase the y-position of the counter
			y += pnlTitle.getHeight();
			
			int h = (int)pnlContact.getPreferredSize().getHeight();
			pnlContact.setBounds(x, y, w, h);
			pnlContacts.add(pnlContact);
			
			y += h + 10;
			
			contactForms.add(frm);
		}
		layoutContent.rowHeights[3] = y + 10;
	}

	
	/**
	 * Open dialog window to edit the contact
	 * @param contactId
	 */
	protected void handleContactDelete(Integer contactId) {
		// search for contact in the list of contact forms
		for (SwingFormContext frm: contactForms) {
			TbContact contact = (TbContact)frm.getValue("contact");
			// found contact with id ?
			if (contactId.equals(contact.getId())) {
				// confirm delete ?
				if (JOptionPane.showConfirmDialog(getParent(), 
						Messages.getString("form.confirm_remove"), 
						Messages.getString("form.remove"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
					return;

				// delete from database
				ContactServices srv = App.getComponent(ContactServices.class);
				srv.delete(contactId);
				
				// refresh the content
				refreshContacts();
			}
		}
	}


	/**
	 * Called when the user clicks on the edit button of the contact
	 * @param contactId
	 */
	protected void handleContactEdit(Integer contactId) {
		// search for contact in the list of contact forms
		for (SwingFormContext frm: contactForms) {
			Integer id = (Integer)frm.getValue("contact.id");
//			EntityServices<TbContact> controller = frm.getController("contact");
			// found contact with id ?
			if (contactId.equals(id)) {
				// open form for editing
				ContactEditDlg dlg = new ContactEditDlg();
				if (dlg.openEdit(contactId) != null)
					refresh();
			}
		}
	}


	/**
	 * Refresh the list displayed with the adverse reaction of the case
	 */
	protected void refreshSideEffects() {
		TbCase tbcase = CaseServices.instance().findEntity(getCaseId());

		btnNewAR.setVisible(tbcase.isOpen());
		btnDelAR.setVisible(tbcase.isOpen());
		btnEditAR.setVisible(tbcase.isOpen());

		if (frmSideEffects == null) {
			// create the side effects form
			frmSideEffects = GuiUtils.createForm("sideeffects");
			frmSideEffects.addEventHandler(this);
			JPanel pnlse = (JPanel)frmSideEffects.getFormUI().getComponent();
			pnlse.setBackground(Color.WHITE);
			pnlSideEffects.add(pnlse);
			// temporary height
			pnlse.setBounds(0, 0, pnlSideEffects.getWidth(), 1);
		}

		frmSideEffects.setValue("tbcase", tbcase);
		JPanel pnl = (JPanel)frmSideEffects.getFormUI().getComponent();
		//TbCase tbcase = (TbCase)frmSideEffects.getController("tbcase").getInstance();

		int h;
		if (tbcase.getSideEffects().size() == 0) {
			txtSeNoResult.setVisible(true);
			pnl.setVisible(false);
			h = 40;
		}
		else {
			txtSeNoResult.setVisible(false);
			pnl.setVisible(true);
			frmSideEffects.getFormUI().update();
			h = (int)pnl.getPreferredSize().getHeight() + 10;
		}
		
		pnl.setSize(800, h);
		layoutContent.rowHeights[5] = h;
		updateComorbiditesButtons();
	}


	/** {@inheritDoc}
	 */
	@Override
	public void onFormEvent(FormEvent event) {
		if ("row-select".equals(event.getEvent()))
			setSelectedSideEffect((CaseSideEffect)event.getData());
	}


	/**
	 * Return the side effect selected by the user in the side effect table
	 * @return instance of {@link CaseSideEffect}
	 */
	public CaseSideEffect getSelectedSideEffect() {
		return selectedSideEffect;
	}


	/**
	 * Inform the panel the case side effect selected. A value of null will clear the selection 
	 * @param selectedSideEffect instance of {@link CaseSideEffect}
	 */
	public void setSelectedSideEffect(CaseSideEffect selectedSideEffect) {
		this.selectedSideEffect = selectedSideEffect;
		
		updateComorbiditesButtons();
	}
}
