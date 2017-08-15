package com.mifos.apache.fineract.data.models.deposit;

import java.util.List;

public class ProductDefinition {

    private Type type;
    private String identifier;
    private String name;
    private String description;
    private Currency currency;
    private Double minimumBalance;
    private String equityLedgerIdentifier;
    private String cashAccountIdentifier;
    private String expenseAccountIdentifier;
    private String accrueAccountIdentifier;
    private Double interest;
    private Term term;
    private List<Charge> charges;
    private Boolean flexible;
    private Boolean active;

    public ProductDefinition() {
        super();
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(final String type) {
        this.type = Type.valueOf(type);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public Double getMinimumBalance() {
        return this.minimumBalance;
    }

    public void setMinimumBalance(final Double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public String getEquityLedgerIdentifier() {
        return this.equityLedgerIdentifier;
    }

    public void setEquityLedgerIdentifier(final String equityLedgerIdentifier) {
        this.equityLedgerIdentifier = equityLedgerIdentifier;
    }

    public String getCashAccountIdentifier() {
        return this.cashAccountIdentifier;
    }

    public void setCashAccountIdentifier(final String cashAccountIdentifier) {
        this.cashAccountIdentifier = cashAccountIdentifier;
    }

    public String getExpenseAccountIdentifier() {
        return this.expenseAccountIdentifier;
    }

    public void setExpenseAccountIdentifier(final String expenseAccountIdentifier) {
        this.expenseAccountIdentifier = expenseAccountIdentifier;
    }

    public String getAccrueAccountIdentifier() {
        return this.accrueAccountIdentifier;
    }

    public void setAccrueAccountIdentifier(final String accrueAccountIdentifier) {
        this.accrueAccountIdentifier = accrueAccountIdentifier;
    }

    public Double getInterest() {
        return this.interest;
    }

    public void setInterest(final Double interest) {
        this.interest = interest;
    }

    public Term getTerm() {
        return this.term;
    }

    public void setTerm(final Term term) {
        this.term = term;
    }

    public List<Charge> getCharges() {
        return this.charges;
    }

    public void setCharges(final List<Charge> charges) {
        this.charges = charges;
    }

    public Boolean getFlexible() {
        return this.flexible;
    }

    public void setFlexible(final Boolean flexible) {
        this.flexible = flexible;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}