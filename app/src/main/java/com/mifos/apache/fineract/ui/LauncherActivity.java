package com.mifos.apache.fineract.ui;

import android.content.Intent;
import android.os.Bundle;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.login.LoginActivity;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 *         On 18/06/17.
 */
public class LauncherActivity extends MifosBaseActivity {

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        Intent intent;
        if (preferencesHelper.getAccessToken() != null) {
            intent = new Intent(this, DashboardActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        finish();
    }
}
