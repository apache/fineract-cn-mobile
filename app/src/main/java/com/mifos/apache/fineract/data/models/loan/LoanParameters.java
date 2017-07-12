package com.mifos.apache.fineract.data.models.loan;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class LoanParameters {

    private String customerIdentifier;
    private List<CreditWorthinessSnapshot> creditWorthinessSnapshots;
    private Double maximumBalance;
    private TermRange termRange;
    private PaymentCycle paymentCycle;

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public List<CreditWorthinessSnapshot> getCreditWorthinessSnapshots() {
        return creditWorthinessSnapshots;
    }

    public void setCreditWorthinessSnapshots(
            List<CreditWorthinessSnapshot> creditWorthinessSnapshots) {
        this.creditWorthinessSnapshots = creditWorthinessSnapshots;
    }

    public Double getMaximumBalance() {
        return maximumBalance;
    }

    public void setMaximumBalance(Double maximumBalance) {
        this.maximumBalance = maximumBalance;
    }

    public TermRange getTermRange() {
        return termRange;
    }

    public void setTermRange(TermRange termRange) {
        this.termRange = termRange;
    }

    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }
}
