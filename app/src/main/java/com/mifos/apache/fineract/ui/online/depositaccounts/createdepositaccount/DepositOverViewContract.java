package com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount;

import com.mifos.apache.fineract.data.models.deposit.ProductInstance;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface DepositOverViewContract {

    void setProductInstance(ProductInstance productInstance, String productName);
}
