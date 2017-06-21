package com.mifos.apache.fineract.data.models.customer;

import java.util.List;

public class CustomerPage {

    private List<Customer> customers;
    private Integer totalPages;
    private Long totalElements;

    public CustomerPage() {
        super();
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(final List<Customer> customers) {
        this.customers = customers;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(final Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(final Long totalElements) {
        this.totalElements = totalElements;
    }
}