package com.mifos.apache.fineract.ui.online.launcher;

import com.mifos.apache.fineract.data.models.Authentication;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 06/07/17.
 */
public interface LauncherContract {

    interface View extends MvpView {

        void checkAccessTokenExpired();

        void checkRefreshAccessToken();

        void startActivity(Class aClass);

        void refreshAccessTokenSuccessfully(Authentication authentication);

        void refreshAccessTokenFailed();

        void clearCredentials();
    }

    interface Presenter {

        void refreshAccessToken();
    }
}
