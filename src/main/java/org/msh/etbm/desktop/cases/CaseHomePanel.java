package org.msh.etbm.desktop.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.AppTableCellRenderer;
import org.msh.etbm.desktop.common.ClientPanel;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.PatientNameCellRenderer;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SyncCellRenderer;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.SynchronizationData;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.adminunit.AdminUnitsUtils;
import org.msh.etbm.services.cases.CaseFilters;
import org.msh.etbm.services.cases.CaseResultItem;
import org.msh.etbm.services.cases.CaseStateReport;
import org.msh.etbm.services.cases.CaseStateReport.CaseStateItem;
import org.msh.etbm.services.cases.CaseStateReport.Item;
import org.msh.etbm.services.cases.CaseStateReport.TagItem;
import org.msh.etbm.services.cases.CaseStateReport.ValidationItem;
import org.msh.etbm.services.cases.CasesQuery;
import org.msh.etbm.services.login.UserSession;
import org.msh.etbm.services.misc.CaseDefinitionFieldIntelligence;
import org.msh.etbm.services.misc.SecDrugsReceivedFieldIntelligence;
import org.msh.eventbus.EventBusService;


/**
 * This is the main panel of the case management module, which displays a summary of all cases
 * and a table with the cases registered, with possibility to notify a new case or suspect and
 * filter cases by name
 * 
 * @author Ricardo Memoria
 *
 */
public class CaseHomePanel extends ClientPanel implements Refreshable {
	private static final long serialVersionUID = 1893046728022395473L;
	
	private static final int MAX_TABLE_ROWS = 10000;
	private static final int TYPESEARCH_DELAY = 500;
	private static final float LEFTPANEL_PERC_WIDTH = 0.2F;
	
	private JTextField edtPatient;
	private JTree treeCases;
	private JTable table;
	private ImageIcon imgRight = new AwesomeIcon(AwesomeIcon.ICON_CHEVRON_RIGHT, Color.BLACK, 14); // ImageIcon(CaseHomePanel.class.getResource("/resources/images/right.png"));
	private ImageIcon imgTagManual = new AwesomeIcon(AwesomeIcon.ICON_TAG, new Color(26, 53, 167), 18);
	private ImageIcon imgTagAutogen = new AwesomeIcon(AwesomeIcon.ICON_TAG, new Color(8, 153, 73), 18);
	private ImageIcon imgTagConsist = new AwesomeIcon(AwesomeIcon.ICON_TAG, new Color(239, 94, 94), 18);
	private JLabel txtFilter;
	private JLabel txtCount;
	private Timer timerTyping;
	private boolean refreshOnActive = false;

	/**
	 * Update information in the screen
	 */
	public void refresh() {
		updateTree();
		updateCaseTable();
	}


	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if ((event == AppEvent.CASE_DELETED) || (event == AppEvent.CASE_MODIFIED) || (event == AppEvent.NEW_CASE) || 
			(event == AppEvent.CASES_REFRESH) || (event == AppEvent.SYNC_EXECUTED)) {
			refreshOnActive = true;
			if (isVisible()) {
				activate();
			}
		}
		else super.handleEvent(event, data);
	}
	

	/** {@inheritDoc}
	 */
	@Override
	public void activate() {
		super.activate();
		if (refreshOnActive) {
			updateTree();
			updateCaseTable();
			refreshOnActive = false;
		}
	}

	/**
	 * Update the table displaying the list of cases from a search result. If there is no search result available, all cases are displayed 
	 */
	public void updateCaseTable() {
		CasesQuery cases = CasesQuery.instance();
		// max number of records
		cases.refresh();
		cases.setEjbql(null);
		cases.setMaxResults(MAX_TABLE_ROWS);
		
		int count = cases.getResultList().size();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		
		if (count > MAX_TABLE_ROWS)
			count = MAX_TABLE_ROWS;
		
		if (count == 0) {
			 table.setVisible(false);
			 model.setRowCount(0);
			 txtCount.setText(Messages.getString("form.norecordfound"));
			 return;
		}

		model.setRowCount(0);

		// populate the table with cases
		for (CaseResultItem item: cases.getResultList()) {
			String classif = item.getTbcase().isSuspect()? item.getTbcase().getClassification().getKeySuspect(): item.getTbcase().getClassification().getKey();

			Object[] vals = {
					item.getTbcase().getSyncData(),
					item.getPatient(), 
					item.getTbcase(), 
					Messages.getString( classif ),
					Integer.toString( item.getTbcase().getAge() ),
					item.getTbcase().getState(),
					item.getTbcase().getOwnerUnit().toString(), 
					item.getAdminUnitDisplay()};
			model.addRow(vals);
		}
	
		PatientNameCellRenderer renderer = new PatientNameCellRenderer();
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new SyncCellRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new TbCaseCellRenderer());
		GuiUtils.setBackground(table, Color.WHITE);
		
		model.fireTableDataChanged();
		table.setVisible(true);

		DecimalFormat format = new DecimalFormat("#,###,##0");
		txtCount.setText( "<html><b>" + Messages.getString("form.result") + ": </b>" +  format.format(count) + " " + Messages.getString("form.resultlist") +"</html>");

		TableRowSorter<TableModel> sorter = (TableRowSorter)table.getRowSorter();
		// sort by sync data
		sorter.setComparator(0, new Comparator<SynchronizationData>() {
	    	@Override
	    	public int compare(SynchronizationData p1, SynchronizationData p2) {
	    		if (p1.isChanged() == p2.isChanged()) {
	    			return 0;
	    		}
	    		if (p1.isChanged()) {
	    			return -1;
	    		}
	    		return 1;
	    	}
		});
		// sort by patient name
		sorter.setComparator(1, new Comparator<Patient>() {
		    	@Override
			public int compare(Patient p1, Patient p2) {
				return p1.toString().compareTo(p2.toString());
			}
		});
		// sort by case
		sorter.setComparator(2, new Comparator<TbCase>() {
	    	@Override
	    	public int compare(TbCase p1, TbCase p2) {
	    		return p1.toString().compareTo(p2.toString());
	    	}
		});
	}


	/**
	 * Update the content of the left tree 
	 */
	protected void updateTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		treeCases.putClientProperty("JTree.lineStyle", "None");
		
		CaseStateReport rep = CaseStateReport.instance();
		rep.refresh();
		
		// add case status report
		List<CaseStateItem> items = rep.getItems();
		DefaultMutableTreeNode nodeCases = null;
		if (items.size() > 0) {
			nodeCases = new DefaultMutableTreeNode(UserSession.getUserWorkspace().getTbunit().getName().toString());
			for (Item item: items) {
				nodeCases.add( nodeDisplay(item));
			}
			root.add(nodeCases);
		}
		
		// add validation status report
