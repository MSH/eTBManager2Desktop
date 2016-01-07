package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;
import org.msh.etbm.entities.enums.CaseClassification;

@Entity
@Table(name="userpermission")
public class UserPermission implements Serializable, Comparable<UserPermission> {
	private static final long serialVersionUID = 7565244271956307412L;

	@Id
    private Integer id;

	@ManyToOne
	@JoinColumn(name="ROLE_ID")
	@NotNull
	private UserRole userRole;

	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="PROFILE_ID")
	@NotNull
	private UserProfile userProfile;

	private boolean canExecute;
	private boolean canChange;
	private boolean grantPermission;
	
	/**
	 * Case classification that this permission shall be applied
	 */
	private CaseClassification caseClassification;


	@Override
	public String toString() {
//		Map<String, String> msgs = Messages.instance();

		String s = "";
//		if (!userRole.getName().equals("-"))
//			s = userRole.getDisplayName();
//		
//		if ((caseClassification != null) && (userRole.isByCaseClassification())) {
//			s = msgs.get(caseClassification.getKey()) + " - " + s;
//		}
		return s;
	}

	
	public boolean isGrantPermission() {
		return grantPermission;
	}

	public void setGrantPermission(boolean grantPermission) {
		this.grantPermission = grantPermission;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public int compareTo(UserPermission userPermission) {
		return (((getUserRole() != null) && (userPermission.getUserRole() != null))? 
				userRole.compareTo(userPermission.getUserRole()): 
				0);
	}

	/**
	 * @return the canChange
	 */
	public boolean isCanChange() {
		return canChange;
	}

	/**
	 * @param canChange the canChange to set
	 */
	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}

	/**
	 * @return the caseClassification
	 */
	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	/**
	 * @param caseClassification the caseClassification to set
	 */
	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}


	/**
	 * @return the canExecute
	 */
	public boolean isCanExecute() {
		return canExecute;
	}


	/**
	 * @param canExecute the canExecute to set
	 */
	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

}
