package org.apache.fineract.data.models.product;

import org.apache.fineract.data.models.loan.AccountAssignment;
import org.apache.fineract.data.models.loan.TermRange;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 20/07/17.
 */

public class Product {

    private String identifier;
    private String name;
    private TermRange termRange;
    private BalanceRange balanceRange;
    private InterestRange interestRange;
    private InterestBasis interestBasis;
    private String patternPackage;
    private String description;
    private String currencyCode;
    private int minorCurrencyUnitDigits;
    private List<AccountAssignment> accountAssignments = new ArrayList<>();
    private String parameters;
    private String createdOn;
    private String createdBy;
    private String lastModifiedOn;
    private String lastModifiedBy;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TermRange getTermRange() {
        return termRange;
    }

    public void setTermRange(TermRange termRange) {
        this.termRange = termRange;
    }

    public BalanceRange getBalanceRange() {
        return balanceRange;
    }

    public void setBalanceRange(BalanceRange balanceRange) {
        this.balanceRange = balanceRange;
    }

    public InterestRange getInterestRange() {
        return interestRange;
    }

    public void setInterestRange(InterestRange interestRange) {
        this.interestRange = interestRange;
    }

    public InterestBasis getInterestBasis() {
        return interestBasis;
    }

    public void setInterestBasis(InterestBasis interestBasis) {
        this.interestBasis = interestBasis;
    }

    public String getPatternPackage() {
        return patternPackage;
    }

    public void setPatternPackage(String patternPackage) {
        this.patternPackage = patternPackage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getMinorCurrencyUnitDigits() {
        return minorCurrencyUnitDigits;
    }

    public void setMinorCurrencyUnitDigits(int minorCurrencyUnitDigits) {
        this.minorCurrencyUnitDigits = minorCurrencyUnitDigits;
    }

    public List<AccountAssignment> getAccountAssignments() {
        return accountAssignments;
    }

    public void setAccountAssignments(
            List<AccountAssignment> accountAssignments) {
        this.accountAssignments = accountAssignments;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