/*		List<ValidationItem> vals = rep.getValidationItems();
		DefaultMutableTreeNode nodeVal = null;
		if (vals.size() > 0) {
			nodeVal = new DefaultMutableTreeNode(Messages.getString("ValidationState"));
			for (ValidationItem item: vals) {
				nodeVal.add( nodeDisplay(item));
			}
			root.add(nodeVal);
		}
*/
		// add tags report
		List<TagItem> tags = rep.getTags();
		DefaultMutableTreeNode nodeTags = null;
		if (tags.size() > 0) {
			nodeTags = new DefaultMutableTreeNode(Messages.getString("admin.tags"));
			for (TagItem item: tags) {
				nodeTags.add( nodeDisplay(item));
			}
			root.add(nodeTags);
		}
		
		treeCases.setModel(new DefaultTreeModel(root));
		if (nodeCases != null)
			treeCases.expandPath(new TreePath(nodeCases.getPath()));
//		if (nodeVal != null)
//			treeCases.expandPath(new TreePath(nodeVal.getPath()));
		if (nodeTags != null)
			treeCases.expandPath(new TreePath(nodeTags.getPath()));

//		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		MyTreeCellRenderer renderer = new MyTreeCellRenderer();
		GuiUtils.setBackground(renderer, Color.WHITE);
		renderer.setBackgroundNonSelectionColor(Color.WHITE);
		renderer.setLeafIcon(imgRight);
		treeCases.setCellRenderer(renderer);
	}

	
	/**
	 * Start the new notification of a new suspect or case 
	 * @param diagnosisType is the type to be notified - suspect or case
	 */
