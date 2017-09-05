package org.apache.fineract.data.models.loan;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class PaymentCycle {

    private String temporalUnit;
    private Integer period;
    private Integer alignmentDay;
    private Integer alignmentWeek;
    private Integer alignmentMonth;

    public String getTemporalUnit() {
        return temporalUnit;
    }

    public void setTemporalUnit(String temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getAlignmentDay() {
        return alignmentDay;
    }

    public void setAlignmentDay(Integer alignmentDay) {
        this.alignmentDay = alignmentDay;
    }

    public Integer getAlignmentWeek() {
        return alignmentWeek;
    }

    public void setAlignmentWeek(Integer alignmentWeek) {
        this.alignmentWeek = alignmentWeek;
    }

    public Integer getAlignmentMonth() {
        return alignmentMonth;
    }

    public void setAlignmentMonth(Integer alignmentMonth) {
        this.alignmentMonth = alignmentMonth;
    }
}
