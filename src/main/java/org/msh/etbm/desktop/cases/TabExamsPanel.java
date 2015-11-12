package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.msh.etbm.custom.bd.ExamBiopsyEditDlg;
import org.msh.etbm.custom.bd.ExamBiopsyServices;
import org.msh.etbm.custom.bd.ExamSkinEditDlg;
import org.msh.etbm.custom.bd.ExamSkinServices;
import org.msh.etbm.custom.bd.entities.ExamBiopsy;
import org.msh.etbm.custom.bd.entities.ExamSkin;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.JWarningLabel;
import org.msh.etbm.desktop.common.SyncCellRenderer;
import org.msh.etbm.desktop.common.TableCellID;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.components.JTransactionalButton;
import org.msh.etbm.entities.CaseData;
import org.msh.etbm.entities.ExamCulture;
import org.msh.etbm.entities.ExamDST;
import org.msh.etbm.entities.ExamHIV;
import org.msh.etbm.entities.ExamMicroscopy;
import org.msh.etbm.entities.ExamXRay;
import org.msh.etbm.entities.ExamXpert;
import org.msh.etbm.entities.LaboratoryExamResult;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.cases.ExamCultureServices;
import org.msh.etbm.services.cases.ExamDSTServices;
import org.msh.etbm.services.cases.ExamHivServices;
import org.msh.etbm.services.cases.ExamMicroscopyServices;
import org.msh.etbm.services.cases.ExamXRayServices;
import org.msh.etbm.services.cases.ExamXpertServices;
import org.msh.etbm.services.core.EntityServices;
import org.msh.etbm.services.login.UserSession;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.LocaleDateConverter;
import org.msh.xview.swing.SwingFormContext;

/**
 * Display the panel with exams of a case
 * @author Ricardo Memoria
 *
 */
public class TabExamsPanel extends CaseTabPanel {

	private static final long serialVersionUID = 34012183235413136L;
	private JTable table;
	private JPanel pnlTop;
	private JSplitPane splitPane;
	private JCheckBox chkHIV;
	private JCheckBox chkMicroscopy;
	private JCheckBox chkCulture;
	private JCheckBox chkDST;
	private JCheckBox chkXRay;
	private JCheckBox chkXpert;
	private JCheckBox chkSkin;
	private JCheckBox chkBiopsy;

	private Map<String, SwingFormContext> forms = new HashMap<String, SwingFormContext>();
	private JLabel txtCount;
	private JPopupMenu popupMenu;
	private JMenuItem cmdHiv;
	private JMenuItem cmdMicroscopy;
	private JMenuItem cmdCulture;
	private JMenuItem cmdDst;
	private JMenuItem cmdXRay;
	private JMenuItem cmdXpert;
	private JMenuItem cmdSkin;
	private JMenuItem cmdBiopsy;
	private JButton btnNew;
	private JButton btnEdit;
	private JLabel lblExamTitle;
	private JPanel pnlExamDetail;
	private JTransactionalButton btnDelete;
	private SwingFormContext currentForm; 


	/**
	 * Default constructor
	 */
	@SuppressWarnings("serial")
	public TabExamsPanel() {
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);
		
