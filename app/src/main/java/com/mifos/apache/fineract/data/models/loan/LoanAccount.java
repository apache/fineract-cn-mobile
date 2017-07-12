package com.mifos.apache.fineract.data.models.loan;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 09/07/17.
 */
public class LoanAccount {

    private String identifier;
    private String productIdentifier;
    private String parameters;
    private List<AccountAssignment> accountAssignments = new ArrayList<>();
    private State currentState;
    private String createdOn;
    private String createdBy;
    private String lastModifiedOn;
    private String lastModifiedBy;
    private LoanParameters loanParameters;

    public enum State {

        @SerializedName("CREATED")
        CREATED,

        @SerializedName("PENDING")
        PENDING,

        @SerializedName("APPROVED")
        APPROVED,

        @SerializedName("ACTIVE")
        ACTIVE,

        @SerializedName("CLOSED")
        CLOSED
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public List<AccountAssignment> getAccountAssignments() {
        return accountAssignments;
    }

    public void setAccountAssignments(
            List<AccountAssignment> accountAssignments) {
        this.accountAssignments = accountAssignments;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
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

    public LoanParameters getLoanParameters() {
        return new Gson().fromJson(parameters, LoanParameters.class);
    }

    /*public void setLoanParameters() {
        this.loanParameters = gson.fromJson(parameters, LoanParameters.class);;
    }*/
}
