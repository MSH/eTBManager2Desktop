package org.msh.etbm.services.misc;

import org.msh.etbm.custom.bd.entities.TbCaseBD;
import org.msh.etbm.custom.bd.entities.enums.SmearStatus;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.*;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.entities.enums.InfectionSite;
import org.msh.etbm.entities.enums.MicroscopyResult;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.etbm.services.login.UserSession;
import org.msh.eventbus.EventBusListener;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.DateUtils;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Mauricio on 29/05/2015.
 */
public class FollowUpSmearStatusFieldIntelligence implements EventBusListener {

    /** {@inheritDoc}
     */
    @Override
    public void handleEvent(Object event, Object... data) {
        Object o = data[0];
        TbCase tbcase;

        if(o instanceof  TbCase){
            tbcase = (TbCase) o;
        }else if(o instanceof ExamMicroscopy){
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
                updateFollowUpSmearStatusField(tbcase);
            }

        });
    }

    /**
     * This method will evaluate if the case is smear positive, negative or not evaluated, based on the follow up microscopy exam.
     * @param tbcase
     */
    private void updateFollowUpSmearStatusField(TbCase c){
        Workspace ws = UserSession.getWorkspace();
        EntityManager entityManager = App.getEntityManager();

        if(!"bd".equals(ws.getExtension()))
            return;

        TbCaseBD tbcase = (TbCaseBD)c;

        SmearStatus value = null;

        //Only evaluate Confirmed TB Pulmonary Cases with treatment registered.
        if ( (!DiagnosisType.CONFIRMED.equals(tbcase.getDiagnosisType())) || (!CaseClassification.TB.equals(tbcase.getClassification()))
                || (!InfectionSite.PULMONARY.equals(tbcase.getInfectionSite())) ){
            tbcase.setFollowUpSmearStatus(value);
            entityManager.persist(tbcase);
            entityManager.flush();
            return;
        }

        ExamMicroscopy followUpExam = getFollowUpExam(tbcase);

        if(followUpExam == null || followUpExam.getResult() == null || MicroscopyResult.NOTDONE.equals(followUpExam.getResult())
                || MicroscopyResult.PENDING.equals(followUpExam.getResult())){
            value = SmearStatus.NOT_EVALUATED;
        }else if(followUpExam.getResult().isPositive()){
            value = SmearStatus.SMEAR_POSITIVE;
        }else if(followUpExam.getResult().isNegative()){
            value = SmearStatus.SMEAR_NEGATIVE;
        }

        if(value == null)
            throw new RuntimeException("Value must not be null.");

        tbcase.setFollowUpSmearStatus(value);
        entityManager.persist(tbcase);
        entityManager.flush();

    }

    /**
     * The rule: The exam microscopy that must be considered is the first exam after the final date of intensive phase
     * until seven days after that date.
     * @param tbcase
     * @return the exam microscopy that has to be considered to evaluate the Smear Status
     */
    private ExamMicroscopy getFollowUpExam(TbCase tbcase){
        List<Object> result;
        EntityManager entityManager = App.getEntityManager();

        if(tbcase.getTreatmentPeriod() == null || tbcase.getIniContinuousPhase() == null)
            return null;

        result = entityManager.createQuery(" from ExamMicroscopy e " +
                                            "where e.tbcase.id = :caseId " +
                                            "and e.dateCollected >= :iniDateLimit " +
                                            "and e.dateCollected <= :finalDateLimit " +
                                            "order by e.dateCollected, e.id ")
                .setParameter("caseId", tbcase.getId())
                .setParameter("iniDateLimit", DateUtils.incDays(tbcase.getIniContinuousPhase(), -8))
                .setParameter("finalDateLimit", DateUtils.incDays(tbcase.getIniContinuousPhase(), 7))
                .getResultList();

        if(result != null && result.size()>0)
            return (ExamMicroscopy) result.get(0);

        return null;
    }

}
