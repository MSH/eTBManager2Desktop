package org.msh.etbm.desktop.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import org.msh.etbm.desktop.cases.CaseHomePanel;
import org.msh.etbm.desktop.cases.NewNotificationFlow;
import org.msh.etbm.desktop.common.ClientPanel;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.PanelKey;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.startup.LoginPanel;
import org.msh.etbm.desktop.startup.StartupPanel;
import org.msh.etbm.desktop.sync.SynchronizeDialog;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.services.login.UserSession;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.LocaleDateConverter;
import org.msh.utils.date.Period;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 * The main window of the application. This is also the entry point of the application.
 * When it's opened, the login panel (class {@link LoginPanel}) is instantiated and 
 * displayed in the client area of the panel. If user is successfully authenticated,
 * the login panel is closed and the cases home panel (class {@link CaseHomePanel}) 
 * is displayed. 
 * 
 * @author Ricardo Memoria
 *
 */
public class MainWindow implements EventBusListener {

    private JFrame frame;
    private static MainWindow mainWindowInstance;

	private JPanel pnlStatus;
	private JLabel txtUser;
	private JLabel txtLastSync;
	private JLabel txtWorkspace;
	private JTabbedPane tabbedPane;
	private StartupPanel startupPanel;
	private JPanel currentView;
	private JToolBar toolbar;


