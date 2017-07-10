package com.mifos.apache.fineract.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.local.PreferenceKey;
import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.Authentication;
import com.mifos.apache.fineract.ui.DashboardActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.login.LoginActivity;
import com.mifos.apache.fineract.utils.DateUtils;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 *         On 18/06/17.
 */
public class LauncherActivity extends MifosBaseActivity implements LauncherContract.View {

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
                startActivity(DashboardActivity.class);
            }
        } else {
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void checkRefreshAccessToken() {
        Authentication authentication = preferencesHelper.getSignedInUser();
        if (DateUtils.isTokenExpired(authentication.getRefreshTokenExpiration())) {
            startActivity(LoginActivity.class);
        } else {
            //Refresh access token
            preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, true);
            launcherPresenter.refreshAccessToken();
        }
    }

    @Override
    public void startActivity(Class aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
        finish();
    }

    @Override
    public void refreshAccessTokenSuccessfully(Authentication authentication) {
        preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, false);
        preferencesHelper.putAccessToken(authentication.getAccessToken());
        preferencesHelper.putSignInUser(authentication);
        startActivity(DashboardActivity.class);
    }

    @Override
    public void refreshAccessTokenFailed() {
        clearCredentials();
        startActivity(LoginActivity.class);
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
}
