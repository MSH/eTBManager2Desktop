package org.msh.etbm.custom.bd.entities.enums;

public enum PulmonaryTypesBD {
     POSITIVE,
     NEGATIVE;

     public String getKey() {
            return getClass().getSimpleName().concat("." + name());
        }
}
