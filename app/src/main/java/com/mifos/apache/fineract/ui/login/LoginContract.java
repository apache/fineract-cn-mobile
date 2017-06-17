package com.mifos.apache.fineract.ui.login;

import com.mifos.apache.fineract.data.models.User;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */

public interface LoginContract {

    interface View extends MvpView {

        void showUserLoginSuccessfully(User user);

        void showError(String errorMessage);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter {

        void login(String username, String password);
    }
}
