/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.common.AppTableCellRenderer;
import org.msh.etbm.desktop.common.GenericDialog;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.Medicine;
import org.msh.etbm.services.MedicineServices;
import org.msh.utils.ItemSelect;

/**
 * Display a dialog window listing all medicines where user is able to select
 * multiple medicines
 * @author Ricardo Memoria
 *
 */
public class MedicineSelectionDlg extends GenericDialog {
	private static final long serialVersionUID = -2918745443637621547L;

	private final AwesomeIcon iconChecked = new AwesomeIcon(AwesomeIcon.ICON_CHECK, Color.DARK_GRAY, 18);
	private final AwesomeIcon iconUnchecked = new AwesomeIcon(AwesomeIcon.ICON_CHECK_EMPTY, Color.DARK_GRAY, 18);

	private JTable table;
	
	@SuppressWarnings("serial")
	public MedicineSelectionDlg() {
		super();
		
		table = new JTable();
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setBackground(new Color(255, 255, 255));
		table.setModel(new DefaultTableModel(
				null,
				new String[] {
					App.getMessage("Medicine")
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false
				};
				@Override
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(300);
		// set the row renderer
		col.setCellRenderer(new AppTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				ItemSelect<Medicine> item = (ItemSelect<Medicine>)table.getModel().getValueAt(row, column);
				setText(item.getItem().toString());
				if (item.isSelected()) {
					Color bkColor = UIManager.getDefaults().getColor("List.selectionBackground");
					setBackground(bkColor);
					iconChecked.setColor(getForeground());
					setIcon(iconChecked);
				}
				else {
					iconChecked.setColor(getForeground());
					setIcon(iconUnchecked);
				}
				
				return comp;
			}
		});

		// set the selection mode and the table row click listener
		ListSelectionModel selmodel = table.getSelectionModel();
		selmodel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selmodel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				int selrow = table.getSelectedRow();
				ItemSelect item = (ItemSelect)table.getModel().getValueAt(selrow, 0);
				item.setSelected(!item.isSelected());
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		setSize(500, 350);
		setResizable(true);
		table.setRowHeight(25);
	}
	
	/**
	 * List of medicines to be selected
	 */
	private List<Medicine> medicines;

	/**
	 * Display the dialog window to select the medicines
	 * @param preselection is the list of medicines that will be initially selected, 
	 * or null, if none is selected
	 * @return list of {@link Medicine} objects that were selected by the user
	 */
	public static List<Medicine> execute(List<Medicine> preselection) {
		MedicineSelectionDlg dlg = new MedicineSelectionDlg();
		dlg.setMedicines(preselection);
		if (dlg.showModal())
			return dlg.medicines;
		else return null;
	}


	/** {@inheritDoc}
	 */
	@Override
	public boolean showModal() {
		mountMedicineList();
		return super.showModal();
	}

	
	
	/**
	 * Create the list of medicines in the grid
	 */
	private void mountMedicineList() {
		MedicineServices srv = App.getComponent(MedicineServices.class);
		List<Medicine> lst = srv.getMedicines();
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		for (Medicine med: lst) {
			Object[] row = {new ItemSelect(med)};
			model.addRow(row);
		}
	}


	/** {@inheritDoc}
	 */
	@Override
	public boolean save() {
		medicines = new ArrayList<Medicine>();
		for (int i = 0; i < table.getRowCount(); i++) {
			ItemSelect<Medicine> item = (ItemSelect)table.getValueAt(i, 0);
			if (item.isSelected())
				medicines.add(item.getItem());
		}
		if (medicines.size() == 0) {
			JOptionPane.showMessageDialog(this, App.getMessage("edtrec.nomedicine"));
			return false;
		}

		return true;
	}

	/**
	 * @return the medicines
	 */
	public List<Medicine> getMedicines() {
		return medicines;
	}

	/**
	 * @param medicines the medicines to set
	 */
	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}

}
