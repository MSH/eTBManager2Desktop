package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.SyncCellRenderer;
import org.msh.etbm.desktop.common.TableCellID;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.components.JTransactionalButton;
import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.entities.TbCase;
import org.msh.utils.date.LocaleDateConverter;
import org.msh.xview.swing.SwingFormContext;


/**
 * Swing panel to display the medical examination read-only form. It's displayed as part
 * of the case detail page when a medical examination is selected in the medical examination tab
 * 
 * @author Ricardo Memoria
 *
 */
public class TabMedExamsPanel extends CaseTabPanel {
	private static final long serialVersionUID = -2214105704061947881L;
	private JTable table;
	
	private JLabel txtCount;
	private JSplitPane splitPane;
	private SwingFormContext form;
	
	private JButton btnNew;
	private JButton btnEdit;
	private JButton btnDelete;


	/**
	 * Create the panel.
	 */
	@SuppressWarnings("serial")
	public TabMedExamsPanel() {
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.4);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);
		
		JPanel panelTop = new JPanel();
		splitPane.setLeftComponent(panelTop);
		panelTop.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setBackground(new Color(255, 255, 255));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"", "@cases.details.date", "@cases.sideeffects.month", "@MedicalExamination.weight",
				"@MedicalExamination.BMI", "@MedicalExamination.usingPrescMedicines", "@MedicalExamination.responsible"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(24);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(60);
		table.getColumnModel().getColumn(5).setPreferredWidth(360);
		table.getColumnModel().getColumn(6).setPreferredWidth(320);
		JScrollPane scrollPane = new JScrollPane(table);
		panelTop.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnNew = new JButton(Messages.getString("form.new") + "..."); //$NON-NLS-1$
		btnNew.setIcon(new AwesomeIcon(AwesomeIcon.ICON_PLUS, btnNew));
		btnNew.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				MedExamEditDlg dlg = new MedExamEditDlg();
				if (dlg.openNew(getCaseId()) != null)
					refresh();
			}
		});
		panel_1.add(btnNew);
		
		btnEdit = new JButton(Messages.getString("form.edit") + "..."); //$NON-NLS-1$
		btnEdit.setIcon(new AwesomeIcon(AwesomeIcon.ICON_EDIT, btnEdit));
		btnEdit.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				MedicalExamination med = getSelectedEntity();
				if (med == null)
					return;
				MedExamEditDlg dlg = new MedExamEditDlg();
				if (dlg.openEdit(med.getId()) != null)
					refresh();
			}
		});
		panel_1.add(btnEdit);
		
		btnDelete = new JTransactionalButton(Messages.getString("form.remove")); //$NON-NLS-1$
		btnDelete.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE, btnDelete));
		btnDelete.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				// called when user clicks on the delete button
				MedicalExamination med = getSelectedEntity();
				if (med == null)
					return;

				if (JOptionPane.showConfirmDialog(getParent(), 
						Messages.getString("form.confirm_remove"), 
						Messages.getString("form.remove"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
					return;

				med = App.getEntityManager().merge(med);
				App.getEntityManager().remove(med);
				refresh();
			}
		});
		panel_1.add(btnDelete);
		
		txtCount = new JLabel(Messages.getString("form.norecordfound")); //$NON-NLS-1$
		panel_1.add(txtCount);
		
		ListSelectionModel selModel = table.getSelectionModel();
		selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableRowSelectionChange();
			}
		});
		
		GuiUtils.prepareTable(table);
		table.getColumnModel().getColumn(0).setCellRenderer(new SyncCellRenderer());
	}


	/**
	 * Return the entity displayed in the selected row of the table
	 * @return
	 */
	protected MedicalExamination getSelectedEntity() {
		int row = table.getSelectedRow();
		if (row == -1)
			return null;
		TableCellID cell = (TableCellID)table.getValueAt(row, 1);
		if (cell == null)
			return null;

		return (MedicalExamination)cell.getValue();
	}


	/**
	 * Called when the row selection is changed
	 */
	protected void tableRowSelectionChange() {
		MedicalExamination med = getSelectedEntity();
		if (med == null)
			return;

		updateForm(med);
	}


	/**
	 * Update form content with the data from the {@link MedicalExamination} object
	 * @param value
	 */
	protected void updateForm(MedicalExamination medexam) {
		if (form == null) {
			form = GuiUtils.createForm("medexam");
			form.getFormUI().setReadOnly(true);

			form.getFormUI().getComponent().setBackground(new Color(255,255,255));
			splitPane.setRightComponent(form.getScrollPaneForm());

			form.getDataModel().setValue("medicalexamination", medexam);
			form.getFormUI().update();
		}
		else form.getDataModel().setValue("medicalexamination", medexam);
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.Refreshable#refresh()
	 */
	@Override
	public void refreshInTransaction(TbCase tbcase) {
		btnNew.setVisible(tbcase.isOpen());
		btnEdit.setVisible(tbcase.isOpen());
		btnDelete.setVisible(tbcase.isOpen());

		List<MedicalExamination> meds = App.getEntityManager()
				.createQuery("from MedicalExamination where tbcase.id = :id order by date desc")
				.setParameter("id", tbcase.getId())
				.getResultList();

		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int count = meds.size();

		if (count == 0) {
			splitPane.setVisible(false);
			model.setRowCount(0);
			GuiUtils.updateRecordCountLabel(txtCount, 0);
			return;
		}

		model.setRowCount(0);

		DecimalFormat df = new DecimalFormat("#,##0.0");
		
		for (MedicalExamination item: meds) {
			String s = item.getUsingPrescMedicines() != null? App.getMessage( item.getUsingPrescMedicines().getMessageKey() ): "";
					
			Object[] vals = {
					item.getSyncData(),
					new TableCellID(item, LocaleDateConverter.getDisplayDate( item.getDate(), false ) ),
					item.getMonthDisplay(),
					item.getWeight() != null?
					Double.toString( item.getWeight() ) + " " + Messages.getString("MedicalExamination.weight.unit"):
					"-",
					df.format(item.getBMI()),
					s,
					item.getResponsible()};
			model.addRow(vals);
		}

		table.setRowSelectionInterval(0, 0);
		model.fireTableDataChanged();
		splitPane.setVisible(true);

		GuiUtils.updateRecordCountLabel(txtCount, count);
	}

}
