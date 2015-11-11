/**
 * 
 */
package org.msh.etbm.desktop.startup;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.MigLayoutPanel;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SystemLogo;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.sync.ServerServices;
import org.msh.etbm.sync.WorkspaceInfo;
import org.msh.eventbus.EventBusService;

/**
 * @author Ricardo Memoria
 *
 */
public class StartupWizard extends JPanel implements Refreshable  {
	private static final long serialVersionUID = -347112452398804029L;
	private static final int PADDING=20;
	
	private static final String PAGE_INI = "ini";
	private static final String PAGE_FILE = "file";
	private static final String PAGE_SERVER = "server";
	private static final String PAGE_WORKSPACES = "workspaces";

	private JPanel pnlContent;
	private String selectedPage = PAGE_INI;
	private JPanel pnlFirst;
	private JPanel pnlFileSel;
	private JPanel pnlServer;
	private JPanel pnlSelWorkspace;
	private CardLayout layout;

	private JXLabel txtMessage;
	private JRadioButton btnIniFile;
	private JRadioButton btnInternet;
	private JButton btnPrevious;
	private JButton btnNext;
	private JButton btnCancel;
	private JTextField edtFile;
	private JTextField edtServer;
	private JTextField edtUser;
	private JTextField edtPassword;
	private JList lbWorkspaces;
	private List<WorkspaceInfo> workspaces;
	private Dimension contentSize = new Dimension(480, 270);
	private WorkspaceInfo selectedWorkspace;
	
