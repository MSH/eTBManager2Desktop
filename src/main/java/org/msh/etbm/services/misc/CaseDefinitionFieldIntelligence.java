package org.msh.etbm.services.misc;


import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.*;
import org.msh.etbm.entities.enums.CaseDefinition;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.entities.enums.XpertResult;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.eventbus.EventBusListener;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.DateUtils;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Mauricio on 11/10/2015.
 */
public class CaseDefinitionFieldIntelligence implements EventBusListener {

    /** {@inheritDoc}
     */
    @Override
    public void handleEvent(Object event, Object... data) {
        Object o = data[0];
        TbCase tbcase;

        if(o instanceof  TbCase){
           tbcase = (TbCase) o;
        }else if(o instanceof LaboratoryExamResult){
            if(!(o instanceof ExamMicroscopy || o instanceof ExamCulture || o instanceof ExamXpert))
                return;
            else
                tbcase = ((LaboratoryExamResult)o).getTbcase();
        }else{
            return;
        }

        EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(tbcase.getId()) {
            @Override
            public void execute(Integer caseId) {
                if(caseId == null)
                    return;

                CaseServices caseSrv = App.getComponent(CaseServices.class);
                TbCase tbcase = caseSrv.findEntity(caseId);
                updateCaseDefinitionField(tbcase);
            }

        });
    }

    /**
     * This method will get the case on caseHome and check its parameter to define if the case definiciton
     * for this field is Bacteriologically Confirmed or Clinically Diagnosed.
     * The details about the logic used are on the comments inside the method.
     */
    private void updateCaseDefinitionField(TbCase tbcase){

        EntityManager em = App.getEntityManager();

        // if there is no case, so just return
        if (tbcase == null) {
            return;
        }

        CaseDefinition value = null;

        //If it is a suspect (presumptive), the case is not a confirmed case, so, no case definition for  this case.
        if (tbcase.getDiagnosisType() == null || tbcase.getDiagnosisType().equals(DiagnosisType.SUSPECT)) {
            tbcase.setCaseDefinition(value);
            em.persist(tbcase);
            em.flush();
            return;
        }

        value = CaseDefinition.CLINICALLY_DIAGNOSED;

        //Check if positive on microscopy exams
        if (isAnyExamPositive(getExams(tbcase, "ExamMicroscopy")))
            value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;

        if (value.equals(CaseDefinition.CLINICALLY_DIAGNOSED)) {
            //Check if positive on culture exams
            if (isAnyExamPositive(getExams(tbcase, "ExamCulture")))
                value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;
        }

        if (value.equals(CaseDefinition.CLINICALLY_DIAGNOSED)) {
            //Check if positive on xpert exams
            if (isAnyExamPositive(getExams(tbcase, "ExamXpert")))
                value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;
        }

        if(!value.equals(tbcase.getCaseDefinition())) {
            tbcase.setCaseDefinition(value);
            em.persist(tbcase);
            em.flush();
        }

    }

    /**
     * This method will return exams according to the following condition:
     * 1- If the case has treatment registered it will return all exams until 8 days before the end start of continuous phase of treatment.
     * 2- If the case doesn't have treatment registered it will return all exams registered.
     * @param tbcase - The Tbcase that is beeing verificated
     * @param examEntityName - the name of the Exam Entity that will be searched.
     * @return The all exams of a type according to the conditions on the description of that method.
     */
    private List<LaboratoryExamResult> getExams(TbCase tbcase, String examEntityName){
        List<LaboratoryExamResult> list = null;

        if(tbcase.getTreatmentPeriod()==null || tbcase.getTreatmentPeriod().getIniDate() == null){
            list = (List<LaboratoryExamResult>) App.getEntityManager().createQuery(" from " + examEntityName + " e " +
                    " where e.tbcase.id = :caseId and e.result is not null " +
                    " order by e.dateCollected desc, e.id desc ")
                    .setParameter("caseId", tbcase.getId())
                    .getResultList();
        }else{
            list = (List<LaboratoryExamResult>) App.getEntityManager().createQuery(" from " + examEntityName + " e " +
                    " where e.tbcase.id = :caseId and e.dateCollected <= :date and e.result is not null  " +
                    " order by e.dateCollected desc, e.id desc ")
                    .setParameter("caseId", tbcase.getId())
                    .setParameter("date", DateUtils.incDays(tbcase.getIntensivePhasePeriod().getEndDate(), -8))
                    .getResultList();
        }

        return list;
    }

    /**
     *
     * @param list list of exams to be checked
     * @return true if at least one exam on list is positive
     */
    private boolean isAnyExamPositive(List<LaboratoryExamResult> list){
        if(list == null)
            return false;

        for(LaboratoryExamResult exam : list){
            if(exam instanceof ExamMicroscopy){
                ExamMicroscopy examMic = (ExamMicroscopy) exam;
                if(examMic.getResult() != null && examMic.getResult().isPositive())
                    return true;
            }else if(exam instanceof ExamCulture){
                ExamCulture examCul = (ExamCulture) exam;
                if(examCul.getResult() != null && examCul.getResult().isPositive())
                    return true;
            }else if(exam instanceof ExamXpert){
                ExamXpert examX = (ExamXpert) exam;
                if(examX.getResult() != null && examX.getResult().equals(XpertResult.TB_DETECTED))
                    return true;
            }
        }

        return false;
    }
}
