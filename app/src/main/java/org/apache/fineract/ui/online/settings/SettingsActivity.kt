package org.apache.fineract.ui.online.settings

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity

class SettingsActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setToolbarTitle(getString(R.string.settings))
        showBackButton()
        replaceFragment(SettingsFragment.newInstance(), false, R.id.container)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}