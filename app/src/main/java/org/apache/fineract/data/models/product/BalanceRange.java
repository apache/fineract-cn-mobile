package org.apache.fineract.data.models.product;

/**
 * @author Rajan Maurya
 *         On 20/07/17.
 */

public class BalanceRange {

    private Double minimum;
    private Double maximum;

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }
}
