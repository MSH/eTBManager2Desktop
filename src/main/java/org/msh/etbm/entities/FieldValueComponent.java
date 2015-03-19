package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FieldValueComponent {

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FIELDVALUE_ID")
	private FieldValue value;

	@Column(name="Complement", length=100)
	private String complement;


	@Override
	public String toString() {
		return (value != null? value.toString(): null);
	}
	
	/**
	 * Clear the content of the field
	 */
	public void clear() {
		value = null;
		complement = null;
	}
	
	/**
	 * Check if complement is empty
	 * @return true - complement is empty
	 */
	public boolean isComplementEmpty() {
		return ((complement == null) || (complement.isEmpty()));
	}
	
	/**
	 * @return the value
	 */
	public FieldValue getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(FieldValue value) {
		if (value != null)
			complement = null;
		this.value = value;
	}

	/**
	 * @return the complement
	 */
	public String getComplement() {
		return complement;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}

}
