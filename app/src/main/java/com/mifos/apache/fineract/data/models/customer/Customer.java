package com.mifos.apache.fineract.data.models.customer;

import java.util.List;

public final class Customer {

    public enum Type {
        PERSON,
        BUSINESS
    }

    public enum State {
        PENDING,
        ACTIVE,
        LOCKED,
        CLOSED
    }

    private String identifier;
    private Type type;
    private String givenName;
    private String middleName;
    private String surname;
    private DateOfBirth dateOfBirth;
    private Boolean member;
    private String accountBeneficiary;
    private String referenceCustomer;
    private String assignedOffice;
    private String assignedEmployee;
    private Address address;
    private List<ContactDetail> contactDetails;
    private State currentState;
    private String createdBy;
    private String createdOn;
    private String lastModifiedBy;
    private String lastModifiedOn;

    public Customer() {
        super();
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(final String type) {
        this.type = Type.valueOf(type);
    }

    public String getGivenName() {
        return this.givenName;
    }

    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public DateOfBirth getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(final DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getMember() {
        return this.member;
    }

    public void setMember(final Boolean member) {
        this.member = member;
    }

    public String getAccountBeneficiary() {
        return this.accountBeneficiary;
    }

    public void setAccountBeneficiary(final String accountBeneficiary) {
        this.accountBeneficiary = accountBeneficiary;
    }

    public String getReferenceCustomer() {
        return this.referenceCustomer;
    }

    public void setReferenceCustomer(final String referenceCustomer) {
        this.referenceCustomer = referenceCustomer;
    }

    public String getAssignedOffice() {
        return this.assignedOffice;
    }

    public void setAssignedOffice(final String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public String getAssignedEmployee() {
        return this.assignedEmployee;
    }

    public void setAssignedEmployee(final String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public List<ContactDetail> getContactDetails() {
        return this.contactDetails;
    }

    public void setContactDetails(final List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getCurrentState() {
        return this.currentState != null ? this.currentState.name() : null;
    }

    public void setCurrentState(final String currentState) {
        this.currentState = State.valueOf(currentState);
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(final String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedOn() {
        return this.lastModifiedOn;
    }

    public void setLastModifiedOn(final String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }
}