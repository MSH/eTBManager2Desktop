package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.GenericDialog;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.PatientNameCellRenderer;
import org.msh.etbm.desktop.components.JDatePicker;
import org.msh.etbm.desktop.components.JPersonNameEdit;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.services.cases.PatientData;
import org.msh.etbm.services.cases.PatientServices;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.LocaleDateConverter;

import com.toedter.calendar.JDateChooser;


/**
 * Dialog box to select the patient to be notified (or select a new patient)
 * @author Ricardo Memoria
 *
 */
public class NewNotificationDlg extends GenericDialog {
	private static final long serialVersionUID = 1278701357018464961L;

	private JPersonNameEdit edtName;
	private JDateChooser edtBirthDate;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel txtCount;
	private JCheckBox chkNewPatient;
	private Patient patient;

	/**
	 * Launch the new notification form
	 */
	public static Patient execute(DiagnosisType diagtype) {
		NewNotificationDlg dialog = new NewNotificationDlg();
		return dialog.openDialog(diagtype);
	}

	
	/**
	 * Open the new notification dialog box and prepare form
	 * @return
	 */
	protected Patient openDialog(DiagnosisType diagtype) {
		scrollPane.setVisible(false);
		setBounds(getX(), getY(), getWidth(), getHeight() - 180);
		getOkButton().setEnabled(false);
		chkNewPatient.setVisible(false);
	
		if (diagtype == DiagnosisType.CONFIRMED)
			 setTitle(Messages.getString("cases.new"));
		else setTitle(Messages.getString("cases.newsusp"));
		
		GuiUtils.prepareTable(table);
		
		
		if (!showModal())
			return null;

		if (table.getSelectedRow() >= 0) {
			patient = (Patient)table.getValueAt(table.getSelectedRow(), 0);
		}
		else {
			patient = new Patient();
			patient.setName(edtName.getPersonName());
			patient.setBirthDate(edtBirthDate.getDate());
		}
		
		return patient;
	}

	
	/**
	 * Search button click action
	 */
	protected void btnSearchClick() {
		// execute the patient search inside a transaction
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				PatientServices srv = App.getComponent(PatientServices.class);
				// return the list of patients
				List<PatientData> lst = srv.getPatients(edtName.getPersonName(), 
						edtBirthDate.getDate(), 0, 1000, "p.name.name");
				
				updateTable(lst);
			}
		});

		
		if (!scrollPane.isVisible()) {
			scrollPane.setVisible(true);
			chkNewPatient.setVisible(true);
			setBounds(getX(), getY(), getWidth(), getHeight() + 250);
		}
	}


	/**
	 * Update the table with the search result of the patient name and birth date
	 * @param patients
	 */
	private void updateTable(List<PatientData> patients) {
		DefaultTableModel model = (DefaultTableModel)table.getModel();

		int count = patients.size();

		if (count > 10000)
			count = 10000;
		
		if (count == 0) {
			 table.setVisible(false);
			 model.setRowCount(0);
			 txtCount.setText(Messages.getString("form.norecordfound"));
			 return;
		}

		model.setRowCount(0);

		for (PatientData item: patients) {
			// avoid lazy initialization
			item.getPatient().getWorkspace().getId();
			// feed the data model
			Object[] vals = {
					item.getPatient(),
					item.getPatient().getRecordNumber(),
					item.getPatient().getMotherName(),
					item.getPatient().getBirthDate() != null? LocaleDateConverter.getDisplayDate(item.getPatient().getBirthDate(), false): "",
					item.getCaseStatus()};
			model.addRow(vals);
		}

		model.fireTableDataChanged();
		table.setVisible(true);

		table.getColumnModel().getColumn(0).setCellRenderer(new PatientNameCellRenderer());
		
		DecimalFormat format = new DecimalFormat("#,###,##0");
		txtCount.setText( "<html><b>" + Messages.getString("form.result") + ": </b>" +  format.format(count) + " " + Messages.getString("form.resultlist") +"</html>");
	}


	/**
	 * Create the dialog.
	 */
	@SuppressWarnings("serial")
	public NewNotificationDlg() {
		setResizable(true);
		setTitle(Messages.getString("cases.newnotif")); //$NON-NLS-1$
		setBounds(100, 100, 880, 382);
		getClientContent().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getClientContent().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel(Messages.getString("cases.casesearching.patient")); //$NON-NLS-1$
		
		JLabel lblNewLabel_1 = new JLabel(Messages.getString("Patient.birthDate")); //$NON-NLS-1$
		
		edtName = new JPersonNameEdit();
//		edtName.setColumns(10);
		
		edtBirthDate = new JDatePicker();
		
		JButton btnNewButton = new JButton(Messages.getString("form.search")); //$NON-NLS-1$
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSearchClick();
			}
		});
		
		txtCount = new JLabel((String) null); //$NON-NLS-1$ //$NON-NLS-1$
		txtCount.setHorizontalAlignment(SwingConstants.RIGHT);
		
		chkNewPatient = new JCheckBox(Messages.getString("cases.newpatient")); //$NON-NLS-1$
		chkNewPatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newPatientSelected();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(chkNewPatient)
							.addPreferredGap(ComponentPlacement.RELATED, 305, Short.MAX_VALUE)
							.addComponent(txtCount, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(edtName, GroupLayout.PREFERRED_SIZE, edtName.getWidth(), GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
								.addComponent(edtBirthDate, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(edtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(edtBirthDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(chkNewPatient)
						.addComponent(txtCount))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		scrollPane = new JScrollPane();
		getClientContent().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkTableSelection();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkTableSelection();
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Patient.name", "Patient.recordNumber", "Patient.motherName", "Patient.birthDate", "cases.sit.current"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(190);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(110);
		table.getColumnModel().getColumn(4).setPreferredWidth(300);
		scrollPane.setViewportView(table);
	}


	protected void newPatientSelected() {
		table.getSelectionModel().clearSelection();
		checkOkButton();
	}


	/**
	 * Standard method handler called when the user selects a row in the table
	 */
	protected void checkTableSelection() {
		if (table.getSelectedRow() >= 0)
			chkNewPatient.setSelected(false);
		checkOkButton();
	}


	/**
	 * Enable or disable the OK button according to the information in the form. The button
	 * will be enabled if there is any patient selected in the table or if the new patient
	 * check box is checked
	 */
	public void checkOkButton() {
		getOkButton().setEnabled( ((chkNewPatient.isSelected()) || (table.getSelectedRow() >= 0)) );
	}



	/**
	 * Return the instance of the patient in use
	 * @return instance of {@link Patient}
	 */
	public Patient getPatient() {
		return patient;
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.GenericDialog#save()
	 */
	@Override
	public boolean save() {
		return true;
	}
}
