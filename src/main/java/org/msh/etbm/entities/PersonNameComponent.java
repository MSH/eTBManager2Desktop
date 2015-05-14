package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.NotNull;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.enums.NameComposition;

@Embeddable
public class PersonNameComponent {

	@Column(length=100, name="PATIENT_NAME")
	@NotNull
	private String name;
	
	@Column(length=100, name="middleName")
	private String middleName;
	
	@Column(length=100, name="lastName")
	private String lastName;

	/**
	 * Check if the name was provided
	 * @return
	 */
	public boolean isEmpty() {
		return (name == null || name.trim().isEmpty()) &&
				(middleName == null || middleName.trim().isEmpty()) &&
				(lastName == null || lastName.trim().isEmpty());
	}
	
	/**
	 * Return the name to be displayed using the workspace name composition
	 * @return
	 */
	public String getDisplayName() {
		return getDisplayName(getNameComposition());
	}


	/**
	 * Return the name composition based on the default workspace of the user
	 * @return
	 */
	public NameComposition getNameComposition() {
		Workspace ws = (Workspace)App.getComponent("defaultWorkspace");
		return ws != null? ws.getPatientNameComposition() : NameComposition.FIRST_MIDDLE_LASTNAME;
	}
	
	/**
	 * Return the displayable string based on the name composition
	 * @param nameComp
	 * @return
	 */
	public String getDisplayName(NameComposition nameComp) {
		switch (nameComp) {
		case FIRST_MIDDLE_LASTNAME:
		   return (name != null? name: "") + (middleName != null? " " + middleName: "") + (lastName != null? " " + lastName: "");

		case FULLNAME:
			return name;
		
		case FIRSTSURNAME:
			return (name != null? name: "") + (middleName != null? ", " + middleName: "");
			
		case LAST_FIRST_MIDDLENAME:
			return (lastName != null? lastName + ", ": "") + (name != null? name: "") + ((middleName != null) && (!middleName.isEmpty())? ", " + middleName: "");
			
		case SURNAME_FIRSTNAME:
			return (middleName != null? middleName + ", ":"") + (name != null? name: "");

		default:
		   return (name != null? name: "") + (middleName != null? " " + middleName: "") + (lastName != null? " " + lastName: "");
		}
	}


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
		this.name = name != null? name.trim(): null;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName != null? middleName.trim() : null;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName != null? lastName.trim() : null;
	}


	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[name = " + name + ", middleName = " + middleName + ", lastName = " + lastName + "]";
	}
}
