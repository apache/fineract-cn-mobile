package org.apache.fineract.data.models.product;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 20/07/17.
 */

public class ProductPage {

    private List<Product> elements;
    private Integer totalPages;
    private Long totalElements;

    public List<Product> getElements() {
        return elements;
    }

    public void setElements(List<Product> elements) {
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
