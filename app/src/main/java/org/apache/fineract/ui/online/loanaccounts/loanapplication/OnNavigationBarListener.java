package org.apache.fineract.ui.online.loanaccounts.loanapplication;

import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.PaymentCycle;
import org.apache.fineract.data.models.loan.TermRange;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */

public interface OnNavigationBarListener {

    interface LoanDetailsData {
        void setLoanDetails(LoanAccount.State currentState, String identifier,
                String productIdentifier, Double maximumBalance, PaymentCycle paymentCycle,
                TermRange termRange, String selectedProduct);
    }

    interface ReviewLoan {
        LoanAccount getLoanAccount();
        String getSelectedProduct();
        List<CreditWorthinessSnapshot> getCreditWorthinessSnapshot();
    }

    interface LoanDebtIncomeData {
        void setDebtIncome(CreditWorthinessSnapshot debtIncome);
    }

    interface LoanCoSignerData {
        void setCoSignerDebtIncome(CreditWorthinessSnapshot coSignerDebtIncome);

        void showProgressbar(String message);

        void hideProgressbar();
    }
}
