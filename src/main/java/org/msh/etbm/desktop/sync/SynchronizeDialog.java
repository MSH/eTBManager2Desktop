/**
 * 
 */
package org.msh.etbm.desktop.sync;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.eventbus.EventBusService;


/**
 * Dialog displayed to show the progress of the data synchronization with the server
 * 
 * @author Ricardo Memoria
 *
 */
public class SynchronizeDialog extends JDialog {
	private static final long serialVersionUID = 8516057085357361814L;
	
	private JXLabel[] labels = new JXLabel[4];
	private int stepIndex;
	
	private JTextArea textArea;
	private JButton btnClose;

	
	/**
	 * Default constructor
	 */
	public SynchronizeDialog() {
		super();
		setSize(600, 350);
		setTitle(Messages.getString("desktop.sync"));

		getContentPane().setLayout(new BorderLayout());

		JPanel pnlLeft = new JPanel();
		GuiUtils.setBackground(pnlLeft, UiConstants.darkBackgroundColor);
		pnlLeft.setPreferredSize(new Dimension(200, 100));
		
		pnlLeft.setLayout(new MigLayout("wrap 1,insets 12", "", "[]14[]14[]14[]"));

		// create left labels
		for (int i = 0; i < 4; i++) {
			labels[i] = addLabel("desktop.sync.step" + Integer.toString(i + 1));
			pnlLeft.add(labels[i]);
		}
		
		getContentPane().add(pnlLeft, BorderLayout.LINE_START);
		
		JPanel pnlRight = new JPanel();
		pnlRight.setLayout(new BorderLayout(4, 4));
		pnlRight.setBorder(new EmptyBorder(4, 4, 4, 4));

		textArea = new JTextArea(30, 30);
		textArea.setFont( new Font( "Monospaced", Font.PLAIN, 12 ));  
		textArea.setLineWrap(true);
		//textArea.setEditable(false);
		JScrollPane spane = new JScrollPane(textArea);
		spane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pnlRight.add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		
		JPanel pnlButton = new JPanel();
		pnlButton.setLayout(new FlowLayout());
		pnlButton.setBorder(new EmptyBorder(4,4,4,4));
		
		btnClose = new JButton(Messages.getString("form.close"));
		btnClose.setEnabled(false);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeForm();
			}
		});
		pnlButton.add(btnClose);
		pnlRight.add(pnlButton, BorderLayout.PAGE_END);
		
		getContentPane().add(pnlRight, BorderLayout.CENTER);
	}

	
	public void closeForm() {
		setVisible(false);
	}
	
	/**
	 * Move to next step
	 */
	protected void moveNextStep() {
		stepIndex++;
		labels[stepIndex].setForeground(Color.WHITE);
		labels[stepIndex].setFont(labels[0].getFont().deriveFont(Font.BOLD));
		labels[stepIndex].setIcon(new AwesomeIcon(AwesomeIcon.ICON_CIRCLE, Color.WHITE, 14));
	}
	
	

	protected JXLabel addLabel(String key) {
		JXLabel lb = new JXLabel(Messages.getString(key));
		lb.setForeground(new Color(200,200,200));
		lb.setLineWrap(true);
		return lb;
	}
	
	/**
	 * Display the dialog and start execution of synchronization
	 */
	public void execute() {
		stepIndex = -1;

		setLocationRelativeTo( MainWindow.instance().getFrame() );
		setIconImage(MainWindow.instance().getFrame().getIconImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);

		// create worker to start job in background
		SyncWorker worker = new SyncWorker();

		// add listener to event change
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				handleEvent(evt);
			}
		});
		worker.execute();
		setVisible(true);
	}

	
	protected void addLog(String msg) {
		textArea.append(msg + '\n');
	}

	/**
	 * Handle events from the synchronization worker
	 * @param evt instance of {@link PropertyChangeEvent}
	 */
	protected void handleEvent(PropertyChangeEvent evt) {
		if (SyncWorker.EVENT_NEXTSTEP.equals(evt.getPropertyName())) {
			moveNextStep();
			return;
		}
		
		if (SyncWorker.EVENT_SHOWMSG.equals(evt.getPropertyName())) {
			JOptionPane.showMessageDialog(this, evt.getOldValue().toString());
			addLog("\n* THE SERVER RETURNED A MESSAGE *");
			addLog(evt.getOldValue().toString());
			btnClose.setEnabled(true);
		}
		
		if (SyncWorker.EVENT_EXEC_SUCCESS.equals(evt.getPropertyName())) {
			JOptionPane.showMessageDialog(this, Messages.getString("desktop.sync.success"));
			btnClose.setEnabled(true);
			textArea.append("\n* SYNCHRONIZATION DONE");
			EventBusService.raiseEvent(AppEvent.CASES_REFRESH);
		}
		
		if (SyncWorker.EVENT_ADDLOG.equals(evt.getPropertyName())) {
			textArea.append(evt.getOldValue().toString() + '\n');
		}
	}

}
