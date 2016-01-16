package org.msh.etbm.services.cases.prevtreat;

import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.SubstanceServices;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 10/01/2016.
 */
@Component
@Scope("prototype")
public class PrevTBTreatController {

    private int numPrevTbTreat = 1;
    private TbCase tbcase;
    private List<Substance> substances;
    private List<PrevTBTreatmentData> listPrevTbTreat;
    /**
     * As the xml interface API doesn't have a "for each" tag the quantity of substances on prev tb table is hard coded.
     */
    private final static int NUM_PREVTB_SUSBTANCES = 21;

    private void updateLstPrevTbTreat(){
        //TODO IMPROVE THIS ACCORDING TO WEB RULES.

        int i = 1;
        listPrevTbTreat = new ArrayList<PrevTBTreatmentData>();
        while (i<=numPrevTbTreat){
            PrevTBTreatmentData info = new PrevTBTreatmentData();
            info.setSubstances(new ArrayList<SubstanceOption>());
            for(Substance s : getSubstances()){
                info.getSubstances().add(new SubstanceOption(s, false));
            }
            listPrevTbTreat.add(info);
            i = i+1;
        }
    }

    public int getNumPrevTbTreat() {
        return numPrevTbTreat;
    }

    public void setNumPrevTbTreat(int numPrevTbTreat) {
        this.numPrevTbTreat = numPrevTbTreat;
    }

    public TbCase getTbcase() {
        return tbcase;
    }

    public void setTbcase(TbCase tbcase) {
        this.tbcase = tbcase;
    }

    public List<Substance> getSubstances() {
        if(substances == null || substances.size() == 0)
            substances = SubstanceServices.getSubstancesPrevTreat();
        return substances;
    }

    public void setSubstances(List<Substance> substances) {
        this.substances = substances;
    }

    public List<PrevTBTreatmentData> getListPrevTbTreat() {
        if(listPrevTbTreat == null || listPrevTbTreat.size() != this.numPrevTbTreat)
            updateLstPrevTbTreat();
        return listPrevTbTreat;
    }

    public void setListPrevTbTreat(List<PrevTBTreatmentData> listPrevTbTreat) {
        this.listPrevTbTreat = listPrevTbTreat;
    }
}
