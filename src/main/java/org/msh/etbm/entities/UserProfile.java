package org.msh.etbm.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.sync.SyncClear;
import org.msh.utils.FieldLog;

@Entity
@Table(name="userprofile")
public class UserProfile extends WSObject implements Serializable, Comparable<UserProfile> {
	private static final long serialVersionUID = -1798192936426485144L;

	@Id
    private Integer id;

	@Column(length=100)
	@NotNull
	@FieldLog(key="form.name")
	private String name;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="userProfile",cascade={CascadeType.ALL})
	@SyncClear
    private List<UserPermission> permissions = new ArrayList<UserPermission>();

	@Column(length=50)
	@FieldLog(key="global.legacyId")
	private String legacyId;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) 
			return true;
		
		if (!(obj instanceof UserProfile))
			return false;
		
		Integer objId = ((UserProfile)obj).getId();
		
		if (objId == null) 
			return false;
		
		return objId.equals(getId());
	}

	@Override
	public String toString() {
		return (getName() != null? name: super.toString());
	}
	
	public UserPermission permissionByRole(UserRole role, CaseClassification classif) {
		for (UserPermission up: getPermissions()) {
			if (up.getUserRole().equals(role)) {
				if ((classif == null) || ((classif != null) && (classif == up.getCaseClassification())))
					return up;
			}
		}
		return null;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int compareTo(UserProfile userProfile) {
		return name.compareTo(userProfile.getName());
	}

	public List<UserPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}
}
