package org.apache.fineract.ui.online.loanaccounts.loanaccountlist;

import android.os.Bundle;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public class LoanAccountsActivity extends FineractBaseActivity {

    private FineractBaseFragment baseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_container);

        String identifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        LoanAccountsFragment loanAccountsFragment = LoanAccountsFragment.newInstance(identifier);
        replaceFragment(loanAccountsFragment, false, R.id.container);
        assignBaseFragment(loanAccountsFragment);

        showBackButton();
    }

    private void assignBaseFragment(FineractBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    @Override
    public void onBackPressed() {
        if (baseFragment.baseSearchView != null &&
                !baseFragment.baseSearchView.isIconified()) {
            baseFragment.closeSearchView();
            return;
        }
        super.onBackPressed();
    }
}
