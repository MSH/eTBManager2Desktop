/**
 * 
 */
package org.msh.etbm.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.common.MigLayoutPanel;

/**
 * @author Ricardo Memoria
 *
 */
public class MigLayoutTest extends JFrame {
	private static final long serialVersionUID = -562803824992246726L;

	private JTextField edtName;
	private JXLabel txtName;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MigLayoutTest test = new MigLayoutTest();
					test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					test.setVisible(true);
					test.setBounds(100, 100, 600, 400);
					
					test.initializeLayout();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 */
	private void initializeLayout() {
		MigLayoutPanel pnl = new MigLayoutPanel("wrap 2");//, "[grow]");
		pnl.setBorder(new LineBorder(Color.RED));
		
		pnl.add(new JXLabel("Nome:"));
		edtName = new JTextField();
		edtName.setPreferredSize(new Dimension(200, edtName.getPreferredSize().height));
		edtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtName.setText(edtName.getText());
			}
		});
		pnl.add(edtName, "wrap");
		
		pnl.add(new JXLabel("Nome:"));
		txtName = new JXLabel();
		txtName.setLineWrap(true);
		pnl.add(txtName, "growx,wrap");
		
		for (int i = 1; i < 5; i++) {
			JXLabel lb = new JXLabel("Label " + i + ":");
			lb.setSize(150, lb.getPreferredSize().height);
			pnl.add( lb , "aligny top");
			
			JTextField fld = new JTextField();
			fld.setPreferredSize(new Dimension(150, fld.getPreferredSize().height));
			if (i == 3) {
				pnl.add(fld, "split 2,flowy");
				lb = new JXLabel("This is an error message");
				lb.setForeground(Color.RED);
				lb.setFont(lb.getFont().deriveFont(Font.BOLD));
				pnl.add(lb);
			}
			else {
				pnl.add( fld);
			}
		}
		
		
		getContentPane().add(pnl);
	}

}
