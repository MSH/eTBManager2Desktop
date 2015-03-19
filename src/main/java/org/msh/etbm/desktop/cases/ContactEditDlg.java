package org.msh.etbm.desktop.cases;

import java.awt.Dimension;

import org.msh.etbm.entities.TbContact;
import org.msh.etbm.services.cases.ContactServices;

/**
 * Dialog window to display the contact evaluation form for editing an existing
 * contact of a case or insert a new one
 * 
 * @author Ricardo Memoria
 *
 */
public class ContactEditDlg extends CaseDataEditDlg<TbContact> {
	private static final long serialVersionUID = 1553239849483712244L;

	/**
	 * Default constructor
	 */
	public ContactEditDlg() {
		super("contact_edt", "contact", ContactServices.class);
	}


/*	*//** {@inheritDoc}
	 * @see org.msh.etbm.desktop.cases.CaseDataEditDlg#getEntityName()
	 *//*
	@Override
	public String getEntityName() {
		return Messages.getString("cases.contacts");
	}
*/
	/** {@inheritDoc}
	 */
	@Override
	protected Dimension getFormSize() {
		return new Dimension(620, 420);
	}
}
