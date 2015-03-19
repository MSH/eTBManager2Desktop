package org.msh.etbm.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.msh.xview.components.Form;
import org.msh.xview.impl.XmlFormReader;
*/
public class FormTest extends JFrame {
	private static final long serialVersionUID = -3336843553036645903L;

	private JScrollPane scrollPane;
	public FormTest() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.RED, 2));
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {200};
		gbl_panel.rowHeights = new int[] {58, 144, 50, 100, 10};
		gbl_panel.columnWeights = new double[]{1.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		panel.setLayout(gbl_panel);
		
		JPanel pnl1 = new JPanel();
		pnl1.setBackground(Color.GREEN);
		pnl1.setLayout(null);
		GridBagConstraints gbc_pnl1 = new GridBagConstraints();
		gbc_pnl1.fill = GridBagConstraints.BOTH;
		gbc_pnl1.insets = new Insets(0, 0, 5, 0);
		gbc_pnl1.gridx = 0;
		gbc_pnl1.gridy = 0;
		panel.add(pnl1, gbc_pnl1);
		
		JLabel lblNewLabel = new JLabel("Title of the block");
		lblNewLabel.setBounds(10, 2, 79, 19);
		pnl1.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(407, 11, 89, 23);
		pnl1.add(btnNewButton);
		
		JPanel pnl2 = new JPanel();
		pnl2.setBackground(new Color(128, 128, 128));
		GridBagConstraints gbc_pnl2 = new GridBagConstraints();
		gbc_pnl2.insets = new Insets(0, 0, 5, 0);
		gbc_pnl2.fill = GridBagConstraints.BOTH;
		gbc_pnl2.gridx = 0;
		gbc_pnl2.gridy = 1;
		panel.add(pnl2, gbc_pnl2);
		
		JPanel pnl3 = new JPanel();
		pnl3.setBackground(new Color(205, 133, 63));
		GridBagConstraints gbc_pnl3 = new GridBagConstraints();
		gbc_pnl3.insets = new Insets(0, 0, 5, 0);
		gbc_pnl3.fill = GridBagConstraints.BOTH;
		gbc_pnl3.gridx = 0;
		gbc_pnl3.gridy = 2;
		panel.add(pnl3, gbc_pnl3);
		
		JPanel pnl4 = new JPanel();
		pnl4.setBackground(new Color(240, 128, 128));
		GridBagConstraints gbc_pnl4 = new GridBagConstraints();
		gbc_pnl4.fill = GridBagConstraints.BOTH;
		gbc_pnl4.gridx = 0;
		gbc_pnl4.gridy = 3;
		panel.add(pnl4, gbc_pnl4);
		getContentPane().add(scrollPane);
		
/*		pnl1.setMaximumSize(new Dimension((int)pnl1.getPreferredSize().getWidth(), 30));
		pnl2.setMaximumSize(new Dimension((int)pnl2.getPreferredSize().getWidth(), 100));
		pnl3.setMaximumSize(new Dimension((int)pnl3.getPreferredSize().getWidth(), 30));
		pnl4.setMaximumSize(new Dimension((int)pnl4.getPreferredSize().getWidth(), 100));
		
		pnl1.setPreferredSize(new Dimension((int)pnl1.getPreferredSize().getWidth(), 30));
		pnl2.setPreferredSize(new Dimension((int)pnl2.getPreferredSize().getWidth(), 100));
		pnl3.setPreferredSize(new Dimension((int)pnl3.getPreferredSize().getWidth(), 30));
		pnl4.setPreferredSize(new Dimension((int)pnl4.getPreferredSize().getWidth(), 100));*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FormTest test = new FormTest();
		test.setVisible(true);
/*		InputStream stream = FormTest.class.getResourceAsStream("/xview/casenew.xview.xml");

		StringBuilder builder = new StringBuilder();
		Reader reader = new BufferedReader(new InputStreamReader(stream));
		char[] buffer = new char[8192];
		int read;
		try {
			while ((read = reader.read(buffer, 0, buffer.length)) > 0)
				builder.append(buffer, 0, read);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String s = builder.toString();
		
		System.out.println(s);
		
		XmlFormReader formreader = new XmlFormReader();
		Form form = formreader.read(s);
		
		System.out.println("id = " + form.getId());
		System.out.println("title = " + form.getTitle());
		assert(form.getComponents().size() == 1);
*/	}
}
