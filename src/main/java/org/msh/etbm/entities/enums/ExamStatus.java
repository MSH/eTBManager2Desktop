package org.msh.etbm.entities.enums;

/**
 * Created by rmemoria on 12/5/15.
 */
public enum ExamStatus {
    REQUESTED,
    ONGOING,
    PERFORMED;

    /**
     * Return the key string in the list of the messages to display the correct status in the selected language
     * @return
     */
    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
