package com.mifos.apache.fineract.ui.online.loans.loanapplication;

import com.mifos.apache.fineract.data.models.loan.CreditWorthinessFactor;

/**
 * @author Rajan Maurya
 *         On 23/07/17.
 */
public interface OnBottomSheetDialogListener {

    interface AddDebt {
        void addDebt(CreditWorthinessFactor creditWorthinessFactor);
        void editDebt(CreditWorthinessFactor creditWorthinessFactor, int position);
    }

    interface AddIncome {
        void addIncome(CreditWorthinessFactor creditWorthinessFactor);
        void editIncome(CreditWorthinessFactor creditWorthinessFactor, int position);
    }
}