		table = new JTable();
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane.setLeftComponent(scrollPane);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2) && (btnEdit.isVisible()))
					btnEdit.doClick();
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
			},
			new String[] {
				"", "@cases.details.exams", "@cases.exams.date", "@ExamStatus" , "@cases.sideeffects.month", "@cases.details.result", "@Laboratory"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(24);
		table.getColumnModel().getColumn(1).setPreferredWidth(190);
		table.getColumnModel().getColumn(2).setPreferredWidth(130);
		table.getColumnModel().getColumn(3).setPreferredWidth(140);
		table.getColumnModel().getColumn(4).setPreferredWidth(160);
		table.getColumnModel().getColumn(5).setPreferredWidth(220);
		scrollPane.setViewportView(table);
		
		pnlTop = new JPanel();
		add(pnlTop, BorderLayout.NORTH);
		pnlTop.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		pnlTop.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnNew = new JButton(Messages.getString("form.new") + "...");
		btnNew.setIcon(new AwesomeIcon(AwesomeIcon.ICON_PLUS, btnNew));
		btnNew.addActionListener(new ActionListener() {
			// called when user clicks on the new button
		    	@Override
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(btnNew, 0, btnNew.getHeight() + 1);
			}
		});
		panel.add(btnNew);
		
		popupMenu = new JPopupMenu();

		ActionListener cmdNewListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!btnEdit.isVisible())
					return;
				CaseDataEditDlg dlg = null;
				if (e.getSource() == cmdHiv)
					dlg = createEditDialog(ExamHIV.class);
				else 
				if (e.getSource() == cmdMicroscopy)
					dlg = createEditDialog(ExamMicroscopy.class);
				else
				if (e.getSource() == cmdCulture)
					dlg = createEditDialog(ExamCulture.class);
				else
				if (e.getSource() == cmdDst)
					dlg = createEditDialog(ExamDST.class);
				else
				if (e.getSource() == cmdHiv)
					dlg = createEditDialog(ExamDST.class);
				else
				if (e.getSource() == cmdXRay)
					dlg = createEditDialog(ExamXRay.class);
				else
				if (e.getSource() == cmdXpert)
					dlg = createEditDialog(ExamXpert.class);
				else
				if(e.getSource() == cmdSkin)
					dlg = createEditDialog(ExamSkin.class);
				else
				if(e.getSource() == cmdBiopsy)
					dlg = createEditDialog(ExamBiopsy.class);
						
				
				if (dlg == null)
					return;
				
				if (dlg.openNew(getCaseId()) != null)
					refresh();
			}
		};
		
		cmdMicroscopy = new JMenuItem(Messages.getString("cases.exammicroscopy") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdMicroscopy);
		cmdMicroscopy.addActionListener(cmdNewListener);
		
		cmdXpert = new JMenuItem(Messages.getString("cases.examxpert") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdXpert);
		cmdXpert.addActionListener(cmdNewListener);
		
		cmdCulture = new JMenuItem(Messages.getString("cases.examculture") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdCulture);
		cmdCulture.addActionListener(cmdNewListener);
		
		cmdDst = new JMenuItem(Messages.getString("cases.examdst") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdDst);
		cmdDst.addActionListener(cmdNewListener);
		
		cmdXRay = new JMenuItem(Messages.getString("cases.examxray") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdXRay);
		cmdXRay.addActionListener(cmdNewListener);
		
		cmdHiv = new JMenuItem(Messages.getString("cases.examhiv") + "..."); //$NON-NLS-1$
		popupMenu.add(cmdHiv);
		cmdHiv.addActionListener(cmdNewListener);

		if("bd".equals(UserSession.getWorkspace().getExtension())){
			cmdSkin = new JMenuItem(Messages.getString("cases.examskintest") + "..."); //$NON-NLS-1$
			popupMenu.add(cmdSkin);
			cmdSkin.addActionListener(cmdNewListener);

			cmdBiopsy = new JMenuItem(Messages.getString("cases.exambiopsy") + "..."); //$NON-NLS-1$
			popupMenu.add(cmdBiopsy);
			cmdBiopsy.addActionListener(cmdNewListener);
		}
		
		btnEdit = new JButton(Messages.getString("form.edit") + "...");
		btnEdit.setIcon(new AwesomeIcon(AwesomeIcon.ICON_EDIT, btnEdit));
		btnEdit.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				// called when user clicks on the edit button
				Object entity = getSelectedEntity();
				if (entity == null)
					return;
				CaseDataEditDlg dlg = createEditDialog(entity.getClass());
				if (dlg.openEdit(getEntityId(entity)) != null)
					refresh();
			}
		});
		panel.add(btnEdit);
		
		btnDelete = new JTransactionalButton(Messages.getString("form.remove"));
		btnDelete.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE, btnDelete));
		btnDelete.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				// called when user clicks on the delete button
				Object entity = getSelectedEntity();
				if (entity == null)
					return;

				if (JOptionPane.showConfirmDialog(getParent(), 
						Messages.getString("form.confirm_remove"), 
						Messages.getString("form.remove"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
					return;
				entity = App.getEntityManager().merge(entity);
				App.getEntityManager().remove(entity);
				refresh();
				if(entity instanceof LaboratoryExamResult)
					EventBusService.raiseEvent(AppEvent.EXAMS_MODIFIED, entity);
			}
		});
		panel.add(btnDelete);
		
		txtCount = new JWarningLabel(Messages.noRecordFound());
		panel.add(txtCount);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlTop.add(panel_3, BorderLayout.NORTH);

		ItemListener checkboxListener = new ItemListener() {
		    	@Override
			public void itemStateChanged(ItemEvent e) {
				refresh();
			}
		};
		
		chkMicroscopy = new JCheckBox(Messages.getString("cases.exammicroscopy"));
		chkMicroscopy.setSelected(true);
		chkMicroscopy.addItemListener(checkboxListener);
		panel_3.add(chkMicroscopy);
		
		chkXpert = new JCheckBox(Messages.getString("cases.examxpert"));
		chkXpert.setSelected(true);
		chkXpert.addItemListener(checkboxListener);
		panel_3.add(chkXpert);
		
		chkCulture = new JCheckBox(Messages.getString("cases.examculture"));
		chkCulture.setSelected(true);
		chkCulture.addItemListener(checkboxListener);
		panel_3.add(chkCulture);
		
		chkDST = new JCheckBox(Messages.getString("cases.examdst"));
		chkDST.setSelected(true);
		chkDST.addItemListener(checkboxListener);
		panel_3.add(chkDST);
		
		chkXRay = new JCheckBox(Messages.getString("cases.examxray"));
		chkXRay.setSelected(true);
		chkXRay.addItemListener(checkboxListener);
		panel_3.add(chkXRay);
		
		chkHIV = new JCheckBox(Messages.getString("cases.examhiv"));
		chkHIV.setSelected(true);
		chkHIV.addItemListener(checkboxListener);
		panel_3.add(chkHIV);

		if("bd".equals(UserSession.getWorkspace().getExtension())){
			chkSkin = new JCheckBox(Messages.getString("cases.examskintest"));
			chkSkin.setSelected(true);
			chkSkin.addItemListener(checkboxListener);
			panel_3.add(chkSkin);

			chkBiopsy = new JCheckBox(Messages.getString("cases.exambiopsy"));
			chkBiopsy.setSelected(true);
			chkBiopsy.addItemListener(checkboxListener);
			panel_3.add(chkBiopsy);
		}
		
		GuiUtils.prepareTable(table);
		table.getColumnModel().getColumn(0).setCellRenderer(new SyncCellRenderer());
		
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableRowSelectionChange();
			}
		});

		// add the content at the bottom of the split panel
		lblExamTitle = new JLabel();
		lblExamTitle.setFont(UiConstants.h2Font);
		pnlExamDetail = new JPanel();
		pnlExamDetail.setBackground(UiConstants.formBackground);
		pnlExamDetail.setLayout(new BorderLayout(0,0));
		pnlExamDetail.setBorder(new EmptyBorder(4,4,4,4));
		pnlExamDetail.add(lblExamTitle, BorderLayout.PAGE_START);
		splitPane.setRightComponent(pnlExamDetail);
	}


	/**
	 * Create the dialog window responsible for editing the given type
	 * @param clazz
	 * @return
	 */
	protected CaseDataEditDlg createEditDialog(Class clazz) {
		if (clazz.isAssignableFrom(ExamCulture.class))
			return new ExamCultureEditDlg();

		if (clazz.isAssignableFrom(ExamHIV.class))
			return new ExamHivEditDlg();

		if (clazz.isAssignableFrom(ExamMicroscopy.class))
			return new ExamMicroscopyEditDlg();

		if (clazz.isAssignableFrom(ExamDST.class))
			return new ExamDSTEditDlg();

		if (clazz.isAssignableFrom(ExamXRay.class))
			return new ExamXRayEditDlg();

		if (clazz.isAssignableFrom(ExamXpert.class))
			return new ExamXpertEditDlg();

		if (clazz.isAssignableFrom(ExamSkin.class))
			return new ExamSkinEditDlg();

		if (clazz.isAssignableFrom(ExamBiopsy.class))
			return new ExamBiopsyEditDlg();

		throw new IllegalArgumentException("No edit window supported to class " + clazz.getName());
	}


	/**
	 * Return the entity displayed in the selected row of the table
	 * @return
	 */
	protected Object getSelectedEntity() {
		int row = table.getSelectedRow();
		if (row == -1)
			return null;
		TableCellID cell = (TableCellID)table.getValueAt(row, 1);
		if (cell == null)
			return null;

		return cell.getValue();
	}


	/**
	 * Called when the row selection is changed
	 */
	protected void tableRowSelectionChange() {
		Object exam = getSelectedEntity();
		if (exam == null)
			return;

		updateForm(exam);
	}


	/** {@inheritDoc}
	 */
	@Override
	public void refreshInTransaction(TbCase tbcase) {
		Object selentity = getSelectedEntity();

		btnNew.setVisible(tbcase.isOpen());
		btnEdit.setVisible(tbcase.isOpen());
		btnDelete.setVisible(tbcase.isOpen());
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();

		model.setRowCount(0);

		// is microscopy selected ?
		if (chkMicroscopy.isSelected()) {
			List<ExamMicroscopy> lst = (new ExamMicroscopyServices()).getList(getCaseId());
			String type = Messages.getString("cases.exammicroscopy");
			for (ExamMicroscopy exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		// is culture selected ?
		if (chkCulture.isSelected()) {
			List<ExamCulture> lst = (new ExamCultureServices()).getList(getCaseId());
			String type = Messages.getString("cases.examculture");
			for (ExamCulture exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		// is Xpert selected ?
		if (chkXpert.isSelected()) {
			List<ExamXpert> lst = (new ExamXpertServices()).getList(getCaseId());
			String type = Messages.getString("cases.examxpert");
			for (ExamXpert exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		// is dst selected ?
		if (chkDST.isSelected()) {
			List<ExamDST> lst = (new ExamDSTServices()).getList(getCaseId());
			String type = Messages.getString("cases.examdst");
			for (ExamDST exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						null,
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		// is XRay selected ?
		if (chkXRay.isSelected()) {
			List<ExamXRay> lst = (new ExamXRayServices()).getList(getCaseId());
			String type = Messages.getString("cases.examxray");
			for (ExamXRay exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDate(), false ) ,
						null,
					 	exam.getMonthDisplay(),
						Messages.getString( exam.getEvolution() != null? exam.getEvolution().getKey() : "" ),
						null};
				model.addRow(vals);
			}
		}

		// is HIV selected ?
		if (chkHIV.isSelected()) {
			List<ExamHIV> lst = (new ExamHivServices()).getList(getCaseId());
			String type = Messages.getString("cases.examhiv");
			for (ExamHIV exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDate(), false ) ,
						null,
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}

		}

		// is ExamSkin selected ?
		if (chkSkin.isSelected()) {
			List<ExamSkin> lst = (new ExamSkinServices()).getList(getCaseId());
			String type = Messages.getString("cases.examskintest");
			for (ExamSkin exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		// is ExamBiopsy selected ?
		if (chkBiopsy.isSelected()) {
			List<ExamBiopsy> lst = (new ExamBiopsyServices()).getList(getCaseId());
			String type = Messages.getString("cases.exambiopsy");
			for (ExamBiopsy exam: lst) {
				Object[] vals = {
						exam.getSyncData(),
						new TableCellID(exam, type),
						LocaleDateConverter.getDisplayDate( exam.getDateCollected(), false ) ,
						(exam.getStatus() != null ? Messages.getString(exam.getStatus().getKey()) : ""),
						exam.getMonthDisplay(),
						(exam.getResult() != null ? Messages.getString( exam.getResult().getKey()) : ""),
						exam.getLaboratory()};
				model.addRow(vals);
			}
		}

		if (model.getRowCount() == 0) {
			splitPane.setVisible(false);
			model.setRowCount(0);
			txtCount.setText(Messages.getString("form.norecordfound"));
				//txtCount.setForeground(new Color(255, 140, 0));
			//txtCount.setFont(new Font("Tahoma", Font.BOLD, 12));
			return;
		}

		GuiUtils.updateRecordCountLabel(txtCount, model.getRowCount());
		txtCount.setIcon(null);
		if (model.getRowCount() == 0) {
			splitPane.setVisible(false);
			return;
		}
		
		int selrow = 0;
		if (selentity != null) {
			int entid = getEntityId(selentity);
			for (int row = 0; row < model.getRowCount(); row++) {
				Object ent = ((TableCellID)model.getValueAt(row, 1)).getValue();
				if ((ent.getClass() == selentity.getClass()) && (getEntityId(ent) == entid)) {
					selrow = row;
					break;
				}
			}
		}
		
		table.setRowSelectionInterval(selrow, selrow);
		model.fireTableDataChanged();
		splitPane.setVisible(true);
	}

	
	/**
	 * Update the content of the form displaying details about the exam
	 * @param exam
	 */
	protected void updateForm(Object exam) {
		EntityManagerUtils.doInTransaction(new ActionCallback(exam) {
			@Override
			public void execute(Object exam) {
				String varname = null;
				Class<? extends EntityServices> serviceClass = null;
				SwingFormContext form = null;
				String title = "";

				final Integer id = getEntityId(exam);

				if (exam instanceof ExamHIV) {
					varname = "examhiv";
					serviceClass = ExamHivServices.class;
					title = App.getMessage("cases.examhiv");
				}
				else if (exam instanceof ExamMicroscopy) {
					// initialize microscopy form
					varname = "exammicroscopy";
					serviceClass = ExamMicroscopyServices.class;
					title = App.getMessage("cases.exammicroscopy");
				}
				else if (exam instanceof ExamCulture) {
					// initialize culture form
					varname = "examculture";
					serviceClass = ExamCultureServices.class;
					title = App.getMessage("cases.examculture");
				}
				else if (exam instanceof ExamDST) {
					varname = "examdst";
					serviceClass = ExamDSTServices.class;
					title = App.getMessage("cases.examdst");
				}
				else if (exam instanceof ExamXRay) {
					varname = "examxray";
					serviceClass = ExamXRayServices.class;
					title = App.getMessage("cases.examxray");
				}
				else if (exam instanceof ExamXpert) {
					varname = "examxpert";
					serviceClass = ExamXpertServices.class;
					title = App.getMessage("cases.examxpert");
				}else if (exam instanceof ExamSkin) {
					// initialize skin form
					varname = "examskin";
					serviceClass = ExamSkinServices.class;
					title = App.getMessage("cases.examskintest");
				}else if (exam instanceof ExamBiopsy) {
					// initialize biopsy form
					varname = "exambiopsy";
					serviceClass = ExamBiopsyServices.class;
					title = App.getMessage("cases.exambiopsy");
				}

				// get an instance of the form
				form = forms.get(varname);
				if (form == null) {
					form = GuiUtils.createForm(varname);
					form.getFormUI().setReadOnly(true);
					forms.put(varname, form);
				}

				EntityServices srv = App.getComponent(serviceClass);
				form.setValue(varname, srv.findEntity(id));
				form.getFormUI().getComponent().setBackground(UiConstants.formBackground);

				// is the new form different from the current form ?
				if (currentForm != form) {
					if (currentForm != null) {
						// remove the previous form
						pnlExamDetail.remove(currentForm.getScrollPaneForm());
					}
					// update the current form
					currentForm = form;
					// add the current form to the panel
					pnlExamDetail.add(form.getScrollPaneForm(), BorderLayout.CENTER);
				}
				form.getFormUI().setPreferredWidth(600);
				form.getFormUI().update();
				lblExamTitle.setText(title);
			}
		});
	}


	/**
	 * Create an instance of the form with the given variable
	 * @param varname
	 * @return
	 */
	protected SwingFormContext createForm(String varname) {
		SwingFormContext form = GuiUtils.createForm(varname);
		form.getFormUI().setReadOnly(true);

		form.getFormUI().getComponent().setBackground(new Color(255,255,255));
		return form;
	}

	
	/**
	 * Return the ID of the given entity
	 * @param entity
	 * @return
	 */
	public Integer getEntityId(Object entity) {
		if (entity instanceof CaseData)
			 return ((CaseData)entity).getId();
		else return ((LaboratoryExamResult)entity).getId();
	}
}
