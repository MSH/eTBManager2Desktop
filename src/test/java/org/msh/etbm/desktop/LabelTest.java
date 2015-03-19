/**
 * 
 */
package org.msh.etbm.desktop;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.swing.XViewUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class LabelTest extends JFrame{
	private static final long serialVersionUID = 529367551335276883L;

	private static final String[] texts = {
		"Banana",
		"Frequency",
		"Period",
		"This is a long text",
		"This is a very LOOOOONNNNGGGG text.. Let's see how it's displayed",
		"Drug resistance type"
	};
	
	private JXLabel[] labels;
	private JXLabel counter;
	private int size = 150;
	private JPanel pnl;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabelTest frame = new LabelTest();
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

	
	protected void run() {
		System.out.println("Run...");
		
		pnl = new JPanel();
		pnl.setLayout(null);
		getContentPane().add(pnl);
		
		JButton btn = new JButton("Test");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reduceSize();
			}
		});
		btn.setBounds(20, 20, 130, 30);
		pnl.add(btn);
		
		counter = new JXLabel();
		counter.setBounds(180, 20, 100, 20);
		counter.setText(Integer.toString(size));
		pnl.add(counter);

		updateLabels();
	}
	
	
	protected void updateLabels() {
		if (labels != null) {
			for (JXLabel lbl: labels) {
				pnl.remove(lbl);
			}
		}
		labels = new JXLabel[texts.length];
		
		int y = 50;
		int i = 0;
		for (String s: texts) {
			JXLabel lbl = new JXLabel();
			labels[i] = lbl;
			lbl.setText(s);
			lbl.setFont(UiConstants.fieldLabel);
			lbl.setLineWrap(true);
			pnl.add(lbl);
			int w = XViewUtils.calcTextWidth(lbl);
			if (w > size) {
				w = size;
			}
			int h = XViewUtils.calcTextHeight(lbl, w);
			lbl.setBounds(30, y, w, h);
			y += h + 8;
			i++;
		}
	}
	
	public void reduceSize() {
		size -= 10;
		counter.setText(Integer.toString(size));
		updateLabels();
	}
}
