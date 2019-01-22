package org.apache.fineract.ui.online.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.DashboardActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */
//TODO Write documentation
public class LoginActivity extends FineractBaseActivity implements LoginContract.View {

    @BindView(R.id.et_tenant)
    EditText etTenant;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Inject
    LoginPresenter loginPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter.attachView(this);
        setActionBarTitle(getString(R.string.fineract_account));
        preferencesHelper.clear();
    }

    @OnClick(R.id.btn_login)
    void onLogin() {

        String tenantIdentifier = etTenant.getEditableText().toString();
        if (!TextUtils.isEmpty(tenantIdentifier.trim())) {
            preferencesHelper.putTenantIdentifier(tenantIdentifier);
        } else {
            etTenant.setError(getString(R.string.error_tenant_identifier_required));
            return;
        }

        String username = etUsername.getEditableText().toString();
        if (TextUtils.isEmpty(username.trim())) {
            etUsername.setError(getString(R.string.error_username_required));
            return;
        }

        String password = etPassword.getEditableText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_password_required));
            return;
        }

        loginPresenter.login(username, password);
    }

    @Override
    public void showUserLoginSuccessfully(Authentication user) {
        preferencesHelper.putAccessToken(user.getAccessToken());
        preferencesHelper.putSignInUser(user);
        preferencesHelper.putUserName(etUsername.getEditableText().toString().trim());
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoInternetConnection() {
        Toaster.show(findViewById(android.R.id.content), R.string.no_internet_connection);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog() {
        showMifosProgressDialog(getString(R.string.logging_in));
    }

    @Override
    public void hideProgressDialog() {
        hideMifosProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideMifosProgressDialog();
        loginPresenter.detachView();
    }
}
