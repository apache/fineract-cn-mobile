package com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount;

import com.mifos.apache.fineract.data.models.deposit.ProductInstance;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface DepositOnNavigationBarListener {

    interface ProductInstanceDetails {
        void setProductInstance(ProductInstance identificationDetails, String productName);
    }
}
