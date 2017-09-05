package org.apache.fineract.ui.online.identification.identificationlist;

import android.os.Bundle;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.utils.ConstantKeys;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */

public class IdentificationsActivity extends FineractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_container);

        String identifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        replaceFragment(IdentificationsFragment.newInstance(identifier), false,
                R.id.container);


        showBackButton();
    }
}
