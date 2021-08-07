package org.apache.fineract.ui.product.productdetails

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.Constants

/*
 * Created by Varun Jain on 07th August 2021
 */

class ProductDetailsActivity :  FineractBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        replaceFragment(ProductDetailsFragment.newInstance(intent.getParcelableExtra(Constants.PRODUCT)), false, R.id.container)
        showBackButton()
    }
}