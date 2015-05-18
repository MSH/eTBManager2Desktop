package org.msh.etbm.entities.enums;

/**
 * Created by rmemoria on 18/5/15.
 */
public enum SecDrugsReceived {
    YES ("global.yes"),
    NO ("global.no"),
    UNKNOWN ("manag.ind.interim.unknown");

    private final String messageKey;

    SecDrugsReceived(String msg) {
        messageKey = msg;
    }

    public String getKey() {
        return messageKey;
    }
}
