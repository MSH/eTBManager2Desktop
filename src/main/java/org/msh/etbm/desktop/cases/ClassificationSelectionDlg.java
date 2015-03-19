package org.msh.etbm.desktop.cases;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.common.DataActionListener;
import org.msh.etbm.desktop.common.GenericDialog;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.services.login.UserSession;

/**
 * Display a dialog box to make the user select the case classification to be 
 * notified or registered as a suspect.
 * @author Ricardo Memoria
 *
 */
public class ClassificationSelectionDlg extends GenericDialog {
	private static final long serialVersionUID = -3187508827670027593L;

	private DiagnosisType diagnosisType;
	private CaseClassification caseClassification;
	
	private JTextArea txtTitle;
	private JPanel panelButtons;


	/**
	 * Launch the application.
	 */
	public static CaseClassification execute(DiagnosisType diagnosisType) {
		ClassificationSelectionDlg dialog = new ClassificationSelectionDlg();
		dialog.diagnosisType = diagnosisType;
		
		dialog.showModal();
		
		return dialog.caseClassification;
	}

	
	/**
	 * Called internally when the window is open 
	 */
	protected void openWindow() {
		
		UserSession session = UserSession.instance();
		
		for (CaseClassification cla: CaseClassification.values()) {
			if (session.isCanEditCaseByClassification(cla)) {
				String key = cla.getKey();
				if (diagnosisType == DiagnosisType.SUSPECT)
					key += ".suspect";

				JButton btn = new JButton( Messages.getString(key) );
				btn.addActionListener(new DataActionListener<CaseClassification>(cla) {
					@Override
					public void actionPerformed(ActionEvent e, CaseClassification data) {
						btnClassifClick(data);
					}
				});
				
				panelButtons.add(btn);
			}
		}
		
		switch (diagnosisType) {
		case CONFIRMED:
			setTitle( Messages.getString("cases.new"));
			txtTitle.setText( Messages.getString("cases.newnotif.title") );
			break;
		case SUSPECT:
			setTitle( Messages.getString("cases.newsusp"));
			txtTitle.setText( Messages.getString("cases.newsusp.title") );
			break;
		}
		
		getOkButton().setVisible(false);
		caseClassification = null;
	}

	
	/**
	 * Called when the user selects a classification
	 * @param classif
	 */
	protected void btnClassifClick(CaseClassification classif) {
		caseClassification = classif;
		setVisible(false);
	}


	/**
	 * Create the dialog.
	 */
	public ClassificationSelectionDlg() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				openWindow();
			}
		});
		setBounds(100, 100, 302, 283);
		
		panelButtons = new JPanel();
		
		txtTitle = new JTextArea();
		txtTitle.setBackground(SystemColor.control);
		txtTitle.setEditable(false);
		txtTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtTitle.setLineWrap(true);
		txtTitle.setWrapStyleWord(true);
		GroupLayout groupLayout = new GroupLayout(getClientContent());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(23)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(txtTitle, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(panelButtons, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
					.addContainerGap(25, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(28, Short.MAX_VALUE)
					.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
					.addGap(23))
		);
		panelButtons.setLayout(new GridLayout(3, 1, 0, 0));
		getClientContent().setLayout(groupLayout);
	}


	/** {@inheritDoc}
	 * @see org.msh.etbm.desktop.common.GenericDialog#save()
	 */
	@Override
	public boolean save() {
		return true;
	}

}
