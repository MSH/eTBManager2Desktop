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

        //Check if positive on microscopy exam
        ExamMicroscopy examMic = (ExamMicroscopy) getLastExam(tbcase, "ExamMicroscopy");
        if (examMic != null && examMic.getResult() != null && examMic.getResult().isPositive())
            value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;

        if (value.equals(CaseDefinition.CLINICALLY_DIAGNOSED)) {
            //Check if positive on culture exam
            ExamCulture examCul = (ExamCulture) getLastExam(tbcase, "ExamCulture");
            if (examCul != null && examCul.getResult() != null && examCul.getResult().isPositive())
                value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;
        }

        if (value.equals(CaseDefinition.CLINICALLY_DIAGNOSED)) {
            //Check if positive on culture exam
            ExamXpert examX = (ExamXpert) getLastExam(tbcase, "ExamXpert");
            if (examX != null && examX.getResult() != null && examX.getResult().equals(XpertResult.TB_DETECTED))
                value = CaseDefinition.BACTERIOLOGICALLY_CONFIRMED;
        }

        if(!value.equals(tbcase.getCaseDefinition())) {
            tbcase.setCaseDefinition(value);
            em.persist(tbcase);
            em.flush();
        }

    }

    /**
     * This method will return the last exam according to the following condition:
     * 1- If the case has treatment registered it will return the last exam before the treatment considering the date collected.
     * 2- If the case doesn't have treatment registered it will return the last exam registered considering the date collected.
     * @param tbcase - The Tbcase that is beeing verificated
     * @param examEntityName - the name of the Exam Entity that will be searched.
     * @return The last exam according to the conditions on the description of that method.
     */
    private LaboratoryExamResult getLastExam(TbCase tbcase, String examEntityName){
        EntityManager em =  App.getEntityManager();

        LaboratoryExamResult result = null;
        List<LaboratoryExamResult> list = null;

        if(tbcase.getTreatmentPeriod()==null || tbcase.getTreatmentPeriod().getIniDate() == null){
            list = (List<LaboratoryExamResult>) em.createQuery(" from " + examEntityName + " e " +
                    " where e.tbcase.id = :caseId and e.result is not null " +
                    " order by e.dateCollected desc, e.id desc ")
                    .setParameter("caseId", tbcase.getId())
                    .getResultList();

            if(list != null && list.size() > 0)
                result = list.get(0);
        }else{
            list = (List<LaboratoryExamResult>) em.createQuery(" from " + examEntityName + " e " +
                    " where e.tbcase.id = :caseId and e.dateCollected <= :iniTreatDate and e.result is not null  " +
                    " order by e.dateCollected desc, e.id desc ")
                    .setParameter("caseId", tbcase.getId())
                    .setParameter("iniTreatDate", tbcase.getTreatmentPeriod().getIniDate())
                    .getResultList();

            if(list != null && list.size() > 0)
                result = list.get(0);
        }

        return result;
    }
}
