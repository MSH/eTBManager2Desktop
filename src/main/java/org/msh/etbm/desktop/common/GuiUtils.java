package org.msh.etbm.desktop.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.cases.CaseDetailPanel;
import org.msh.etbm.desktop.xview.CustomFormDataModel;
import org.msh.etbm.entities.TbCase;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.FormManager;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.XViewUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.toedter.calendar.JDateChooser;

/**
 * Set of utility functions commonly used in the user interface
 * 
 * @author Ricardo Memoria
 *
 */
public class GuiUtils {

	/**
	 * Prepare table to be displayed using a standard view of the system
	 * @param table
	 */
	public static void prepareTable(JTable table) {
		table.setShowHorizontalLines(false);
		table.setBackground(new Color(255, 255, 255));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(21);
		table.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1));
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		AppTableCellRenderer renderer = new AppTableCellRenderer();
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		
		// translate headers
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			String s = col.getHeaderValue().toString();
			if (!s.isEmpty())
				col.setHeaderValue( XViewUtils.translateMessage( col.getHeaderValue().toString() ));
		}
		setBackground(table, Color.WHITE);
	}
	
	
	/**
	 * Create a new instance of {@link JDateChooser} using the workspace locale
	 * @return
	 */
	public static JDateChooser createDateChooser() {
		String datePattern = App.getMessage("locale.datePattern");
		String dateMask = App.getMessage("locale.dateMask");
		
		JDateChooser dc = new JDateChooser(datePattern, dateMask, '_');
		
		dc.setSize(150, dc.getPreferredSize().height);
		return dc;
	}

	/**
	 * Return the TB case key used in panels to avoid more than one panel with the same case to open
	 * @param tbcase
	 * @return
	 */
	public static Object getTbCasePanelKey(TbCase tbcase) {
		return "TBCASE_" + tbcase.getId();
	}
	
	
	/**
	 * Return the singleton instance of the {@link FormManager} interface
	 * @return
	 */
	public static FormManager getFormManager() {
		return (FormManager)App.getComponent("formManager");
	}
	
	
	/**
	 * Create a new instance of a {@link SwingFormContext} from the form manager 
	 * @param formName
	 * @return
	 */
	public static SwingFormContext createForm(String formName) {
		SwingFormContext form = (SwingFormContext)getFormManager().createFormAdapter(formName);
		form.setDataModel(new CustomFormDataModel());
		return form;
	}
	
	
	/**
	 * Initialize a combo box with a list of objects
	 * @param cb
	 * @param lst
	 */
	public static void initComboBox(JComboBox cb, List lst) {
		cb.removeAllItems();
		cb.addItem("-");
		for (Object obj: lst)
			cb.addItem(obj);
		cb.setSelectedIndex(0);

//		if (cb.getItemCount() == 1) {
			Dimension d = cb.getPreferredSize();
			cb.setSize(d);
//		}
	}
	
	
	/**
	 * Create a new form context from the form manager in use
	 * @param formName
	 * @return
	 */
	public static SwingFormContext createNewForm(String formName) {
		FormManager frmMan = (FormManager)App.getComponent("formManager");
		return (SwingFormContext)frmMan.createFormAdapter(formName);
	}
	
	
	/**
	 * Open the case detail page in the main window
	 * @param caseid
	 */
	public static void openCaseDetailPage(Integer caseid) {
		EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(caseid) {
			@Override
			public void execute(Integer id) {
				TbCase tbcase = App.getEntityManager().find(TbCase.class, id);
				
				if (MainWindow.instance().activePanelByKey( GuiUtils.getTbCasePanelKey( tbcase )))
					return;

				CaseDetailPanel pnl = new CaseDetailPanel();
				pnl.setTbcaseId(tbcase.getId());
				MainWindow.instance().addPanel(pnl, Messages.getString(tbcase.getClassification().getKey()) + " - " + tbcase.getDisplayCaseNumber(), true);
			}
		});
	}


	/**
	 * Update label that display the number of records
	 * @param label the instance of {@link JLabel} component that will display the record counting result
	 * @param count the number of records found
	 */
	public static void updateRecordCountLabel(JLabel labelCount, int count) {
		if (count == 0) {
			labelCount.setText(Messages.getString("form.norecordfound"));
			labelCount.setForeground(new Color(255, 140, 0));
			labelCount.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		else {
			DecimalFormat format = new DecimalFormat("#,###,##0");
			labelCount.setText( "<html><b>" + Messages.getString("form.result") + ": </b>" +  format.format(count) + " " + Messages.getString("form.resultlist") +"</html>");
			labelCount.setForeground(new Color(0, 0, 0));
			labelCount.setFont(labelCount.getParent().getFont());
		}
		
	}
	
	
	/**
	 * Set the background color of the component, adjusting the colorization factor
	 * of the substance component
	 * @param comp instance of {@link JComponent} to have its background color changed
	 * @param color the background color
	 */
	public static void setBackground(JComponent comp, Color color) {
		comp.setBackground(color);
		comp.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1));
	}
}
