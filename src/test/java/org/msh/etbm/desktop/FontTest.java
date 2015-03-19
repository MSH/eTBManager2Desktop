/**
 * 
 */
package org.msh.etbm.desktop;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.components.AwesomeIcon;

/**
 * @author Ricardo Memoria
 *
 */
public class FontTest extends JFrame {
	private static final long serialVersionUID = -6350229761418072138L;

	private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		
		try {
		    FontTest frame = new FontTest();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FontFormatException 
     */
    public FontTest() throws FontFormatException, IOException, URISyntaxException {
/*	URL fontfile = getClass().getResource("/resources/fonts/fontawesome-webfont.ttf");
	Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontfile.toURI()));
*/	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 450, 300);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 20, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);
	
	JButton btnNewButton = new JButton("Ok");
	btnNewButton.setIcon(new AwesomeIcon(AwesomeIcon.ICON_OK, btnNewButton));
	btnNewButton.setForeground(Color.BLACK);
	btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
	btnNewButton.setBounds(20, 5, 128, 38);
	contentPane.add(btnNewButton);
	
	JButton btnCancel = new JButton("Cancel");
	btnCancel.setIcon(new AwesomeIcon(AwesomeIcon.ICON_POWER_OFF, btnCancel));
	btnCancel.setForeground(Color.GRAY);
	btnCancel.setFont(new Font("Tahoma", Font.BOLD, 24));
	btnCancel.setBounds(20, 54, 188, 38);
	contentPane.add(btnCancel);
	
	JButton btnStar = new JButton("Star");
	btnStar.setIcon(new AwesomeIcon(AwesomeIcon.ICON_STAR, btnStar));
	btnStar.setForeground(new Color(70, 130, 180));
	btnStar.setFont(new Font("Tahoma", Font.BOLD, 40));
	btnStar.setBounds(20, 150, 200, 60);
	contentPane.add(btnStar);
    }
}
