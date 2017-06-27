package com.mifos.apache.fineract.ui.customerdetails;

import android.os.Bundle;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public class CustomerDetailsActivity extends MifosBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        String identifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        replaceFragment(CustomerDetailsFragment.newInstance(identifier), false,
                R.id.global_container);

        showBackButton();
    }
}
