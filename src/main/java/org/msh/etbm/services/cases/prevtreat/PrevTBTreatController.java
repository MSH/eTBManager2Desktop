package org.msh.etbm.services.cases.prevtreat;

import com.toedter.calendar.DateUtil;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.PrevTBTreatment;
import org.msh.etbm.entities.Substance;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.SubstanceServices;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Month;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 10/01/2016.
 */
@Component
@Scope("prototype")
public class PrevTBTreatController {

    private int numPrevTbTreat;
    private TbCase tbcase;
    private List<Substance> substances;
    private List<PrevTBTreatmentData> listPrevTbTreat;

    public PrevTBTreatController(TbCase c){
        this.tbcase = c;
        loadList();
    }

    public PrevTBTreatController(){

    }

    private void updateLstPrevTbTreat(){
        int rowCount = (listPrevTbTreat != null ? listPrevTbTreat.size() : 0);

        if(rowCount > numPrevTbTreat){
            //need to decrease quantity of rows.
            if(listPrevTbTreat == null)
                return;

            while(listPrevTbTreat.size() > numPrevTbTreat){
                listPrevTbTreat.remove(listPrevTbTreat.size()-1);
            }

        }else if(numPrevTbTreat > rowCount){
            //need to decrease quantity of rows.
            if(listPrevTbTreat == null)
                listPrevTbTreat = new ArrayList<PrevTBTreatmentData>();

            while(numPrevTbTreat > listPrevTbTreat.size()){

                PrevTBTreatmentData info = new PrevTBTreatmentData();
                info.setSubstances(new ArrayList<SubstanceOption>());
                for(Substance s : getSubstances()){
                    info.getSubstances().add(new SubstanceOption(s, false));
                }

                listPrevTbTreat.add(info);
            }
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
        loadList();
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

    public void save(){
        EntityManager entityManager = App.getEntityManager();

        //remove existing prev tb treat
        entityManager.createQuery("delete from PrevTBTreatment where tbcase.id = :caseId")
                .setParameter("caseId", tbcase.getId())
                .executeUpdate();

        if(listPrevTbTreat == null)
            return;

        for(PrevTBTreatmentData p : listPrevTbTreat){
            PrevTBTreatment p2 = new PrevTBTreatment();
            p2.setSubstances(p.getSelectedSubstances());
            p2.setTbcase(tbcase);
            p2.setMonth(p.getMonth().getRecordNumber());
            p2.setYear(p.getYear());
            p2.setOutcome(p.getOutcome());
            p2.setOutcomeMonth(p.getOutcomeMonth() != null ? p.getOutcomeMonth().getRecordNumber() : null);
            p2.setOutcomeYear((p.getOutcomeYear() == 0 ? null : p.getOutcomeYear()));

            entityManager.persist(p2);
        }
    }

    private void loadList(){
        if(tbcase == null)
            return;

        EntityManager entityManager = App.getEntityManager();

        List<PrevTBTreatment> list = entityManager.createQuery("from PrevTBTreatment where tbcase.id = :caseId")
                .setParameter("caseId", tbcase.getId())
                .getResultList();

        for(PrevTBTreatment p : list){
            PrevTBTreatmentData info = new PrevTBTreatmentData();
            info.setOutcome(p.getOutcome());
            info.setYear(p.getYear());
            info.setMonth(Month.getByRecordNumber(p.getMonth()));
            info.setOutcomeYear(p.getOutcomeYear());
            info.setOutcomeMonth(p.getOutcomeMonth() == null ? null : Month.getByRecordNumber(p.getOutcomeMonth()));
            info.setSubstances(new ArrayList<SubstanceOption>());
            for(Substance s : getSubstances()){
                boolean isSelected = false;
                for(Substance selSubst : p.getSubstances()){
                    if(selSubst.getId().equals(s.getId())){
                        isSelected = true;
                        break;
                    }
                }
                info.getSubstances().add(new SubstanceOption(s, isSelected));
            }

            if(listPrevTbTreat == null)
                listPrevTbTreat = new ArrayList<PrevTBTreatmentData>();

            listPrevTbTreat.add(info);
        }

        if(listPrevTbTreat != null)
            this.numPrevTbTreat = listPrevTbTreat.size();
    }

    public boolean isAllYearsValid(){
        if(listPrevTbTreat==null)
            return true;

        int currentYear = DateUtils.yearOf(DateUtils.getDate());

        for(PrevTBTreatmentData p : listPrevTbTreat){
            if(p.getYear() > currentYear || p.getYear() < 1900)
                return false;

            if(p.getOutcomeYear() != null && p.getOutcomeYear() != 0 && (p.getOutcomeYear() > currentYear || p.getOutcomeYear() < 1900))
                return false;
        }

        return true;
    }
}
