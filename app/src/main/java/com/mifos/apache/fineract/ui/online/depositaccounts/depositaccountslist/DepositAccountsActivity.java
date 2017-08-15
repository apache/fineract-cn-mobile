package com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountslist;

import android.os.Bundle;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */

public class DepositAccountsActivity extends MifosBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_container);

        String identifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        replaceFragment(DepositAccountsFragment.newInstance(identifier), false,
                R.id.container);

        showBackButton();
    }
}
