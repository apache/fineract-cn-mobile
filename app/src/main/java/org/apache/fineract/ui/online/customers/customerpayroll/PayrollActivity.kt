package org.apache.fineract.ui.online.customers.customerpayroll

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys

class PayrollActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        val customerIdentifier = intent.getStringExtra(ConstantKeys.CUSTOMER_IDENTIFIER)
        replaceFragment(PayrollFragment.newInstance(customerIdentifier), false, R.id.container)

        showBackButton()
    }
}
