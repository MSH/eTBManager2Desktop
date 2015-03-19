package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.msh.etbm.entities.enums.TbField;
import org.msh.utils.FieldLog;

/**
 * Stores data about a field value from TB forms
 * @author Ricardo Lima
 *
 */
@Entity
@Table(name="fieldvalue")
public class FieldValue extends WSObject implements Serializable {
	private static final long serialVersionUID = -754148519681677704L;

	@Id
    private Integer id;

	@Embedded
	@FieldLog(key="form.name")
	private LocalizedNameComp name = new LocalizedNameComp();

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="name1", column=@Column(name="SHORT_NAME1")),
		@AttributeOverride(name="name2", column=@Column(name="SHORT_NAME2"))})
	private LocalizedNameComp shortName = new LocalizedNameComp();

	@Column(length=20)
	private String customId;
	
	private TbField field;
	
	private boolean other;
	
	@Column(length=100)
	private String otherDescription;
	
	private Integer displayOrder;
	

	
	/** {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof FieldValue))
			return false;
		
		return ((FieldValue)obj).getId().equals(getId());
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
	 * @return the shortName
	 */
	public LocalizedNameComp getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(LocalizedNameComp shortName) {
		this.shortName = shortName;
	}

	/**
	 * @param customId the customId to set
	 */
	public void setCustomId(String customId) {
		this.customId = customId;
	}

	/**
	 * @return the customId
	 */
	public String getCustomId() {
		return customId;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(TbField field) {
		this.field = field;
	}

	/**
	 * @return the field
	 */
	public TbField getField() {
		return field;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (getName() != null? name.toString(): super.toString());
	}

	/**
	 * @return the other
	 */
	public boolean isOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(boolean other) {
		this.other = other;
	}

	/**
	 * @return the otherDescription
	 */
	public String getOtherDescription() {
		return otherDescription;
	}

	/**
	 * @param otherDescription the otherDescription to set
	 */
	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}
