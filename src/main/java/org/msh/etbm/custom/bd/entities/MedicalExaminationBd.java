package org.msh.etbm.custom.bd.entities;

import org.msh.etbm.custom.bd.entities.enums.DotProvider;
import org.msh.etbm.entities.FieldValue;
import org.msh.etbm.entities.MedicalExamination;

import javax.persistence.*;

/**
 *
 * Records information about a medical examination of a case in bangladesh workspace
 *
 * @author Utkarsh Srivastava
 *
 */
@Entity
@Table(name="medicalexaminationbd")
@DiscriminatorValue("bd")
public class MedicalExaminationBd extends MedicalExamination {
	private static final long serialVersionUID = 2760727118134685773L;

	private DotProvider dotType;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="DOTTYPE_ID")
    private FieldValue dotTypeFv;

	public DotProvider getDotType() {
		return dotType;
	}

	public void setDotType(DotProvider dotType) {
		this.dotType = dotType;
	}

    public FieldValue getDotTypeFv() { return dotTypeFv; }

    public void setDotTypeFv(FieldValue dotTypeFv) { this.dotTypeFv = dotTypeFv; }

}
