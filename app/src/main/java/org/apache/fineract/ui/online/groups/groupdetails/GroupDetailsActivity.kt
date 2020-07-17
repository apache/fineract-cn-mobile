package org.apache.fineract.ui.online.groups.groupdetails

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.Constants


/*
 * Created by saksham on 21/June/2019
*/

class GroupDetailsActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        replaceFragment(GroupDetailsFragment.newInstance(intent.getParcelableExtra(Constants.GROUP)), false, R.id.container)
        showBackButton()
    }
}