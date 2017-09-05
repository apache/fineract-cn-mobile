package org.apache.fineract.data.models.deposit;

public class Charge {

    private String actionIdentifier;
    private String incomeAccountIdentifier;
    private String name;
    private String description;
    private Boolean proportional;
    private Double amount;

    public Charge() {
        super();
    }

    public String getActionIdentifier() {
        return this.actionIdentifier;
    }

    public void setActionIdentifier(final String actionIdentifier) {
        this.actionIdentifier = actionIdentifier;
    }

    public String getIncomeAccountIdentifier() {
        return this.incomeAccountIdentifier;
    }

    public void setIncomeAccountIdentifier(final String incomeAccountIdentifier) {
        this.incomeAccountIdentifier = incomeAccountIdentifier;
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

    public Boolean getProportional() {
        return this.proportional;
    }

    public void setProportional(final Boolean proportional) {
        this.proportional = proportional;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Charge charge = (Charge) o;

        return name != null ? name.equals(charge.name) : charge.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}