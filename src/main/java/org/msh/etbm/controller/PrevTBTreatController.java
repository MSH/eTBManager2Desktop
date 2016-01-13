package org.msh.etbm.controller;

import javassist.bytecode.CodeAttribute;
import org.hibernate.validator.NotNull;
import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.enums.PrevTBTreatmentOutcome;
import org.msh.etbm.services.SubstanceServices;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 10/01/2016.
 */
@Component
public class PrevTBTreatController {

    private int numPrevTbTreat = 3;
    private TbCase tbcase;
    private List<Substance> substances;
    private List<PrevTBTreatInfo> lstPrevTbTreat;

    public void initialize(){
        substances = SubstanceServices.getSubstancesPrevTreat();
    }

    public List<PrevTBTreatInfo> getLstPrevTbTreat(){
        return lstPrevTbTreat;
    }

    private void updateLstPrevTbTreat(){
        int i = 0;
        lstPrevTbTreat = new ArrayList<PrevTBTreatInfo>();
        while (i<numPrevTbTreat){
            PrevTBTreatInfo info = new PrevTBTreatInfo();
            info.setSubstances(new ArrayList<SubstanceOption>());
            for(Substance s : substances){
                info.getSubstances().add(new SubstanceOption(s, false));
            }
        }
    }

    public int getNumPrevTbTreat() {
        return numPrevTbTreat;
    }

    public void setNumPrevTbTreat(int numPrevTbTreat) {
        this.numPrevTbTreat = numPrevTbTreat;
        updateLstPrevTbTreat();
    }

    public TbCase getTbcase() {
        return tbcase;
    }

    public void setTbcase(TbCase tbcase) {
        this.tbcase = tbcase;
    }

    public class SubstanceOption{
        private Substance substance;
        private boolean selected;

        public SubstanceOption(Substance substance, boolean selected){
            this.substance = substance;
            this.selected = selected;
        }

        public Substance getSubstance() {
            return substance;
        }

        public void setSubstance(Substance substance) {
            this.substance = substance;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public class PrevTBTreatInfo{
        private Integer month;
        private int year;
        private PrevTBTreatmentOutcome outcome;
        private List<SubstanceOption> substances = new ArrayList<SubstanceOption>();

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public PrevTBTreatmentOutcome getOutcome() {
            return outcome;
        }

        public void setOutcome(PrevTBTreatmentOutcome outcome) {
            this.outcome = outcome;
        }

        public List<SubstanceOption> getSubstances() {
            return substances;
        }

        public void setSubstances(List<SubstanceOption> substances) {
            this.substances = substances;
        }
    }
}
