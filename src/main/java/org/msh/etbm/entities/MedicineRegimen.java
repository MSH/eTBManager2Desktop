package org.msh.etbm.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.msh.etbm.entities.enums.RegimenPhase;

@Entity
@Table(name = "medicineregimen")
public class MedicineRegimen implements Serializable {

    private static final long serialVersionUID = 442884632590945592L;
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "MEDICINE_ID", nullable = false)
    private Medicine medicine;
    private Integer defaultDoseUnit;
    private Integer defaultFrequency;
    private RegimenPhase phase;
    private Integer monthsTreatment;
    @ManyToOne
    @JoinColumn(name = "SOURCE_ID", nullable = false)
    private Source defaultSource;
    @ManyToOne
    @JoinColumn(name = "REGIMEN_ID", nullable = false)
    private Regimen regimen;    

    public Source getDefaultSource() {
        return defaultSource;
    }

    public void setDefaultSource(Source defaultSource) {
        this.defaultSource = defaultSource;
    }

    public RegimenPhase getPhase() {
        return phase;
    }

    public void setPhase(RegimenPhase phase) {
        this.phase = phase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Integer getDefaultDoseUnit() {
        return defaultDoseUnit;
    }

    public void setDefaultDoseUnit(Integer defaultDoseUnit) {
        this.defaultDoseUnit = defaultDoseUnit;
    }

    public Integer getDefaultFrequency() {
        return defaultFrequency;
    }

    public void setDefaultFrequency(Integer defaultFrequency) {
        this.defaultFrequency = defaultFrequency;
    }

    /**
     * @return the monthsTreatment
     */
    public Integer getMonthsTreatment() {
        return monthsTreatment;
    }

    /**
     * @param monthsTreatment the monthsTreatment to set
     */
    public void setMonthsTreatment(Integer monthsTreatment) {
        this.monthsTreatment = monthsTreatment;
    }

    public Regimen getRegimen() {
        return regimen;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }
    
    
}
