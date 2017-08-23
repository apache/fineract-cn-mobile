package com.mifos.apache.fineract.ui.online.loanaccounts.loanapplication;

import com.mifos.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.data.models.loan.TermRange;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */

public interface OnNavigationBarListener {

    interface LoanDetailsData {
        void setLoanDetails(LoanAccount.State currentState, String identifier,
                String productIdentifier, Double maximumBalance, PaymentCycle paymentCycle,
                TermRange termRange);
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
