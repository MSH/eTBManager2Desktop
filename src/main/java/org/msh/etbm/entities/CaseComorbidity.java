package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "casecomorbidity")
public class CaseComorbidity extends SynchronizableEntity implements Serializable {
	private static final long serialVersionUID = -2790164290027049637L;

    
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="CASE_ID",nullable=false)
	private TbCase tbcase;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMORBIDITY_ID", nullable=false)
	FieldValue comorbidity;
	
	@Column(length=100)
	private String duration;
	
	@Column(length=200)
	private String comment;


    /** {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CaseComorbidity)) {
            return false;
        }
        
        if (((CaseComorbidity)other).getId() == null)
        	return false;

        return ((CaseComorbidity) other).getId().equals(getId());
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	/**
	 * @return the comorbidity
	 */
	public FieldValue getComorbidity() {
		return comorbidity;
	}

	/**
	 * @param comorbidity the comorbidity to set
	 */
	public void setComorbidity(FieldValue comorbidity) {
		this.comorbidity = comorbidity;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	} 
}
