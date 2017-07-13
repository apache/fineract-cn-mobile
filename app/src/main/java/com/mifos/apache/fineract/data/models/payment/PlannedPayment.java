package com.mifos.apache.fineract.data.models.payment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
public class PlannedPayment {

    private Double interestRate;
    private List<CostComponent> costComponents = new ArrayList<>();
    private Double remainingPrincipal;
    private String date;

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public List<CostComponent> getCostComponents() {
        return costComponents;
    }

    public void setCostComponents(
            List<CostComponent> costComponents) {
        this.costComponents = costComponents;
    }

    public Double getRemainingPrincipal() {
        return remainingPrincipal;
    }

    public void setRemainingPrincipal(Double remainingPrincipal) {
        this.remainingPrincipal = remainingPrincipal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
