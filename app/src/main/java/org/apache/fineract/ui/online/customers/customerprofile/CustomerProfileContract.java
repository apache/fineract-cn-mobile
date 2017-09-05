package org.apache.fineract.ui.online.customers.customerprofile;

import org.apache.fineract.ui.base.MvpView;

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
