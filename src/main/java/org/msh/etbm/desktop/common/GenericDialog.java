package org.msh.etbm.desktop.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.JButtonEx.ButtonStyle;
import org.msh.etbm.desktop.components.AwesomeIcon;

/**
 * Abstraction of a dialog window used as a base class for most of the dialog
 * windows in the system. Basically this window contains a footer bar with two buttons:
 * OK and cancel button.<p/>
 * This generic dialog allows customization of most of the dialog layout, like hidding
 * and showing buttons and changing its label.
 *  
 * @author Ricardo Memoria
 *
 */
public abstract class GenericDialog extends JDialog {
	private static final long serialVersionUID = 2737579752926005736L;

	private final JPanel contentPanel = new JPanel();
	
	private JButton okButton;
	private JButton cancelButton;
	
	private boolean confirmed;

	/**
	 * Show the window in a modal form, and return true if user confirmed
	 * the operation, i.e, clicked on the OK button
	 * @return true if user confirmed the operation or false if user canceled the dialog
	 */
	public boolean showModal() {
		setLocationRelativeTo( MainWindow.instance().getFrame() );
		setIconImage(MainWindow.instance().getFrame().getIconImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setVisible(true);
		return confirmed;
	}
	
	
	/**
	 * Standard handler called when the user clicks on the OK button
	 */
	public void commandOk() {
		if (!save())
			return;
		confirmed = true;
		setVisible(false);
	}
	
	/**
	 * Standard handler called when user clicks on the cancel button
	 */
	public void commandCancel() {
		cancel();
		confirmed = false;
		setVisible(false);
	}
	
	
	public void hideOkButton() {
		
	}
	
	/**
	 * Standard handler to save the work done. Called when the user clicks on the 
	 * confirmation button
	 */
	public abstract boolean save();


	/**
	 * Called when the operation is canceled by the user
	 */
	public void cancel() {
		
	}

	
	/**
	 * Create the dialog.
	 */
	public GenericDialog() {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 500, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(6, 4, 6, 4));
			buttonPane.setBackground(Color.GRAY);
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButtonEx(Messages.getString("form.ok"), AwesomeIcon.ICON_OK, ButtonStyle.BIG);
				okButton.setDefaultCapable(true);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commandOk();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
//				okButton.setPreferredSize(new Dimension((int)okButton.getPreferredSize().getWidth() + 20, okButton.getHeight()));
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButtonEx(Messages.getString("form.cancel"), AwesomeIcon.ICON_REMOVE, ButtonStyle.BIG);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commandCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}


	/**
	 * Return true if the operation was confirmed, or false if the user canceled it
	 * @return the status of the operation in a boolean value
	 */
	public boolean isConfirmed() {
		return confirmed;
	}
	
	/**
	 * Return the {@link Container} component that will contain the other components
	 * to be displayed in the client area of the form
	 * @return instance of the {@link Container} class
	 */
	public Container getClientContent() {
		return contentPanel;
	}


	/**
	 * Return the instance of the {@link JButton} component that will be displayed as a OK button
	 * @return instance of {@link JButton} component
	 */
	public JButton getOkButton() {
		return okButton;
	}


	/**
	 * Change the 
	 * @param okButton the okButton to set
	 */
/*	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}
*/

	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}


	/**
	 * @param cancelButton the cancelButton to set
	 */
	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

}
