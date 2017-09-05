package org.apache.fineract.ui.online.launcher;

import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.ui.base.MvpView;

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
