package org.apache.fineract.ui.product.productDetails

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.apache.fineract.R
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys

class ProductDetailActivity : FineractBaseActivity() {

    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        product = intent.getParcelableExtra(ConstantKeys.PRODUCT)
        setToolbarTitle(getString(R.string.account_details))
        showBackButton()
        populateUserInterface()
    }

    private fun populateUserInterface() {
        nsvProduct.isNestedScrollingEnabled = true
        tvIdentifier.text = product.identifier
        tvName.text = product.name
        tvDescription.text = product.description
        tvPatternPackage.text = product.patternPackage
        tvInterestBasic.text = product.interestBasis.toString()
        tvTermRange.text = "${product.termRange?.maximum} ${product.termRange?.temporalUnit}"
        tvBalanceRange.text = "${product.balanceRange?.minimum}-${product.balanceRange?.maximum}"
        tvInterestBasic.text = product.interestBasis.toString()
        tvMinorCurrencyUnitDigits.text = product.minorCurrencyUnitDigits.toString()
        tvCurrencyCode.text = product.currencyCode
        tvParameters.text = product.parameters
        tvCreatedOn.text = product.createdOn
        tvCreatedBy.text = product.createdBy
        tvLastModifiedOn.text = product.lastModifiedOn
        tvLastModifiedBy.text = product.lastModifiedBy
    }
}