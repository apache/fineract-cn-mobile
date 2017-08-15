package com.mifos.apache.fineract.data.models.deposit;


public class Term {

    private Integer period;
    private TimeUnit timeUnit;
    private InterestPayable interestPayable;

    public Term() {
        super();
    }

    public Integer getPeriod() {
        return this.period;
    }

    public void setPeriod(final Integer period) {
        this.period = period;
    }

    public String getTimeUnit() {
        if (this.timeUnit != null) {
            return this.timeUnit.name();
        } else {
            return null;
        }
    }

    public void setTimeUnit(final String timeUnit) {
        if (timeUnit != null) {
            this.timeUnit = TimeUnit.valueOf(timeUnit);
        }
    }

    public String getInterestPayable() {
        return this.interestPayable.name();
    }

    public void setInterestPayable(final String interestPayable) {
        this.interestPayable = InterestPayable.valueOf(interestPayable);
    }
}