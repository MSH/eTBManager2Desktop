package org.msh.etbm.entities.enums;

/**
 * Created by rmemoria on 29/6/15.
 */
public enum TreatmentCategory {
    INITIAL_REGIMEN_FIRST_LINE_DRUGS,
    RETREATMENT_FIRST_LINE_DRUGS,
    SECOND_LINE_TREATMENT_REGIMEN
    ;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
