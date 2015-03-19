package org.msh.etbm.desktop;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

import org.msh.customdata.CustomObject;
import org.msh.customdata.CustomProperties;
import org.msh.customdata.CustomPropertiesImpl;
import org.msh.etbm.desktop.xview.CustomFormDataModel;
import org.msh.etbm.entities.PersonNameComponent;
import org.msh.etbm.entities.enums.Gender;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.SwingFormManager;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class TestXViewFrame extends JFrame implements CustomObject {
	private static final long serialVersionUID = 2867868609419391204L;

	private CustomProperties data = new CustomPropertiesImpl();
	
	private JPanel contentPane;

	private JPanel pnlForm;
	private SwingFormContext frm;
	
	private PersonNameComponent username = new PersonNameComponent();
	private Integer number;
	private String phone;
	private String email = "rmemoria@msh.org";
	private String login;
	private String url;
	private String comments;
	private boolean checked;
	private Date date;
	private Gender gender;
	private List<Item> items;
	
	private static final String[] names = {"New York", "Rio de Janeiro", "Madrid", "Levanto"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestXViewFrame frame = new TestXViewFrame();
					frame.setVisible(true);
					frame.createForm();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public TestXViewFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("wrap 3"));
		
		JButton btnNewButton = new JButton("Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateForm();
			}
		});
		btnNewButton.setBounds(10, 11, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validateForm();
			}
		});
		btnValidate.setBounds(150, 11, 89, 23);
		contentPane.add(btnValidate);

		JButton btn = new JButton("Teste");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String)frm.getDataModel().getValue("form.email");
				frm.getDataModel().setValue("form.email", s + " teste");
			}
		});
		contentPane.add(btn);
		
		pnlForm = new JPanel();
		pnlForm.setBorder(new LineBorder(Color.ORANGE, 2));
//		pnlForm.setBounds(10, 45, 640, 500);
		contentPane.add(pnlForm, "span 3");

		try {
			UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
			UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
			UIManager.getDefaults().put("JPanel.background", Color.BLACK);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error selecting theme");
		}
	}

	@Override
	public CustomProperties getCustomProperties() {
		return data;
	}
	
	public CustomProperties getData() {
		return data;
	}
	
	public void showHi() {
		JOptionPane.showMessageDialog(this, "Hi XView form");
	}
	
	public String showWait() {
		JOptionPane.showMessageDialog(this, "Wait... Operation in progress");
		return "executing...";
	}
	
	/**
	 * Update the form window
	 */
	protected void updateForm() {
		pnlForm.removeAll();
		createForm();
		pnlForm.invalidate();
//		pnlForm.repaint();
	}

	
	protected void validateForm() {
		frm.validate();
		System.out.println("\n** RESULT **\n");
		for (Item item: items) {
			String s = item.isSelected()? "(X)": "( )";
			System.out.println(s + ", name = " + item.getName() + ", population = " + item.getPopulation());
		}

		for (String prop: getCustomProperties().getProperties()) {
			System.out.println(prop + " = " + getCustomProperties().getValue(prop));
		}
	}
	
	/**
	 * Create the form and display it on the window
	 */
	protected void createForm() {
		SwingFormManager manager = new SwingFormManager("/xview/");
		frm = (SwingFormContext)manager.createFormAdapter("test2-new");
		frm.setDataModel(new CustomFormDataModel());
		frm.getDataModel().setValue("form", this);

		items = new ArrayList<TestXViewFrame.Item>();
		Random r = new Random();
		for (String s: names) {
			Item item = new Item();
			item.setName(s);
			item.setPopulation((r.nextInt(1000) * 1000) + r.nextInt(1000));
			item.setSelected(item.getPopulation() > 500000);
			items.add(item);
		}
		
		frm.getFormUI().setPreferredWidth(500);
		frm.getFormUI().update();
		JComponent pnl = frm.getFormUI().getComponent();
//		pnl.setBounds(2, 2, pnl.getWidth(), pnl.getHeight());
		System.out.println("TOTAL WIDTH = " + pnl.getWidth());
		pnlForm.add(pnl);
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the username
	 */
	public PersonNameComponent getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(PersonNameComponent username) {
		this.username = username;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	
	public class Item {
		private String name;
		private int population;
		private boolean selected;
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the population
		 */
		public int getPopulation() {
			return population;
		}
		/**
		 * @param population the population to set
		 */
		public void setPopulation(int population) {
			this.population = population;
		}
		/**
		 * @return the selected
		 */
		public boolean isSelected() {
			return selected;
		}
		/**
		 * @param selected the selected to set
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}


	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}


	/** {@inheritDoc}
	 */
	@Override
	public Object getCustomPropertiesId() {
		return 100;
	}

}
