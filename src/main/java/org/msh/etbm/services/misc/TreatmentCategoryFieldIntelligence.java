package org.msh.etbm.services.misc;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.entities.*;
import org.msh.etbm.entities.enums.MedicineLine;
import org.msh.etbm.entities.enums.PatientType;
import org.msh.etbm.entities.enums.TreatmentCategory;
import org.msh.etbm.services.cases.CaseServices;
import org.msh.eventbus.EventBusListener;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * Created by Mauricio on 13/10/2015.
 */
public class TreatmentCategoryFieldIntelligence implements EventBusListener {

    /** {@inheritDoc}
     */
    @Override
    public void handleEvent(Object event, Object... data) {
        Object o = data[0];
        TbCase tbcase;

        if(o instanceof  TbCase){
            tbcase = (TbCase) o;
        }else{
            return;
        }

        AppEvent appEvent = (AppEvent) event;

        if(AppEvent.CASE_MODIFIED.equals(appEvent) || AppEvent.NEW_CASE_SAVED.equals(appEvent)){

            EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(tbcase.getId()) {
                @Override
                public void execute(Integer caseId) {
                    if (caseId == null)
                        return;

                    CaseServices caseSrv = App.getComponent(CaseServices.class);
                    TbCase tbcase = caseSrv.findEntity(caseId);
                    tbCaseUpdated(tbcase);
                }

            });

        }else if(AppEvent.TREATMENT_STARTED.equals(appEvent)){

            EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(tbcase.getId()) {
                @Override
                public void execute(Integer caseId) {
                    if (caseId == null)
                        return;

                    CaseServices caseSrv = App.getComponent(CaseServices.class);
                    TbCase tbcase = caseSrv.findEntity(caseId);
                    updateInitialRegimenWithSecondLineDrugs(tbcase);
                }

            });

        }
    }

    /**
     * When tbcase is updated the field treatmentCategory is calculated.
    */
    public void tbCaseUpdated(TbCase tbcase){

        if(tbcase.getInitialRegimenWithSecondLineDrugs() == null)
            return;

        updateTreatmentCategoryField(tbcase);

        App.getEntityManager().persist(tbcase);
        App.getEntityManager().flush();
    }

    /**
     * This method identifies if the initial regimen (the regimen registered when starting the treatment) has second
     * line drugs and stores at a tbcase attribute.
     * @param tbcase
     */
    private void updateInitialRegimenWithSecondLineDrugs(TbCase tbcase){
        if(tbcase == null || tbcase.getPrescribedMedicines() == null || tbcase.getPrescribedMedicines().size() < 1)
            throw new RuntimeException("The started treatment must have at least one prescribed medicine.");

        Boolean hasSecondLineDrugs = false;

        for(PrescribedMedicine pm : tbcase.getPrescribedMedicines()){
            if(MedicineLine.SECOND_LINE.equals(pm.getMedicine().getLine())) {
                hasSecondLineDrugs = true;
                break;
            }
        }

        tbcase.setInitialRegimenWithSecondLineDrugs(hasSecondLineDrugs);

        updateTreatmentCategoryField(tbcase);

        App.getEntityManager().persist(tbcase);
        App.getEntityManager().flush();
    }

    /**
     * This method calculates the treatment category for the case based on the patienttype and the
     * InitialRegimenWithSecondLineDrugs attributes.
     * @param tbcase
     */
    private void updateTreatmentCategoryField(TbCase tbcase){
        TreatmentCategory category;

        if(tbcase.getPatientType() == null)
            return;

        if(tbcase.getInitialRegimenWithSecondLineDrugs().booleanValue())
            category = TreatmentCategory.SECOND_LINE_TREATMENT_REGIMEN;
        else if(PatientType.NEW.equals(tbcase.getPatientType()) || PatientType.UNKNOWN_PREVIOUS_TB_TREAT.equals(tbcase.getPatientType()))
            category = TreatmentCategory.INITIAL_REGIMEN_FIRST_LINE_DRUGS;
        else
            category = TreatmentCategory.RETREATMENT_FIRST_LINE_DRUGS;

        tbcase.setTreatmentCategory(category);
    }
}
