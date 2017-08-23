package com.mifos.apache.fineract.ui.online.loanaccounts.plannedpayment;

import android.os.Bundle;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 14/07/17.
 */

public class PlannedPaymentActivity extends MifosBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_container);

        String productIdentifier = getIntent().getExtras().getString(
                ConstantKeys.PRODUCT_IDENTIFIER);
        String caseIdentifier = getIntent().getExtras().getString(ConstantKeys.CASE_IDENTIFIER);
        replaceFragment(PlannedPaymentFragment.newInstance(productIdentifier, caseIdentifier),
                false, R.id.global_container);

        showBackButton();
    }
}
