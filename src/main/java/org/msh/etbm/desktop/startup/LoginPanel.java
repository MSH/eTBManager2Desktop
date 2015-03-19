package org.msh.etbm.desktop.startup;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.JButtonEx;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SystemLogo;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.services.login.Authenticator;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;

/**
 * Panel containing the login form that is displayed when the user executes
 * the application. This panel is destroyed when user logs into the system
 * or close the application without authentication.
 * <p/>
 * Another functionality implemented in this panel is the possibility for the
 * user to change the language (and its locale) used inside the system.
 * 
 * @author Ricardo Memoria
 *
 */
public class LoginPanel extends JPanel implements Refreshable, EventBusListener {
	private static final long serialVersionUID = 2139993587461645267L;
	private JTextField edtUsername;
	private JPasswordField edtPassword;
	private JComboBox cbServer;
	private JButton btnEnter;
	private JButton btnExit;
	private JLabel txtURL;
	private JLabel txtPassword;
	private JLabel txtUsername;
	private JCheckBox chkOffline;

	
	/**
	 * Default constructor
	 */
	public LoginPanel() {
		setForeground(Color.LIGHT_GRAY);
		setLayout(null);

		setForeground(SystemColor.inactiveCaption);
		setBackground(UiConstants.panelBackgroundColor);
		setLayout(null);
		setBounds(0, 0, 474, 450);

		SystemLogo logopnl = new SystemLogo();
		logopnl.setBounds(91, 31, 400, 80);
		logopnl.setBackground(getBackground());
		add(logopnl);

		txtUsername = new JLabel("User Name");
		txtUsername.setBounds(91, 126, 118, 14);
		add(txtUsername);
		
		txtPassword = new JLabel("Password");
		txtPassword.setBounds(91, 154, 118, 14);
		add(txtPassword);
		
		edtUsername = new JTextField();
		edtUsername.setColumns(10);
		edtUsername.setBounds(209, 121, 140, 24);
		add(edtUsername);
		
		edtPassword = new JPasswordField();
		edtPassword.setBounds(209, 151, 140, (int)edtPassword.getPreferredSize().getHeight());
		add(edtPassword);
		
		txtURL = new JLabel("System URL");
		txtURL.setBounds(91, 180, 108, 14);
		add(txtURL);
		
		cbServer = new JComboBox();
		cbServer.setEditable(true);
		cbServer.setBounds(209, 180, 221, (int)cbServer.getPreferredSize().getHeight());
		add(cbServer);
		
		chkOffline = new JCheckBox(Messages.getString("desktop.offline"));
		chkOffline.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				changeOfflineMode();
			}
		});
		chkOffline.setSelected(true);
		chkOffline.setBounds(209, 204, 140, 23);
		add(chkOffline);
		
		btnEnter = new JButtonEx(Messages.getString("login.enter"));
		btnEnter.setIcon(new AwesomeIcon(AwesomeIcon.ICON_SIGNIN, btnEnter));
		btnEnter.setDefaultCapable(true);
		btnEnter.setFont(UiConstants.buttonBigFont);
		btnEnter.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				cmdEnter();
			}
		});
		btnEnter.setBounds(129, 235, 140, 29);
		add(btnEnter);
		
		btnExit = new JButtonEx(Messages.getString("form.exit"));
		btnExit.setIcon(new AwesomeIcon(AwesomeIcon.ICON_SIGNOUT, btnExit));
		btnExit.addActionListener(new ActionListener() {
		    	@Override
			public void actionPerformed(ActionEvent e) {
				cmdExit();
			}
		});
		btnExit.setBounds(277, 235, 88, 29);
		add(btnExit);
		
		JLabel label_6 = new JLabel("Developed by MSH");
		label_6.setForeground(Color.GRAY);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 10));
		label_6.setBounds(184, 399, 101, 13);
		add(label_6);
		
		JLabel label_7 = new JLabel("Copyright © 2005 Management Sciences for Health, Inc. All rights reserved.");
		label_7.setForeground(Color.GRAY);
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 10));
		label_7.setBounds(36, 382, 409, 13);
		add(label_7);
		
		JLabel label_8 = new JLabel((String) null);
		label_8.setIcon(new ImageIcon(LoginPanel.class.getResource("/resources/images/usaid_siaps_400x61.png")));
		label_8.setBounds(36, 317, 409, 61);
		add(label_8);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(36, 289, 409, 13);
		add(separator);
		

		// temporary
		chkOffline.setVisible(false);
		cbServer.setVisible(false);
		txtURL.setVisible(false);
	}



	/**
 	 * Called when the user clicks on the <code>enter</code> button. Check user name and password, and 
 	 * display an error message in case there is a validation problem.
 	 */
 	protected void cmdEnter() {
 		// is user name informed ?
 		if (edtUsername.getText().isEmpty()) {
 			JOptionPane.showMessageDialog(this, Messages.getString("login.user_login") + ": " + Messages.requiredField());
 			edtUsername.requestFocus();
 			return;
 		}
 
 		char[] c = edtPassword.getPassword();
 		String passwd = new String(c);
 
 		// password was informed ?
 		if (passwd.isEmpty()) {
 			JOptionPane.showMessageDialog(this, Messages.getString("User.password") + ": " + Messages.requiredField());
 			edtPassword.requestFocus();
 			return;
 		}
 
 		// call authenticator service to log user or deny access
 		Authenticator auth = App.getComponent(Authenticator.class);
 		if (!auth.login(edtUsername.getText(), passwd, null)) {
 			JOptionPane.showMessageDialog(this, Messages.getString("org.jboss.seam.loginFailed"));
 			return;
 		}
 
 		// notify observers that the user logged in
 		EventBusService.raiseEvent(AppEvent.LOGGEDIN);
 	}

 	
 	/**
 	 * Mount the server combo box with the available servers
 	 */