    /**
     * Entry point of the application. This is where the application
     * starts execution.
     * @param args
     */
    public static void main( String[] args )
    {
    	// the locale is manually set because in some countries it's not properly done
    	String s = System.getProperty("user.language");
    	if (s != null) {
    		String locs[] = s.split("_");
    		if (locs.length > 1)
    			 Locale.setDefault(new Locale(locs[0], locs[1]));
    		else Locale.setDefault(new Locale(locs[0]));
    	}

    	// handle exceptions occurred inside the system
    	EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    	queue.push(new EventQueueProxy());
    	
    	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			App.initialize();
			mainWindowInstance = new MainWindow();
			MainWindow.instance().open();
		}
	});
    }


    /**
     * Public method to add a new panel to the tabbed pane of the main window.
     * This method is called to include a new panel in the main window. The
     * <code>panel</code> argument is the panel to be displayed, and if this
     * panel implements the interface {@link Refreshable}, the system will
     * call the method <code>refresh()</code> after it's included in the 
     * main window tab.
     *  
     * @param panel is the instance of the panel to be displayed
     * @param title is the title displayed in the tab where the panel is
     * @param closable If true, displays a close link to close the panel
     */
    public void addPanel(JPanel panel, String title, boolean closable) {
    	if (closable)
    		panel.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);

    	tabbedPane.addTab("<html><body marginwidth='3' marginheight='3'>" + title + "</body></html>", null, panel, null);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		
		if (panel instanceof Refreshable)
			((Refreshable)panel).refresh();
    }


    /**
     * Search for an opened panel in the list of tabs by its key. 
     * Once the panel is found, it is displayed and becomes the active panel 
     * in the tabbed panel component of the main window. In order to expose a key,
     * the panel must implement the interface {@link PanelKey}, which contains
     * a method that exposes the key of the panel. The key can be used
     * to avoid displaying a panel twice, i.e, to include more than one
     * panel with the same content.
     * 
     * @return true if
     */
    public boolean activePanelByKey(Object panelKey) {
    	for (int i = 0; i < tabbedPane.getComponentCount(); i++) {
    		Component comp = tabbedPane.getComponentAt(i);
    		if (comp instanceof PanelKey) {
    			Object key = ((PanelKey)comp).getKey();
    			if (panelKey.equals(key)) {
    				tabbedPane.setSelectedIndex(i);
    				return true;
    			}
    		}
    	}
    	return false;
    }


    /**
     * Remove a panel from the list of tabbed panels the main window.
     * The panel is referenced by its instance.<p/>
     * If panel implements the class {@link ClientPanel}, the
     * method <code>closing()</code> is called before it's removed.
     *  
     * @param panel is the panel to be removed from the main window
     */
    public void removePanel(JPanel panel) {
    	if (panel instanceof ClientPanel)
    		((ClientPanel)panel).closing();
    	tabbedPane.remove(panel);
    }


    /**
     * Called to open the main window when application starts. Prepare the 
     * main window to be displayed, initializing some components and displaying
     * the login panel (class {@link LoginPanel}) 
     */
    public void open() {
		frame.setTitle(Messages.getString("mdrtb_system") + "  " + App.instance().getVersionNumber() + " - " + App.instance().getBuildNumber()); //$NON-NLS-1$
		frame.setBounds(frame.getX(), frame.getY(), 1200, 600);

		txtUser.setVisible(false);
		txtWorkspace.setVisible(false);
		txtLastSync.setVisible(false);

    	// prevent Substance of changing the colors 
		pnlStatus.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1));
		((JPanel)frame.getContentPane()).putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1));

		frame.setVisible(true);

		startupPanel = new StartupPanel();
		startupPanel.refresh();
		getFrame().add(startupPanel);
		
		// notification about changes in the language
		EventBusService.observeEvent(AppEvent.LANGUAGE_CHANGED, this);

		// notification about user that logged in 
		EventBusService.observeEvent(AppEvent.LOGGEDIN, this);

		// notification about application exit request
		EventBusService.observeEvent(AppEvent.REQUEST_APP_EXIT, this);
    }


	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		// language was changed ?
		if (AppEvent.LANGUAGE_CHANGED.equals(event)) {
			frame.setTitle(Messages.getString("mdrtb_system"));
			return;
		}

		// user has logged in ?
		if (AppEvent.LOGGEDIN.equals(event)) {
			afterLogin();
			return;
		}
		
		if (AppEvent.REQUEST_APP_EXIT.equals(event)) {
			getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			getFrame().dispose();
		}
	}


	/**
	 * Return the instance of the main window in a static method
	 * @return
	 */
	public static MainWindow instance() {
		return mainWindowInstance;
	}


	/**
	 * Create the application.
	 */
	public MainWindow() {
		initLookAndFeel();
		initialize();
		loadIcon();
	}
    
    /**
     * Load the application icon and set it to the frame of the main window.
     */
    private void loadIcon() {
    	URL img = this.getClass().getResource("/resources/images/app-icon.png");
    	ImageIcon appIcon = new ImageIcon(img);
    	frame.setIconImage(appIcon.getImage());
	}


	/**
     * Initialize Look and Feel using the Substance framework.
     */
    protected void initLookAndFeel() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
			UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
			UIManager.getDefaults().put("JPanel.background", Color.BLACK);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error selecting theme");
		}

		setUIFont(UiConstants.defaultFont);
    }
    
    
    /**
     * Set the default font used by the {@link UIManager} of the swing application.
     * @param font
     */
    public void setUIFont(FontUIResource font) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, font);
		}
    }

    
    /**
     * Called when the user is successfully logged in
     */
    protected void afterLogin() {
		txtUser.setVisible(true);
		txtWorkspace.setVisible(true);
		txtLastSync.setVisible(true);

		txtUser.setText(UserSession.getUser().getName());
		txtWorkspace.setText(UserSession.getWorkspace().getName().toString());
		txtLastSync.setText(LocaleDateConverter.getDisplayDate(UserSession.getServerSignature().getLastSyncDate(), false) + " - "
								+ LocaleDateConverter.getAsElapsedTime(new Period(UserSession.getServerSignature().getLastSyncDate(), DateUtils.getDate())));

    	// close login panel
    	startupPanel.closeHandler();
    	getFrame().getContentPane().remove(startupPanel);
    	startupPanel = null;

    	// instantiate the tabbed panel of the main window
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				clientTabChanged();
			}
		});

		// open the default panel
		JPanel pnl = new CaseHomePanel();
		addPanel(pnl, App.getMessage("cases"), false);

		addToolbar();
   	}

    
    /**
     * Add the toolbar to the page
     */
    protected void addToolbar() {
		// add the toolbar
		toolbar = new JToolBar();
		GuiUtils.setBackground(toolbar, UiConstants.darkBackgroundColor);
		getFrame().getContentPane().add(toolbar, BorderLayout.PAGE_START);
		
		JButton btnNewNotification = new JButton(Messages.getString("cases.new"));
		btnNewNotification.setIcon(new AwesomeIcon(AwesomeIcon.ICON_PLUS_SIGN, Color.WHITE, 18));
		btnNewNotification.setForeground(Color.WHITE);
		btnNewNotification.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		newNotificationClick(DiagnosisType.CONFIRMED);
	    	}
		});
		toolbar.add(btnNewNotification);
		
		JButton btnNewSusp = new JButton(Messages.getString("cases.newsusp"));
		btnNewSusp.setIcon(new AwesomeIcon(AwesomeIcon.ICON_PLUS_SIGN, Color.WHITE, 18));
		btnNewSusp.setForeground(Color.WHITE);
		btnNewSusp.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		newNotificationClick(DiagnosisType.SUSPECT);
	    	}
		});
		toolbar.add(btnNewSusp);

		JButton btn = new JButton("Synchronize");
		btn.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CLOUD_UPLOAD, Color.WHITE, 18));
		btn.setForeground(Color.WHITE);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronizationClick();
			}
		});
		toolbar.add(btn);
    }


	/**
	 * Called when the user clicks on the synchronization button
	 */
	protected void synchronizationClick() {
		if (JOptionPane.showConfirmDialog(this.getFrame(), Messages.getString("desktop.sync.confirm"), 
				Messages.getString("desktop.sync"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
			return;
		
		SynchronizeDialog dlg = new SynchronizeDialog();
		dlg.execute();
	}


	/**
	 * Called when the tab (the displayed content) is changed. If the current panel
	 * and the new panel extends the class {@link ClientPanel}, they will be notified
	 * when it is deactivated (by the method <code>deactivate()</code>) and when
	 * the new panel is activated (by the method <code>activate()</code>)
	 */
	protected void clientTabChanged() {
		JPanel pnl = (JPanel)tabbedPane.getComponentAt( tabbedPane.getSelectedIndex() );
		if ((currentView != null) && (currentView instanceof ClientPanel))
			((ClientPanel)currentView).deactivate();
			
		if (pnl instanceof ClientPanel)
			((ClientPanel)pnl).activate();
		
		currentView = pnl;
	}

	
	/**
	 * Start the new notification of a new suspect or case 
	 * @param diagnosisType is the type to be notified - suspect or case
	 */
	protected void newNotificationClick(DiagnosisType diagnosisType) {
		NewNotificationFlow.instance().start(diagnosisType);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(UiConstants.selectedRow);
		frame.setBounds(100, 100, 742, 439);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pnlStatus = new JPanel();
		pnlStatus.setBackground(UiConstants.darkBackgroundColor);
		pnlStatus.setBorder(new EmptyBorder(3, 3, 3, 3));
		frame.getContentPane().add(pnlStatus, BorderLayout.SOUTH);
		pnlStatus.setLayout(new GridLayout(0, 3, 0, 0));

		txtUser = new JLabel(); //$NON-NLS-1$ //$NON-NLS-1$
		txtUser.setFont(txtUser.getFont().deriveFont(Font.BOLD));
		txtUser.setForeground(new Color(255, 255, 255));
		txtUser.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/user.png")));
		pnlStatus.add(txtUser);

		txtLastSync = new JLabel(); //$NON-NLS-1$ //$NON-NLS-1$
		txtLastSync.setFont(txtLastSync.getFont().deriveFont(Font.BOLD));
		txtLastSync.setForeground(new Color(255, 255, 255));
		txtLastSync.setIcon(new AwesomeIcon(AwesomeIcon.ICON_CLOUD_UPLOAD, Color.WHITE, 18));
		pnlStatus.add(txtLastSync);
		
		txtWorkspace = new JLabel(); //$NON-NLS-1$
		txtWorkspace.setForeground(new Color(255, 255, 255));
		txtWorkspace.setFont(txtWorkspace.getFont().deriveFont(Font.BOLD));
		pnlStatus.add(txtWorkspace);
		
/*		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu(Messages.getString("cases")); //$NON-NLS-1$
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem(Messages.getString("home")); //$NON-NLS-1$
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem(Messages.getString("cases.new")); //$NON-NLS-1$
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu(Messages.getString("admin")); //$NON-NLS-1$
		menuBar.add(mnNewMenu_1);
*/	}



	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

}
