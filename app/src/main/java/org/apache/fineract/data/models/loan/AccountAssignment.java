package org.apache.fineract.data.models.loan;

/**
 * @author Rajan Maurya
 *         On 09/07/17.
 */
public class AccountAssignment {

    private String designator;
    private String accountIdentifier;
    private String ledgerIdentifier;

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public String getLedgerIdentifier() {
        return ledgerIdentifier;
    }

    public void setLedgerIdentifier(String ledgerIdentifier) {
        this.ledgerIdentifier = ledgerIdentifier;
    }

    @Override
    public String toString() {
        return "AccountAssignment{" +
                "designator='" + designator + '\'' +
                ", accountIdentifier='" + accountIdentifier + '\'' +
                ", ledgerIdentifier='" + ledgerIdentifier + '\'' +
                '}';
    }
}