/* 	protected void mountServerList() {
 		DatabaseManager dbman = DatabaseManager.instance();
 		SelectItem selected = null;
 		for (Database db: dbman.getDatabases()) {
 			SelectItem item = new SelectItem(db, db.getName());
 			cbServer.addItem(item);
 			if (db == dbman.getSelectedDatabase())
 				selected = item;
 		}

 		// select the default server
 		if (selected != null) {
 			cbServer.setSelectedItem(selected);
 		}
 	}
*/ 	
 	/**
 	 * Exit the login page without authentication. In this case, close the application.
 	 */
 	protected void cmdExit() {
 		// send command to end application
 		EventBusService.raiseEvent(AppEvent.REQUEST_APP_EXIT);
 	}
 	
	/**
	 * Initialize some fields and open the login dialog
	 */
	public void refresh() {
		changeOfflineMode();
		languageChangeHandler();
//		mountServerList();
		
		// initialize combo of languages
/*		SelectItem selitem = null;
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
				updateLabels();
			}
		});
		cbLanguage.setSize(cbLanguage.getPreferredSize());
*/		
//		bgImage = new ImageIcon(LoginPanel.class.getResource("/resources/images/bg_login.jpg"));
		
		setVisible(true);
//		pnlContent.setVisible(false);
		
		edtUsername.requestFocus();

/*		new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pnlContent.fadeIn(400);
				((Timer)e.getSource()).stop();
				edtUsername.requestFocus();
			}
		}).start();
*/	}
	

	/**
	 * Update the panel messages due to a change of the language.
	 */
	protected void languageChangeHandler() {
		txtUsername.setText(Messages.getString("login.user_login"));
		txtPassword.setText(Messages.getString("User.password"));
		txtURL.setText(Messages.getString("SystemConfig.systemURL"));
		chkOffline.setText(Messages.getString("LoginDlg.chckbxNewCheckBox.text"));
		btnEnter.setText(Messages.getString("login.enter"));
		btnExit.setText(Messages.getString("form.exit"));
	}


	/**
	 * Action called when check box "off-line mode" is clicked
	 */
	protected void changeOfflineMode() {
//		boolean b = !chkOffline.isSelected();
		
//		txtURL.setVisible(b);
//		edtURL.setVisible(b);
	}



	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if (AppEvent.LANGUAGE_CHANGED.equals(event))
			languageChangeHandler();
	}



	/** {@inheritDoc}
	 */
/*	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this); 
	}
*/}
