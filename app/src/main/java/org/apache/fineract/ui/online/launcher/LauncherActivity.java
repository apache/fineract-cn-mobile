package org.apache.fineract.ui.online.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.mifos.mobile.passcode.utils.PassCodeConstants;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferenceKey;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.online.PassCodeActivity;
import org.apache.fineract.ui.online.login.LoginActivity;
import org.apache.fineract.utils.DateUtils;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 *         On 18/06/17.
 */
public class LauncherActivity extends FineractBaseActivity implements LauncherContract.View {

    public static final String LOG_TAG = LauncherActivity.class.getSimpleName();

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    LauncherPresenter launcherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getActivityComponent().inject(this);
        launcherPresenter.attachView(this);
        checkAccessTokenExpired();
    }

    @Override
    public void checkAccessTokenExpired() {
        if (preferencesHelper.getAccessToken() != null) {
            Authentication authentication = preferencesHelper.getSignedInUser();

            //Check Access token expired or not.
            if (DateUtils.isTokenExpired(authentication.getAccessTokenExpiration())) {
                checkRefreshAccessToken();
            } else {
                startPasscodeActivity();
            }
        } else {
            startLoginActivity();
        }
        finish();
    }

    @Override
    public void checkRefreshAccessToken() {
        Authentication authentication = preferencesHelper.getSignedInUser();
        if (DateUtils.isTokenExpired(authentication.getRefreshTokenExpiration())) {
            startLoginActivity();
        } else {
            //Refresh access token
            preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, true);
            launcherPresenter.refreshAccessToken();
        }
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startPasscodeActivity() {
        Intent intent = new Intent(this, PassCodeActivity.class);
        intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
        startActivity(intent);
    }

    @Override
    public void refreshAccessTokenSuccessfully(Authentication authentication) {
        preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, false);
        preferencesHelper.putAccessToken(authentication.getAccessToken());
        preferencesHelper.putSignInUser(authentication);
        startPasscodeActivity();
    }

    @Override
    public void refreshAccessTokenFailed() {
        clearCredentials();
        startLoginActivity();
    }

    @Override
    public void clearCredentials() {
        preferencesHelper.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        launcherPresenter.detachView();
    }

    @Override
    public void showNoInternetConnection() {

    }

    @Override
    public void showError(String message) {

    }
}