/*	protected void newNotificationClick(DiagnosisType diagnosisType) {
		NewNotificationFlow.instance().start(diagnosisType);
	}
*/

	/**
	 * Create a new node to be included in the tree using a {@link Item} from the {@link CaseStateReport} 
	 * as a reference
	 * @param item is the instance of the {@link Item} that will be linked to the tree node
	 * @return instance of the {@link DefaultMutableTreeNode} to be included in the tree
	 */
	protected DefaultMutableTreeNode nodeDisplay(Item item) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(item.toString());
		node.setUserObject(new NodeWrapper(item));
		return node;
	}


	/**
	 * Called when the tree is clicked
	 * @param e instance of the {@link TreeSelectionEvent}
	 */
	protected void treeNodeChanged(TreeSelectionEvent e) {
		updateCasesFilters();
		updateCaseTable();
	}

	
	/**
	 * Update the filters of the cases table
	 */
	protected void updateCasesFilters() {
		CaseFilters filters = CaseFilters.instance();
		filters.clearFilters();

		// get filter by patient name
		String pac = edtPatient.getText().isEmpty() ? null: edtPatient.getText();
		filters.setPatient(pac);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeCases.getLastSelectedPathComponent();

		// get filter by selected node in tree
		if ((node != null) && (node.getUserObject() instanceof NodeWrapper)) {
			NodeWrapper data = (NodeWrapper)node.getUserObject();
			Item item = data.getItem();
			
			if (item instanceof CaseStateItem) {
				CaseStateItem ci = (CaseStateItem)item;
				filters.setStateIndex( ci.getStateIndex() );
			}
			else
			if (item instanceof ValidationItem) {
				ValidationItem vi = (ValidationItem)item;
				filters.setValidationState( vi.getValidationState() );
			}
			else
			if (item instanceof TagItem) {
				TagItem ti = (TagItem)item;
				filters.setTagid(ti.getTagId());
			}
		}
	}
	
	/**
	 * Filter the cases by the patient name or number
	 */
	protected void filterCasesByPatient() {
		updateCasesFilters();
		updateCaseTable();
//		txtFilter.setText("<html><b>" + Messages.getString("global.searchkey") + ": </b>" + edtPatient.getText() + "</html>");
	}

	
	/**
	 * Called when user type the patient name
	 */
	protected void edtPatientClick() {
		if (timerTyping == null)
			timerTyping = new Timer(TYPESEARCH_DELAY, new ActionListener() {
			    	@Override
				public void actionPerformed(ActionEvent e) {
					timerTyping.stop();
					filterCasesByPatient();
				}
			});
		
		timerTyping.restart();
	}


	/**
	 * Create the panel and instantiate all the components of the user interface
	 */
	@SuppressWarnings("serial")
	public CaseHomePanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(LEFTPANEL_PERC_WIDTH);
