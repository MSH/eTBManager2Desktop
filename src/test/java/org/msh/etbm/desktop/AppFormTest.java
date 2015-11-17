/**
 * 
 */
package org.msh.etbm.desktop;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.entities.MedicalExamination;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.services.login.Authenticator;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.SwingFormManager;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 * @author Ricardo Memoria
 *
 */
public class AppFormTest extends JFrame {
	private static final long serialVersionUID = 5406436046537436536L;

	private SwingFormContext frm;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFormTest frame = new AppFormTest();
					frame.setVisible(true);
					frame.setBounds(40, 40, 800, 600);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AppFormTest() {
		super();
		setTitle("App form Test");
		setBounds(100, 100, 682, 475);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	}
	
	public void run() {
		try {
			UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
			UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
			UIManager.getDefaults().put("JPanel.background", Color.BLACK);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error selecting theme");
		}
		getContentPane().setFont(UiConstants.defaultFont);
		App.initialize();
		List<Workspace> lst = App.getEntityManager().createQuery("from Workspace").getResultList();
		Authenticator auth = App.getComponent(Authenticator.class);
		if (!"SUCCESS".equals(auth.login("RICARDO", "doors21", lst.get(0).getId()))) {
			throw new RuntimeException("Authentication error");
		}
		createForm();
	}
	
	
	protected void createForm() {
		getContentPane().removeAll();
		
		JButton btn = new JButton("Refresh");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createForm();
			}
		});
		getContentPane().add(btn);
		
		SwingFormManager manager = new SwingFormManager("/xview/");
		frm = (SwingFormContext)manager.createFormAdapter("casenew");
//		frm = (SwingFormContext)manager.createFormAdapter("test2-new");
		frm.getDataModel().setValue("form", this);

		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				TbCase tbcase = App.getEntityManager().find(TbCase.class, new Integer(10));
				frm.getDataModel().setValue("tbcase", tbcase);
				frm.getDataModel().setValue("patient", tbcase.getPatient());
				frm.getDataModel().setValue("medicalexamination", new MedicalExamination());
				frm.getFormUI().setReadOnly(true);
				frm.getFormUI().setPreferredWidth(600);
				frm.getFormUI().update();
			}
		});
		JComponent pnl = frm.getFormUI().getComponent();
		System.out.println("TOTAL WIDTH = " + pnl.getWidth());
		getContentPane().add(pnl);
		getContentPane().repaint();
	}
	
	/**
	 * Refresh the content reading the form again
	 */
	public void refresh() {
		createForm();
	}
}
