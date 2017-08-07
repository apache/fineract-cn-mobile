package com.mifos.apache.fineract.ui.online.customer.customerprofile;

import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public interface CustomerProfileContract {

    interface View extends MvpView {

        void checkCameraPermission();

        void requestPermission();

        void loadCustomerPortrait();
    }
}
