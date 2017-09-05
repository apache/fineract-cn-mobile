package org.apache.fineract.ui.online.customers.customeractivities;

import android.os.Bundle;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public class CustomerActivitiesActivity extends MifosBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_container);

        String identifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        replaceFragment(CustomerActivitiesFragment.newInstance(identifier), false, R.id.container);

        showBackButton();
    }
}
