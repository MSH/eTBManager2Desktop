/**
 * 
 */
package org.msh.etbm.desktop.startup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.CloseListener;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SelectItem;
import org.msh.etbm.desktop.components.JPanelFading;
import org.msh.etbm.desktop.databases.DatabaseManager;
import org.msh.etbm.desktop.databases.PackageList;
import org.msh.etbm.entities.ServerSignature;
import org.msh.etbm.services.login.ServerSignatureServices;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class StartupPanel extends JPanel implements Refreshable, CloseListener, EventBusListener  {
	private static final long serialVersionUID = 8222405147640216193L;

	private static final int INNER_PANEL_PADDING = 4;
	
	private ImageIcon bgImage;
//	private LoginPanel loginPanel;
//	private LoadPackagePanel loadPackagePanel;
	private JPanelFading centralPanel;
	private JPanel contentPanel;
	private JPanel pnlTop;
	private JComboBox cbLanguage;
	private JLabel txtLanguage;
	
	private ServerCredentials credentials;


	/**
	 * Default constructor
	 */
	public StartupPanel() {
		super();

		setLayout(null);

		// create background image
		bgImage = new ImageIcon(getClass().getResource("/resources/images/bg_login.jpg"));
		
		pnlTop = new JPanel();
		pnlTop.setBackground(UiConstants.darkBackgroundColor);
		pnlTop.setLayout(null);
		add(pnlTop);
		
		cbLanguage = new JComboBox();
		cbLanguage.setBounds(200, 4, 28, 20);
		pnlTop.add(cbLanguage);

		txtLanguage = new JLabel("Language:");
		txtLanguage.setBounds(10, 7, 109, 14);
		txtLanguage.setForeground(Color.WHITE);
		pnlTop.add(txtLanguage);

		// create the central panel
		centralPanel = new JPanelFading();
		centralPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(220, 220, 220), new Color(128, 128, 128)));
		centralPanel.setBackground(UiConstants.panelBackgroundColor);
		centralPanel.setForeground(SystemColor.inactiveCaption);
		centralPanel.setLayout(null);
		centralPanel.setVisible(false);
		add(centralPanel);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateCentralPanel();
			}
		});
		
		EventBusService.observeEvent(AppEvent.LANGUAGE_CHANGED, this);
		EventBusService.observeEvent(StartupEvent.OPEN_LOGIN, this);
		EventBusService.observeEvent(StartupEvent.EXECUTE_INIFILE, this);
		EventBusService.observeEvent(StartupEvent.EXECUTE_INIFILE_FINISHED, this);
		EventBusService.observeEvent(StartupEvent.OPEN_WIZARD, this);
		EventBusService.observeEvent(StartupEvent.DOWNLOAD_INIFILE, this);
		EventBusService.observeEvent(StartupEvent.DOWNLOAD_INIFILE_FINISHED, this);
	}

	
	/**
	 * Initialize combo box with available languages
	 */
	protected void initializeComboLanguage() {
		// initialize combo of languages
		SelectItem selitem = null;
		for (Locale locale: Messages.getAvailableLocales()) {
			SelectItem item = new SelectItem(locale, locale.getDisplayName(locale));
			cbLanguage.addItem(item);
			if (locale.getLanguage().equals(Locale.getDefault().getLanguage()))
				selitem = item;
		}
		cbLanguage.setSelectedItem(selitem);
		// action called when language is changed
		cbLanguage.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				Messages.changeLocale((Locale)((SelectItem)cbLanguage.getSelectedItem()).getValue());
				txtLanguage.setText(Messages.getString("User.language"));
//				if (loginPanel != null)
//					loginPanel.updateLabels();
			}
		});
		cbLanguage.setSize(cbLanguage.getPreferredSize());
	}
	

	/** {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this); 
	}


	/** {@inheritDoc}
	 */
	@Override
	public void refresh() {
		initializeComboLanguage();

		new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((Timer)e.getSource()).stop();
				startup();
			}
		}).start();
	}


	/**
	 * Initialize the execution of the startup panel, which is at the moment that the panel 
	 * is already visible in the screen
	 */
	protected void startup() {
		// is there any file to be imported ?
		if (PackageList.instance().isAnyPackageAvailable())
			 EventBusService.raiseEvent(StartupEvent.EXECUTE_INIFILE);
		else {
			if (!DatabaseManager.instance().isDatabaseInitialized())
				 EventBusService.raiseEvent(StartupEvent.OPEN_WIZARD);
			else {
				EventBusService.raiseEvent(StartupEvent.OPEN_LOGIN);
			}
		}
	}


	/**
	 * Change the view to the selected class
	 * @param pnlClass
	 */
	private <E> E showContentPanel(Class<? extends JPanel> pnlClass) {
		JPanel panel;
		try {
			panel = pnlClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		removeContentView();

		// adjust panel size to the new child panel
		Dimension size = panel.getSize();
		centralPanel.setSize((int)size.getWidth() + (INNER_PANEL_PADDING * 2), (int)size.getHeight() + (INNER_PANEL_PADDING * 2));
		centralPanel.add(panel);
		panel.setLocation(INNER_PANEL_PADDING, INNER_PANEL_PADDING);
		updateCentralPanel();
		
		contentPanel = panel;

		// show the panel with a fade animation
		final Refreshable refreshable = (Refreshable)panel;
		centralPanel.fadeIn(400);
		new Timer(400, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((Timer)e.getSource()).stop();
				refreshable.refresh();
			}
		}).start();
		
		return (E)panel;
	}
	
	
	/**
	 * Remove all components of the central panel and call its CloseListener interface if available
	 */
	private void removeContentView() { 
		// remove the components and check if they implement the closeListener interface
		for (Component comp: centralPanel.getComponents()) {
			if (comp instanceof CloseListener)
				((CloseListener) comp).closeHandler();
		}

		centralPanel.removeAll();
	}
	
 	/**
 	 * Update the position of the login panel when the window is resized
 	 */
 	protected void updateCentralPanel() {
 		int x = (getWidth() - centralPanel.getWidth()) / 2;
 		int y = (getHeight() - centralPanel.getHeight()) / 2;
 		centralPanel.setLocation(x, y + 10);
 		
 		pnlTop.setBounds(0, 0, getWidth(), 30);
	}


	/** {@inheritDoc}
	 */
	@Override
	public void closeHandler() {
		removeContentView();
	}

	
	/**
	 * Store the server URL given by the user
	 */
	protected void storeServerUrl() {
		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				ServerSignatureServices srv = App.getComponent(ServerSignatureServices.class);
				ServerSignature sig = srv.getServerSignature();

				// is credential available?
				if (credentials != null) {
					sig.setServerUrl(credentials.getServer());
				}
				else {
					sig.setServerUrl(sig.getPageRootURL());
				}
				
				srv.updateServerSignature(sig);
			}
		});
		
	}

	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if (event == StartupEvent.EXECUTE_INIFILE) {
			executeIniFile(data);
			return;
		}

		// initiaization file finished
		if (event == StartupEvent.EXECUTE_INIFILE_FINISHED) {
			storeServerUrl();
			showContentPanel(LoginPanel.class);
			return;
		}
		
		// must open login page ?
		if (event == StartupEvent.OPEN_LOGIN) {
			showContentPanel(LoginPanel.class);
			return;
		}

		// must open wizard ?
		if (event == StartupEvent.OPEN_WIZARD) {
			showContentPanel(StartupWizard.class);
			return;
		}
		
		// language has changed ?
		if (event == AppEvent.LANGUAGE_CHANGED) {
			// the LoadPackagePanel supports language change updating
			if (contentPanel.getClass() != LoadPackagePanel.class)
				showContentPanel(contentPanel.getClass());
			return;
		}
		
		// remote file must be downloaded ?
		if (event == StartupEvent.DOWNLOAD_INIFILE) {
			// show panel to download file and execute operation
			credentials = (ServerCredentials)data[0];
			DownloadIniFilePanel pnl = showContentPanel(DownloadIniFilePanel.class);
			pnl.execute(credentials);
		}
	}


	/**
	 * Show the window to start importing a list of available initialization
	 * file
	 * @param data array of {@link File} objects
	 */
	private void executeIniFile(Object[] data) {
		if (data != null) {
			PackageList pkgList = PackageList.instance();
			for (Object obj: data) {
				File file = (File)obj;
				pkgList.addPackageFile(file);
			}
		}
		showContentPanel(LoadPackagePanel.class);
	}
}
