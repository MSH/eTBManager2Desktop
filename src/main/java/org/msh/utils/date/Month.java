package org.msh.utils.date;

/**
 * Created by Mauricio on 27/01/2016.
 */
public enum Month {
    JANUARY(0),
    FEBRUARY(1),
    MARCH(2),
    APRIL(3),
    MAY(4),
    JUNE(5),
    JULY(6),
    AUGUST(7),
    SEPTEMBER(8),
    OCTOBER(9),
    NOVEMBER(10),
    DECEMBER(11);

    int recordNumber;

    Month(int recordNumber){
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    public static Month getByRecordNumber(int recordNumber){
        switch (recordNumber){
            case 0: return JANUARY;
            case 1: return FEBRUARY;
            case 2: return MARCH;
            case 3: return APRIL;
            case 4: return MAY;
            case 5: return JUNE;
            case 6: return JULY;
            case 7: return AUGUST;
            case 8: return SEPTEMBER;
            case 9: return OCTOBER;
            case 10: return NOVEMBER;
            case 11: return DECEMBER;
        }

        return null;
    }

}
