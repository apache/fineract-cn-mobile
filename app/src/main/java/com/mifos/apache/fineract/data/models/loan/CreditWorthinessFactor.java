package com.mifos.apache.fineract.data.models.loan;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class CreditWorthinessFactor {

    private String description;
    private Double amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
