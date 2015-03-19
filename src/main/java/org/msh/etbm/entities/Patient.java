package org.msh.etbm.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.validator.NotNull;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.enums.Gender;
import org.msh.etbm.entities.enums.NameComposition;

/**
 * Store information of patient personal information
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="patient")
public class Patient extends WSObject implements Synchronizable {
	private static final long serialVersionUID = 6137777841151141479L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

	@Embedded
	private PersonNameComponent name;
	
	@Column(length=50)
	private String securityNumber;
	
	@Column(length=100)
	private String motherName;
	
        @Temporal(javax.persistence.TemporalType.DATE)
	private Date birthDate;
	
	private Integer recordNumber;

	@NotNull
	private Gender gender;
	
	@Column(length=50)
	private String legacyId;
	
	@OneToMany(mappedBy="patient")
	private List<TbCase> cases = new ArrayList<TbCase>();

	@Column(length=100)
	private String fatherName;
	
	@Embedded
	private SynchronizationData syncData;

	
	public String getFullName() {
		Workspace ws = null;
		if (getWorkspace() != null)
			 ws = getWorkspace();
		else ws = (Workspace)App.getComponent("defaultWorkspace");
		return compoundName(ws);
	}
	
	public String compoundName(Workspace ws) {
		NameComposition comp = ws.getPatientNameComposition();

		return (name != null? name.getDisplayName(comp) : null);
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public Integer getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}

	public String getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}


	/**
	 * @return the cases
	 */
	public List<TbCase> getCases() {
		return cases;
	}

	/**
	 * @param cases the cases to set
	 */
	public void setCases(List<TbCase> cases) {
		this.cases = cases;
	}
	
	
	@Override
	public String toString() {
		return "Patient [" + (name != null? name.toString(): "") + "]";
	}

	/**
	 * @return the name
	 */
	public PersonNameComponent getName() {
		if (name == null)
			name = new PersonNameComponent();
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(PersonNameComponent name) {
		this.name = name;
	}

	/**
	 * @return the fatherName
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * @param fatherName the fatherName to set
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * @return the syncData
	 */
	public SynchronizationData getSyncData() {
		return syncData;
	}

	/**
	 * @param syncData the syncData to set
	 */
	public void setSyncData(SynchronizationData syncData) {
		this.syncData = syncData;
	}
}
