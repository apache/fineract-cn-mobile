package org.apache.fineract.ui.online.loanaccounts.plannedpayment;

import android.os.Bundle;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.utils.ConstantKeys;

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
