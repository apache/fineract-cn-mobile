package com.mifos.apache.fineract.data.models.deposit;

import java.util.List;

public class ProductInstance {

    private String customerIdentifier;
    private String productIdentifier;
    private String accountIdentifier;
    private List<String> beneficiaries;
    private String state;
    private Double balance;

    public ProductInstance() {
        super();
    }

    public String getCustomerIdentifier() {
        return this.customerIdentifier;
    }

    public void setCustomerIdentifier(final String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getProductIdentifier() {
        return this.productIdentifier;
    }

    public void setProductIdentifier(final String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getAccountIdentifier() {
        return this.accountIdentifier;
    }

    public void setAccountIdentifier(final String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public List<String> getBeneficiaries() {
        return this.beneficiaries;
    }

    public void setBeneficiaries(final List<String> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public String getState() {
        return this.state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(final Double balance) {
        this.balance = balance;
    }
}