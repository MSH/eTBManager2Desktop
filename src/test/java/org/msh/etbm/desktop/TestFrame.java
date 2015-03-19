package org.msh.etbm.desktop;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import org.jdesktop.swingx.JXLabel;

public class TestFrame extends JFrame {
	private static final long serialVersionUID = -4307970952998949363L;

	private JPanel contentPane;
	private JXLabel labels[];
	private JTextField comps[];
	private JButton button;
	
	private static final String captions[] = {"Banana", "Abacaxi", "Este é um caption longo com uma pergunta para o usuário", 
		"Amazônia", "Fábrica", "outro caption", "teste 2", "Amarelo abacate", "Paciente está em tratamento supervisionado?", 
		"Número de tratamentos anteriores", "Endereço atual é diferente do endereço de notificação?",
		"Data de notificação", "Data do diagnóstico"};


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public void initialize() {
		labels = new JXLabel[captions.length];
		comps = new JTextField[captions.length];

		for (int i = 0; i < captions.length; i++) {
			labels[i] = new JXLabel(captions[i] + " :");
			comps[i] = new JTextField(20);
			
			contentPane.add(labels[i]);
			contentPane.add(comps[i]);
		}

		button = new JButton("Teste");
		contentPane.add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				labels[2].setVisible( !labels[2].isVisible() );
				comps[2].setVisible( !comps[2].isVisible() );
				
				labels[3].setVisible( !labels[3].isVisible() );
				comps[3].setVisible( !comps[3].isVisible() );
				updatePositions();
			}
		});
		
		updatePositions();
	}
	
	/**
	 * Update the position of the components
	 */
	protected void updatePositions() {
		// calculate column size
		int colsize = 0;
		int maxwidth = 180;
		int minwidth = 100;
		for (JXLabel label: labels) {
			label.setLineWrap(false);
			if ((label.isVisible()) && (colsize < label.getPreferredSize().width))
				colsize = label.getPreferredSize().width;
		}
		
		if (colsize > maxwidth)
			colsize = maxwidth;
		
		if (colsize < minwidth)
			colsize = minwidth;
		
		colsize += 8;
		
		// position elements
		int x = 10;
		int y = 10;
		
		for (int i = 0; i < labels.length; i++) {
			JXLabel c1 = labels[i];
			Component c2 = comps[i];
			
			if (c1.isVisible()) {
				int w = c1.getPreferredSize().width;
				int h = c1.getPreferredSize().height;
				if (w > maxwidth) {
					w = maxwidth;
					c1.setLineWrap(true);
					View view = (View)c1.getClientProperty(BasicHTML.propertyKey);
					view.setSize(w, 0);
					h = (int)Math.ceil(view.getPreferredSpan(View.Y_AXIS));
				}
				c1.setBounds(x, y, w, h);
				c2.setBounds(x + colsize, y, c2.getPreferredSize().width, c2.getPreferredSize().height);

				h = c1.getHeight() > c2.getHeight()? c1.getHeight(): c2.getHeight();

				h+= 6;
				
				y += h;
			}
		}
		
		button.setBounds(x, y, button.getPreferredSize().width, button.getPreferredSize().height);
	}


	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	}
}
