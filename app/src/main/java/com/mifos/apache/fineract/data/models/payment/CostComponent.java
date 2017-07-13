package com.mifos.apache.fineract.data.models.payment;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */

public class CostComponent {

    private String chargeIdentifier;
    private Double amount;

    public String getChargeIdentifier() {
        return chargeIdentifier;
    }

    public void setChargeIdentifier(String chargeIdentifier) {
        this.chargeIdentifier = chargeIdentifier;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
