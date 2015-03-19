package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Component of a medicine, 
 *  example: Isoniazid is a common component of medicines
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="medicinecomponent")
public class MedicineComponent implements Serializable{
	private static final long serialVersionUID = -195735659908845780L;

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name="SUBSTANCE_ID")
	private Substance substance;

	private Integer strength;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="MEDICINE_ID")
	private Medicine medicine;


	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Substance getSubstance() {
		return substance;
	}

	public void setSubstance(Substance substance) {
		this.substance = substance;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}



	/** {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ((substance != null) && (medicine != null)? substance.getAbbrevName() + " " + strength + medicine.getStrengthUnit(): super.toString());
	}


	/** {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof MedicineComponent)
			return false;
		MedicineComponent other = (MedicineComponent) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
		
}
