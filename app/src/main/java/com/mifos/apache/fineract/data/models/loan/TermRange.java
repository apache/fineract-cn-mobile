package com.mifos.apache.fineract.data.models.loan;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */
public class TermRange {

    private String temporalUnit;
    private Integer maximum;

    public String getTemporalUnit() {
        return temporalUnit;
    }

    public void setTemporalUnit(String temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }
}
