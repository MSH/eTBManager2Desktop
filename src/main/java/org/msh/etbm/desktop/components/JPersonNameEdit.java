package org.msh.etbm.desktop.components;

import java.awt.FlowLayout;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.PersonNameComponent;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.NameComposition;
import org.msh.xview.swing.component.JTextFieldEx;
import org.msh.xview.swing.component.JTextFieldEx.CharCase;

/**
 * Create a component to edit a person name according to the 
 * name composition defined in the default workspace
 * 
 * @author Ricardo Memoria
 *
 */
public class JPersonNameEdit extends JPanel {
	private static final long serialVersionUID = -7471933639927207108L;

	private JTextFieldEx edtName;
	private JTextFieldEx edtMiddleName;
	private JTextFieldEx edtLastName;
	
	private CharCase charCase;

	// key event listeners
	private List<KeyListener> listeners;
	
	public JPersonNameEdit() {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		updateNameComposition();
	}

	/**
	 * @return the edtName
	 */
	public JTextField getEdtName() {
		return edtName;
	}

	/**
	 * @return the edtMiddleName
	 */
	public JTextField getEdtMiddleName() {
		return edtMiddleName;
	}

	/**
	 * @return the edtLastName
	 */
	public JTextField getEdtLastName() {
		return edtLastName;
	}

	
	/**
	 * Add a text field box to the panel
	 * @param width
	 * @return
	 */
	private JTextFieldEx addTextField(int columns) {
		JTextFieldEx edt = new JTextFieldEx();
		add(edt);
		edt.setMaxLength(100);
		edt.setColumns(columns);
		if (listeners != null)
			for (KeyListener l: listeners)
				edt.addKeyListener(l);
		return edt;
	}


	/**
	 * Update controls based on name composition
	 */
	private void updateNameComposition() {
		removeAll();

		Workspace ws = (Workspace)App.getComponent("defaultWorkspace");
		
		NameComposition nc;
		if (ws == null)
			 nc = NameComposition.FIRST_MIDDLE_LASTNAME;
		else nc = ws.getPatientNameComposition();

		// default panel width
		edtName = null;
		edtMiddleName = null;
		edtLastName = null;
		int defCols = 9;
	
		switch (nc) {
		case FULLNAME:
			edtName = addTextField(25);
			break;
		
		case SURNAME_FIRSTNAME:
			edtMiddleName = addTextField(defCols + 5);
			edtName = addTextField(defCols + 5);
			break;
			
		case FIRSTSURNAME:
			edtName = addTextField(defCols + 4);
			edtMiddleName = addTextField(defCols + 4);
			break;

		case LAST_FIRST_MIDDLENAME:
			edtLastName = addTextField(defCols);
			edtName = addTextField(defCols);
			edtMiddleName = addTextField(defCols);
			break;

		default:
			edtName = addTextField(defCols);
			edtMiddleName = addTextField(defCols);
			edtLastName = addTextField(defCols);
			break;
		}

		// calculates the width 
		int w = (edtName != null? edtName.getPreferredSize().width: 0) +
				(edtMiddleName != null? edtMiddleName.getPreferredSize().width: 0) +
				(edtLastName != null? edtLastName.getPreferredSize().width: 0) + 10;
		
		setSize(w, edtName.getHeight());
		updateCharCase();
	}

	/**
	 * @return the personName
	 */
	public PersonNameComponent getPersonName() {
		PersonNameComponent p = new PersonNameComponent();
		
		p.setName(edtName != null ? edtName.getText() : null);
		p.setMiddleName(edtMiddleName != null ? edtMiddleName.getText() : null);
		p.setLastName(edtLastName != null? edtLastName.getText() : null);

		return p;
	}

	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(PersonNameComponent personName) {
		if (edtName != null)
			edtName.setText(personName != null? personName.getName(): "");

		if (edtMiddleName != null)
			edtMiddleName.setText(personName != null? personName.getMiddleName(): "");

		if (edtLastName != null)
			edtLastName.setText(personName != null? personName.getLastName(): "");
	}

	/**
	 * @return the charCase
	 */
	public CharCase getCharCase() {
		return charCase;
	}

	/**
	 * @param charCase the charCase to set
	 */
	public void setCharCase(CharCase charCase) {
		this.charCase = charCase;
		updateCharCase();
	}

	/**
	 * Update the char case in use by the editors
	 */
	private void updateCharCase() {
		if (edtName != null)
			edtName.setCharCase(charCase);

		if (edtMiddleName != null)
			edtMiddleName.setCharCase(charCase);

		if (edtLastName != null)
			edtLastName.setCharCase(charCase);
	}


	/** {@inheritDoc}
	 * @see java.awt.Component#addKeyListener(java.awt.event.KeyListener)
	 */
	@Override
	public void addKeyListener(KeyListener listener) {
		if (listeners == null)
			listeners = new ArrayList<KeyListener>();
		listeners.add(listener);
		
		if (edtName != null)
			edtName.addKeyListener(listener);
		
		if (edtMiddleName != null)
			edtMiddleName.addKeyListener(listener);
		
		if (edtLastName != null)
			edtLastName.addKeyListener(listener);
	}


	/** {@inheritDoc}
	 * @see java.awt.Component#removeKeyListener(java.awt.event.KeyListener)
	 */
	@Override
	public void removeKeyListener(KeyListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
		
		if (edtName != null)
			edtName.removeKeyListener(listener);
		
		if (edtMiddleName != null)
			edtMiddleName.removeKeyListener(listener);
		
		if (edtLastName != null)
			edtLastName.removeKeyListener(listener);
	}
}
