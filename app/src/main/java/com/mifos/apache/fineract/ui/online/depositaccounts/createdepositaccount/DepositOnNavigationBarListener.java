package com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount;

import android.support.annotation.Nullable;

import com.mifos.apache.fineract.data.models.deposit.DepositAccount;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface DepositOnNavigationBarListener {

    interface ProductInstanceDetails {
        void setProductInstance(DepositAccount depositAccount, @Nullable String productName);
    }
}