//		GuiUtils.setBackground(splitPane, Color.WHITE);
		add(splitPane);
		
		JPanel panel = new JPanel();
		GuiUtils.setBackground(panel, Color.WHITE);
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
/*		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(5, 2, 5, 2));
		GuiUtils.setBackground(panel_1, Color.WHITE);
		panel.add(panel_1, BorderLayout.NORTH);
		
		JButton btnNewNotification = new JButton(Messages.getString("cases.newnotif"));
		btnNewNotification.setIcon(new AwesomeIcon(AwesomeIcon.ICON_FILE_TEXT_ALT, btnNewNotification));
		btnNewNotification.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				newNotificationClick(DiagnosisType.CONFIRMED);
			}
		});
		
		JButton btnNewButton = new JButton(Messages.getString("cases.newsusp"));
		btnNewButton.setIcon(new AwesomeIcon(AwesomeIcon.ICON_FILE_TEXT_ALT, btnNewButton));
		btnNewButton.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				newNotificationClick(DiagnosisType.SUSPECT);
			}
		});
		panel_1.setLayout(new GridLayout(0, 1, 4, 4));
		panel_1.add(btnNewNotification);
		panel_1.add(btnNewButton);
*/		
		treeCases = new JTree();
		treeCases.addTreeSelectionListener(new TreeSelectionListener() {
		    	@Override
			public void valueChanged(TreeSelectionEvent e) {
				treeNodeChanged(e);
			}
		});
		treeCases.setVisibleRowCount(0);
		treeCases.setRootVisible(false);
		GuiUtils.setBackground(treeCases, Color.WHITE);
		panel.add(treeCases, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		GuiUtils.setBackground(panel_2, Color.WHITE);
		
		table = new JTable();
		table.setBackground(Color.WHITE);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					tableDoubleClick();
			}
		});
		
		
		String[] names = AdminUnitsUtils.getCountryStructureLabels();
		String aulabel = "AdministrativeUnit";
		if (names.length > 0) {
			aulabel = names[0];
		}
		if (names.length > 2) {
			aulabel += ", " + names[1];
		}
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"", "@Patient.name", "@Patient.caseNumber", "@CaseClassification", "@TbCase.age", "@CaseState", "@Tbunit", aulabel
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, String.class, String.class, String.class, String.class, Object.class, Object.class, Object.class
			};
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(24);
		table.getColumnModel().getColumn(1).setPreferredWidth(187);
		table.getColumnModel().getColumn(2).setPreferredWidth(103);
		table.getColumnModel().getColumn(3).setPreferredWidth(114);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		table.getColumnModel().getColumn(5).setPreferredWidth(215);
		table.getColumnModel().getColumn(6).setPreferredWidth(203);
		table.getColumnModel().getColumn(7).setPreferredWidth(211);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GuiUtils.setBackground(scrollPane, Color.WHITE);
		GuiUtils.setBackground(table, Color.WHITE);
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(table);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new GridLayout(2, 1, 0, 0));
		GuiUtils.setBackground(panel_4, Color.WHITE);
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3);
		panel_3.setBorder(new EmptyBorder(4, 4, 4, 4));
		GuiUtils.setBackground(panel_3, Color.WHITE);
		
		JLabel lblNewLabel = new JLabel(Messages.getString("Patient"));
		
		edtPatient = new JTextField();
		edtPatient.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				edtPatientClick();
			}
		});
		edtPatient.setColumns(10);
		
		JButton btnAdvancedSearch = new JButton(Messages.getString("cases.advancedsearch"));
		btnAdvancedSearch.setVisible(false);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(10)
					.addComponent(lblNewLabel)
					.addGap(10)
					.addComponent(edtPatient, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
					.addComponent(btnAdvancedSearch))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(4)
					.addComponent(lblNewLabel))
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(1)
					.addComponent(edtPatient, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(btnAdvancedSearch)
		);
		panel_3.setLayout(gl_panel_3);
		
		JPanel pnlResult = new JPanel();
		pnlResult.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		panel_4.add(pnlResult);
		pnlResult.setLayout(new GridLayout(0, 2, 0, 0));
		GuiUtils.setBackground(pnlResult, Color.WHITE);
		
		txtFilter = new JLabel(); //$NON-NLS-1$
		pnlResult.add(txtFilter);
		
		txtCount = new JLabel(); //$NON-NLS-1$
		txtCount.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlResult.add(txtCount);

		// custom code
		GuiUtils.prepareTable(table);
		
		EventBusService.observeEvent(AppEvent.NEW_CASE, this);
		EventBusService.observeEvent(AppEvent.CASE_DELETED, this);
		EventBusService.observeEvent(AppEvent.CASE_MODIFIED, this);
		EventBusService.observeEvent(AppEvent.CASES_REFRESH, this);

		CaseDefinitionFieldIntelligence cdIntel = new CaseDefinitionFieldIntelligence();
		EventBusService.observeEvent(AppEvent.NEW_CASE_SAVED, cdIntel);
		EventBusService.observeEvent(AppEvent.CASE_MODIFIED, cdIntel);
		EventBusService.observeEvent(AppEvent.EXAMS_MODIFIED, cdIntel);

		SecDrugsReceivedFieldIntelligence secDrugsIntell = new SecDrugsReceivedFieldIntelligence();
		EventBusService.observeEvent(AppEvent.NEW_CASE_SAVED, secDrugsIntell);
		EventBusService.observeEvent(AppEvent.CASE_MODIFIED, secDrugsIntell);
	}
	

	/**
	 * Handle table double click on a row
	 */
	protected void tableDoubleClick() {
		TbCase tbcase = (TbCase)table.getValueAt(table.getSelectedRow(), 2);

		GuiUtils.openCaseDetailPage(tbcase.getId());
	}



	/**
	 * Local class to wrap the content of an {@link Item} instance inside a node of the tree.
	 * It basically override the <code>ToString()</code> to display the message that will be
	 * displayed in the node of the tree
	 *  
	 * @author Ricardo Memoria
	 *
	 */
	public class NodeWrapper {
		private Item item;

		/**
		 * Default constructor
		 * @param item instance of {@link Item} from {@link CaseStateReport}
		 */
		public NodeWrapper(Item item) {
			super();
			this.item = item;
		}

		/**
		 * Return the instance of the {@link Item} from {@link CaseStateReport} being wrapped
		 * @return instance of {@link Item}
		 */
		public Item getItem() {
			return item;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = item.getDescription();
			if (item.getTotal() > 0) {
				DecimalFormat format = new DecimalFormat("#,###,##0");
				s += "  (" + format.format(item.getTotal()) + ")";
			}
			
			return s;
		}
	}
	
	
	/**
	 * Custom render of the table to handle cells that contains an instance of {@link TbCase} entity
	 * @author Ricardo Memoria
	 *
	 */
	public class TbCaseCellRenderer extends AppTableCellRenderer {
		private static final long serialVersionUID = -9176503753798096836L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof TbCase)
				value = ((TbCase)value).getDisplayCaseNumber();

			Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			return renderer;
		}
	}
	
	
	public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 503783278674875564L;

		/** {@inheritDoc}
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
					row, hasFocus);

			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				if (node.getUserObject() instanceof NodeWrapper) {
					NodeWrapper data = (NodeWrapper)node.getUserObject();
					Item item = data.getItem();
					if (item instanceof TagItem) {
						TagItem ti = (TagItem)item;
						switch (ti.getType()) {
						case AUTOGEN:
							setIcon(imgTagAutogen);
							break;
						case AUTOGEN_CONSISTENCY:
							setIcon(imgTagConsist);
							break;
						default:
							setIcon(imgTagManual);
						}
					}
					else {
						setIcon(imgRight);
					}
				}
			}
			
			return comp;
		}
		
	}
}
