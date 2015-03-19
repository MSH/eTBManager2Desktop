package org.msh.etbm.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Table;
import org.msh.utils.FieldLog;


@Entity
@javax.persistence.Table(name="administrativeunit")
@Table(appliesTo="administrativeunit",indexes={@Index(name="idxcode", columnNames={"code"})})
public class AdministrativeUnit extends WSObject {
	private static final long serialVersionUID = 7777075173601864769L;

	@Id
	private Integer id;

	@Embedded
	@FieldLog(key="form.name")
	private LocalizedNameComp name = new LocalizedNameComp();
	
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private AdministrativeUnit parent;
	
	@OneToMany(mappedBy="parent",fetch=FetchType.LAZY)
	@OrderBy(clause="NAME1")
	private List<AdministrativeUnit> units = new ArrayList<AdministrativeUnit>();

	@Column(length=50)
	@FieldLog(key="global.legacyId")
	private String legacyId;
	
	// properties to help dealing with trees
	private int unitsCount;
	
	@Column(length=15, nullable=false)
	@FieldLog(ignore=true)
	private String code;
	
	@ManyToOne
	@JoinColumn(name="COUNTRYSTRUCTURE_ID")
	private CountryStructure countryStructure;
	


	/**
	 * Return the display name of the administrative unit concatenated with its parent units
	 * @return
	 */
	public String getFullDisplayName() {
		String s = getName().toString();

		for (AdministrativeUnit adm: getParentsTreeList(false)) {
			s += ", " + adm.getName().toString();
		}
		
		return s;
	}

	/**
	 * Return the parent list including the own object
	 * @return List of {@link AdministrativeUnit} instance
	 */
	public List<AdministrativeUnit> getParents() {
		return getParentsTreeList(true);
	}

	/**
	 * Return a list with parents administrative unit, where the first is the upper level administrative unit and
	 * the last the lowest level
	 * @return {@link List} of {@link AdministrativeUnit} instances
	 */
	public List<AdministrativeUnit> getParentsTreeList(boolean includeThis) {
		ArrayList<AdministrativeUnit> lst = new ArrayList<AdministrativeUnit>();

		AdministrativeUnit aux;
		if (includeThis)
			 aux = this;
		else aux = getParent();

		while (aux != null) {
			lst.add(0, aux);
			aux = aux.getParent();
		}
		return lst;
	}


	/**
	 * Check if an administrative unit code (passed as the code parameter) is a child of the current administrative unit
	 * @param code of the unit
	 * @return true if code is of a child unit, otherwise return false
	 */
	public boolean isSameOrChildCode(String code) {
		return isSameOrChildCode(this.code, code);
/*		int len = this.code.length();
		if (len > code.length())
			return false;
		return (this.code.equals(code.substring(0, this.code.length())));
*/	}
	
	
	/**
	 * Static method to check if a code is equals of a child of the code given by the parentCode param
	 * @param parentCode
	 * @param code
	 * @return
	 */
	public static boolean isSameOrChildCode(String parentCode, String code) {
		int len = parentCode.length();
		if (len > code.length())
			return false;
		return (parentCode.equals(code.substring(0, parentCode.length())));
	}


	/**
	 * Return the parent administrative unit based on its level. If level is the same of this unit, it returns itself.
	 * If level is bigger than the level of this unit, it returns null
	 * @param level
	 * @return {@link AdministrativeUnit} instance, which is itself or a parent admin unit
	 */
	public AdministrativeUnit getAdminUnitByLevel(int level) {
		if (level == countryStructure.getLevel())
			return this;
		List<AdministrativeUnit> lst = getParents();
		for (AdministrativeUnit adm: lst) {
			if (adm.getLevel()== level)
				return adm;
		}
		return null;
	}

	
	/**
	 * Return parent administrative unit of level 1
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel1() {
		return getAdminUnitByLevel(1);
	}

	
	/**
	 * Return parent administrative unit of level 2
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel2() {
		return getAdminUnitByLevel(2);
	}

	
	/**
	 * Return parent administrative unit of level 3
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel3() {
		return getAdminUnitByLevel(3);
	}

	
	/**
	 * Return parent administrative unit of level 4
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel4() {
		return getAdminUnitByLevel(4);
	}


	/**
	 * Return parent administrative unit of level 5
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel5() {
		return getAdminUnitByLevel(5);
	}

	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public LocalizedNameComp getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(LocalizedNameComp name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public AdministrativeUnit getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AdministrativeUnit parent) {
		this.parent = parent;
	}

	/**
	 * @return the units
	 */
	public List<AdministrativeUnit> getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(List<AdministrativeUnit> units) {
		this.units = units;
	}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyCode) {
		this.legacyId = legacyCode;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName().toString();
	}

	/**
	 * @return the unitsCount
	 */
	public int getUnitsCount() {
		return unitsCount;
	}

	/**
	 * @param unitsCount the unitsCount to set
	 */
	public void setUnitsCount(int unitsCount) {
		this.unitsCount = unitsCount;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof AdministrativeUnit))
			return false;
		
		return ((AdministrativeUnit)obj).getId().equals(getId());
	}

	/**
	 * @return the countryStructure
	 */
	public CountryStructure getCountryStructure() {
		return countryStructure;
	}

	/**
	 * @param countryStructure the countryStructure to set
	 */
	public void setCountryStructure(CountryStructure countryStructure) {
		this.countryStructure = countryStructure;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	public int getLevel() {
		String s = getCode();
		if (s == null)
			return 0;

		return s.length()/3;
	}
}