	/**
	 * Default constructor
	 */
	public StartupWizard() {
		super();
		
		setSize(580, 480);
		setBorder(new EmptyBorder(PADDING,PADDING,PADDING,PADDING));
		
		MigLayoutPanel pnl = new MigLayoutPanel("wrap 3", "", "[]20[]15[]");
		pnl.add(new SystemLogo(), "span 3");
		
		pnlContent = new JPanel();
		
		layout = new CardLayout();
		pnlContent.setLayout(layout);
		pnlContent.add(getFirstPanel(), PAGE_INI);
		pnlContent.add(getFileSelPanel(), PAGE_FILE);
		pnlContent.add(getServerPanel(), PAGE_SERVER);
		pnlContent.add(getSelWorkspacePanel(), PAGE_WORKSPACES);
		layout.show(pnlContent, selectedPage);
		
		pnlContent.setPreferredSize(contentSize);
		pnlContent.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		pnl.add(pnlContent, "span 3");

		btnPrevious = new JButton(Messages.getString("form.navprevious"));
		btnPrevious.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CHEVRON_LEFT, btnPrevious));
		btnPrevious.setEnabled(false);
		btnPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handlePreviousClick();
			}
		});
		pnl.add(btnPrevious);

		btnNext = new JButton(Messages.getString("form.navnext"));
		btnNext.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CHEVRON_RIGHT, btnNext));
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleNextClick();
			}
		});
		pnl.add(btnNext);

		btnCancel = new JButton(Messages.getString("form.exit"));
		btnCancel.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE, btnCancel));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleCancelClick();
			}
		});
		pnl.add(btnCancel, "align right");
		
		add(pnl);
	}
	
	/**
	 * Called when the user clicks on the next button
	 */
	protected void handleNextClick() {
		String page = null;

		if (PAGE_INI.equals(selectedPage)) {
			if ((!btnIniFile.isSelected()) && (!btnInternet.isSelected())) {
				JOptionPane.showMessageDialog(this, Messages.getString("desktop.startup.firsttime.error1"));
				return;
			}
			
			if (btnIniFile.isSelected())
				 page = PAGE_FILE;
			else page = PAGE_SERVER;
		}
		
		if (PAGE_FILE.equals(selectedPage)) {
			File file = new File(edtFile.getText());
			if ((!file.exists()) || (file.isDirectory())) {
				JOptionPane.showMessageDialog(this, Messages.getString("desktop.invalidfile"));
				return;
			}
			//TODO:Set selectedWorkspace in LoadPackagePanel, the way below is not working
			EventBusService.raiseEvent(StartupEvent.EXECUTE_INIFILE, file, selectedWorkspace);
		}
		
		// user entered login and password, so check the workspaces available
		if (PAGE_SERVER.equals(selectedPage)) {
			if (isEmpty(edtServer.getText()) || (isEmpty(edtUser.getText())) || (isEmpty(edtPassword.getText()))) {
				JOptionPane.showMessageDialog(this, Messages.getString("desktop.startup.informallflds"));
				return;
			}
			loadWorkspaces();
			
			if (selectedWorkspace != null) {
				requestFileDownload();
				return;
			}

			page = PAGE_WORKSPACES;
		}
		
		if (PAGE_WORKSPACES.equals(selectedPage)) {
			if (lbWorkspaces.getSelectedIndex() < 0) {
				JOptionPane.showMessageDialog(this, Messages.getString("desktop.startup.selectws"));
			}
			else {
				selectedWorkspace = workspaces.get(lbWorkspaces.getSelectedIndex());
				requestFileDownload();
				return;
			}
		}
		
		if (page != null) {
			layout.show(pnlContent, page);
			btnPrevious.setEnabled(!PAGE_INI.equals(page));
			selectedPage = page;
		}
	}

	
	/**
	 * Download the file
	 */
	private void requestFileDownload() {
//		EventBusService.raiseEvent(StartupEvent.OPEN_LOGIN);
		ServerServices conn = App.getComponent(ServerServices.class);
		String token = conn.login(edtServer.getText(), selectedWorkspace.getId(), edtUser.getText(), edtPassword.getText());
		System.out.println(token);
		
		EventBusService.raiseEvent(StartupEvent.DOWNLOAD_INIFILE,
				new ServerCredentials(edtServer.getText(), 
						selectedWorkspace.getId(), 
						edtUser.getText(), 
						edtPassword.getText(),
						token)); 
	}

	/**
	 * Load the list of workspaces to be selected by the system
	 */
	private void loadWorkspaces() {
		ServerServices conn = App.getComponent(ServerServices.class);
		List<WorkspaceInfo> lst = conn.getWorkspaces(edtServer.getText(), edtUser.getText(), edtPassword.getText());
		if (lst.size() == 1) {
			selectedWorkspace = lst.get(0);
			return;
		}
		
		if (lst.size() == 0)
			throw new RuntimeException("No workspace returned");
		
		workspaces = lst;
		selectedWorkspace = null;
		DefaultListModel model = (DefaultListModel)lbWorkspaces.getModel();
		model.clear();
		for (WorkspaceInfo ws: lst) {
			model.addElement(ws.getName1() + " - " + ws.getHealthUnitName());
		}
	}

	/**
	 * Called when the user clicks on the previous button
	 */
	protected void handlePreviousClick() {
		if ((PAGE_FILE.equals(selectedPage)) || (PAGE_SERVER.equals(selectedPage))) {
			selectedPage = PAGE_INI;
		}

		if (PAGE_WORKSPACES.equals(selectedPage))
			selectedPage = PAGE_SERVER;

		layout.show(pnlContent, selectedPage);
	}


	/** {@inheritDoc}
	 */
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Check if string is empty
	 * @param s string to test
	 * @return true if string is empty
	 */
	private boolean isEmpty(String s) {
		return (s == null) || (s.trim().isEmpty());
	}


	/**
	 * Called when the user clicks on the cancel button
	 */
	protected void handleCancelClick() {
		if (JOptionPane.showConfirmDialog(this, Messages.getString("desktop.startup.quit"), 
				Messages.getString("form.exit"), 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			App.exit();
	}
	
	/**
	 * Return the first panel of the wizard
	 * @return instance of {@link JPanel}
	 */
	protected JPanel getFirstPanel() {
		if (pnlFirst != null)
			return pnlFirst;

		pnlFirst = createDefaultPanel("desktop.startup.firsttime", AwesomeIcon.ICON_DESKTOP, 1, "", "[][]10[][]");
		
		txtMessage = new JXLabel(Messages.getString("desktop.startup.msg1") + ":");
		txtMessage.setLineWrap(true);
		txtMessage.setPreferredSize(new Dimension(500, 50));
		txtMessage.setMaximumSize(new Dimension(getWidth() - (PADDING*2), getHeight()));
		pnlFirst.add(txtMessage);

		ButtonGroup grp = new ButtonGroup();
		btnIniFile = new JRadioButton(Messages.getString("desktop.startup.inifile"));
		grp.add(btnIniFile);
		btnInternet = new JRadioButton(Messages.getString("desktop.startup.internetconn"));
		grp.add(btnInternet);

		pnlFirst.add(btnIniFile);
		pnlFirst.add(btnInternet);
		pnlFirst.setMaximumSize(contentSize);
		
		return pnlFirst;
	}

	
	/**
	 * Return the file selection panel
	 * @return
	 */
	protected JPanel getFileSelPanel() {
		if (pnlFileSel != null)
			return pnlFileSel;

		pnlFileSel = createDefaultPanel("desktop.startup.inifile", AwesomeIcon.ICON_FILE, 2, "", "[]10[][]");

		pnlFileSel.add(new JLabel(Messages.getString("desktop.startup.inifile.enter") + ":"), "span 2");
		
		edtFile = new JTextField(50);
		pnlFileSel.add(edtFile);
		JButton btnSearch = new JButton(Messages.getString("form.search"));
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchFile();
			}
		});
		pnlFileSel.add(btnSearch);
		
		return pnlFileSel;
	}

	
	/**
	 * Open dialog to search for the initialization file
	 */
	protected void searchFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return Messages.getString("desktop.startup.inifile");
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				int i = f.getName().lastIndexOf('.');
				if (i > 0) {
					return f.getName().substring(i + 1).equalsIgnoreCase("pkg");
				}
				return false;
			}
		});
		int ret = fc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			edtFile.setText( fc.getSelectedFile().toString() );
		}
	}

	/**
	 * Return the panel that will receive information about the server
	 * @return instance of {@link JPanel} class
	 */
	private JPanel getServerPanel() {
		if (pnlServer != null)
			return pnlServer;

		pnlServer = createDefaultPanel("desktop.startup.internetconn", AwesomeIcon.ICON_CLOUD_DOWNLOAD, 2, "", "[]10[]8[]8[]");

		JLabel lbl = new JLabel(Messages.getString("SystemConfig.systemURL") + ":");
		pnlServer.add(lbl);
		edtServer = new JTextField(30);
		pnlServer.add(edtServer);

		lbl = new JLabel(Messages.getString("login.user_login") + ":");
		pnlServer.add(lbl);
		edtUser = new JTextField(15);
		pnlServer.add(edtUser);
		
		lbl = new JLabel(Messages.getString("User.password") + ":");
		pnlServer.add(lbl);
		edtPassword = new JPasswordField(15);
		pnlServer.add(edtPassword);
		
		return pnlServer;
	}

	
	/**
	 * Create panel to select the workspace
	 * @return instance of {@link JPanel}
	 */
	private JPanel getSelWorkspacePanel() {
		if (pnlSelWorkspace != null)
			return pnlSelWorkspace;

		pnlSelWorkspace = createDefaultPanel("admin.workspaces", AwesomeIcon.ICON_GLOBE, 1, "", "[]10[][]");
		
		pnlSelWorkspace.add(new JLabel(Messages.getString("desktop.startup.selectws")));
		
		lbWorkspaces = new JList();
		lbWorkspaces.setModel(new DefaultListModel());
		pnlSelWorkspace.add(new JScrollPane(lbWorkspaces));
		
		return pnlSelWorkspace;
	}
	
	/**
	 * Create a standard panel to be used in the wizard
	 * @param title is the title to be displayed
	 * @param awesomeIcon is the ID of a {@link AwesomeIcon}
	 * @param layoutConstraints layout constraints of the {@link MigLayout}
	 * @param columnConstraints column constraints of the {@link MigLayout}
	 * @param rowConstraints row constraints of the {@link MigLayout}
	 * @return instance of the {@link MigLayoutPanel}
	 */
	protected MigLayoutPanel createDefaultPanel(String title, int awesomeIcon, 
			int columns, String columnConstraints, String rowConstraints) {
		MigLayoutPanel pnl = new MigLayoutPanel("wrap " + Integer.toString(columns), columnConstraints, rowConstraints);
		pnl.setBorder(new EmptyBorder(PADDING - 10,PADDING,PADDING - 10,PADDING));
		
		JLabel txtTitle = new JXLabel(Messages.getString(title));
		txtTitle.setFont(UiConstants.h1Font);
		txtTitle.setIcon(new AwesomeIcon(awesomeIcon, Color.LIGHT_GRAY, 48));
		pnl.add(txtTitle, "span " + Integer.toString(columns));
		
		return pnl;
	}
}
