package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.msh.utils.FieldLog;




@Entity
@Table(name="countrystructure")
public class CountryStructure extends WSObject implements Serializable{
	private static final long serialVersionUID = -9182467866935116572L;

	@Id
        private Integer id;
	
	@Embedded
	@FieldLog(key="form.name")
	private LocalizedNameComp name = new LocalizedNameComp();
	
	@Column(name="STRUCTURE_LEVEL")
	private int level;
	


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
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (name != null? name.toString() : super.toString());
	}
}
