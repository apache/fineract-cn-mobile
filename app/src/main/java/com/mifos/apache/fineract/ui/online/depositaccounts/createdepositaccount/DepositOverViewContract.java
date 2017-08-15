package com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount;

import com.mifos.apache.fineract.data.models.deposit.DepositAccount;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface DepositOverViewContract {

    void setProductInstance(DepositAccount depositAccount, String productName,
            DepositAction depositAction);
}
