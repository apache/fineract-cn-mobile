package org.apache.fineract.data.models.loan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 09/07/17.
 */

public class LoanAccountPage {

    @SerializedName("elements")
    private List<LoanAccount> loanAccounts;
    private Integer totalPages;
    private Long totalElements;

    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    public void setLoanAccounts(
            List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
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

    @Override
    public String toString() {
        return "LoanAccountPage{" +
                "loanAccounts=" + loanAccounts +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                '}';
    }
}
