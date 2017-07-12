package com.mifos.apache.fineract.data.models.loan;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class CreditWorthinessSnapshot {

    private String forCustomer;
    private List<CreditWorthinessFactor> incomeSources;
    private List<CreditWorthinessFactor> assets;
    private List<CreditWorthinessFactor> debts;

    public String getForCustomer() {
        return forCustomer;
    }

    public void setForCustomer(String forCustomer) {
        this.forCustomer = forCustomer;
    }

    public List<CreditWorthinessFactor> getIncomeSources() {
        return incomeSources;
    }

    public void setIncomeSources(
            List<CreditWorthinessFactor> incomeSources) {
        this.incomeSources = incomeSources;
    }

    public List<CreditWorthinessFactor> getAssets() {
        return assets;
    }

    public void setAssets(
            List<CreditWorthinessFactor> assets) {
        this.assets = assets;
    }

    public List<CreditWorthinessFactor> getDebts() {
        return debts;
    }

    public void setDebts(List<CreditWorthinessFactor> debts) {
        this.debts = debts;
    }
}
