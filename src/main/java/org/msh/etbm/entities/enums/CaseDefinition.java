package org.msh.etbm.entities.enums;

/**
 * Created by rmemoria on 12/5/15.
 */
public enum CaseDefinition {
    BACTERIOLOGICALLY_CONFIRMED,
    CLINICALLY_DIAGNOSED
    ;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
