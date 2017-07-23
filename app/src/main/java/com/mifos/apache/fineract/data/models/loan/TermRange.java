package com.mifos.apache.fineract.data.models.loan;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */
public class TermRange {

    private String temporalUnit;
    private Double maximum;

    public TermRange(String temporalUnit, Double maximum) {
        this.temporalUnit = temporalUnit;
        this.maximum = maximum;
    }

    public String getTemporalUnit() {
        return temporalUnit;
    }

    public void setTemporalUnit(String temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }
}
