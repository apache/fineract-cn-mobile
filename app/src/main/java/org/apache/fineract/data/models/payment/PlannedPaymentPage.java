package org.apache.fineract.data.models.payment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
public class PlannedPaymentPage {

    private List<ChargeName> chargeNames = new ArrayList<>();
    private List<PlannedPayment> elements = new ArrayList<>();
    private Integer totalPages;
    private Long totalElements;

    public List<ChargeName> getChargeNames() {
        return chargeNames;
    }

    public void setChargeNames(
            List<ChargeName> chargeNames) {
        this.chargeNames = chargeNames;
    }

    public List<PlannedPayment> getElements() {
        return elements;
    }

    public void setElements(
            List<PlannedPayment> elements) {
        this.elements = elements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
