package org.apache.fineract.ui.online.teller.tellerdetails

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.Constants

/*
 * Created by Varun Jain on 14.06.2021
*/

class TellerDetailsActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        replaceFragment(TellerDetailsFragment.newInstance(intent.getParcelableExtra(Constants.TELLER)), false, R.id.container)
        showBackButton()
    }

}