/**
 * 
 */
package org.msh.etbm.desktop.common;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.startup.LoginPanel;

/**
 * Panel containing the system logo, its brand name and small description
 * @author Ricardo Memoria
 *
 */
public class SystemLogo extends MigLayoutPanel {
	private static final long serialVersionUID = -6532787345012575911L;

	private JXLabel txtDesc;
	private JLabel txtSystem;
	
	/**
	 * Default constructor
	 */
	public SystemLogo() {
		super("wrap 2", "", "[top][top]");
		
		JLabel txt = new JLabel();
		txt.setIcon(new ImageIcon(LoginPanel.class.getResource("/resources/images/logo_64x64.png")));
		add(txt, "span 1 2");
		
		txtSystem = new JLabel(Messages.getString("mdrtb_system"));
		txtSystem.setFont(UiConstants.h1Font);
		add(txtSystem);

		txtDesc = new JXLabel(Messages.getString("mdrtbsys"));
		txtDesc.setVerticalAlignment(SwingConstants.TOP);
		txtDesc.setBounds(165, 69, 265, 46);
		txtDesc.setLineWrap(true);
		add(txtDesc);
	}
	
	public void updateLanguage() {
		txtDesc.setText(Messages.getString("mdrtbsys"));
		txtSystem.setText(Messages.getString("mdrtb_system"));
	}
}
