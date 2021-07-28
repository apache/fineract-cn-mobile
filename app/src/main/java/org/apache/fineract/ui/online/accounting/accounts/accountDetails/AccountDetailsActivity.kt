package org.apache.fineract.ui.online.accounting.accounts.accountDetails

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.Constants

/**
 * Created by Varun Jain on 17/July/2021
 */

class AccountDetailsActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        replaceFragment(AccountDetailsFragment.newInstance(intent.getParcelableExtra(Constants.ACCOUNT)), false, R.id.container)
        showBackButton()
    }
}