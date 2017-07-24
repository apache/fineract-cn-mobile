package com.mifos.apache.fineract.ui.loanapplication;

import com.mifos.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.data.models.loan.TermRange;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */

public interface OnNavigationBarListener {

    interface LoanDetailsData {
        void setLoanDetails(String currentState, String identifier, String productIdentifier,
                Double maximumBalance, PaymentCycle paymentCycle, TermRange termRange);
    }

    interface LoanDebtIncomeData {
        void setDebtIncome(CreditWorthinessSnapshot debtIncome);
    }

    interface LoanCoSignerData {
        void setCoSignerDebtIncome(CreditWorthinessSnapshot coSignerDebtIncome);
        void showProgressbar();
        void hideProgressbar();
    }
}
