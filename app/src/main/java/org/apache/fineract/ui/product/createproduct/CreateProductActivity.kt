package org.apache.fineract.ui.product.createproduct

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_create_product.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.loan.AccountAssignment
import org.apache.fineract.data.models.loan.TermRange
import org.apache.fineract.data.models.product.BalanceRange
import org.apache.fineract.data.models.product.InterestBasis
import org.apache.fineract.data.models.product.InterestRange
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.product.ProductAction
import org.apache.fineract.ui.product.viewmodel.ProductViewModel
import org.apache.fineract.ui.product.viewmodel.ProductViewModelFactory
import org.apache.fineract.utils.Constants
import javax.inject.Inject

/*
 * Created by Varun Jain on 14th August 2021
 */

class CreateProductActivity : FineractBaseActivity(), StepperLayout.StepperListener {

    private var product = Product()
    private var productAction = ProductAction.CREATE

    @Inject
    lateinit var productViewModelFactory: ProductViewModelFactory

    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)
        activityComponent.inject(this)
        productAction = intent.getSerializableExtra(Constants.PRODUCT_ACTION) as ProductAction
        when (productAction) {
            ProductAction.EDIT -> {
                setToolbarTitle(getString(R.string.edit_product))
                intent?.extras?.getParcelable<Product>(Constants.PRODUCT)?.let {
                    product = it
                }
            }
            ProductAction.CREATE -> {
                setToolbarTitle(getString(R.string.create_product))
            }
        }
        productViewModel =
            ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel::class.java)
        subscribeUI()
        showBackButton()
        slCreateProduct.adapter = CreateProductAdapter(supportFragmentManager, this, productAction)
        slCreateProduct.setOffscreenPageLimit(slCreateProduct.adapter.count)
        slCreateProduct.setListener(this)
    }

    private fun subscribeUI() {
        productViewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING -> {
                    if (productAction == ProductAction.CREATE) {
                        showMifosProgressDialog(getString(R.string.create_product))
                    } else {
                        showMifosProgressDialog(getString(R.string.updating_product_please_wait))
                    }
                }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    if (productAction == ProductAction.CREATE) {
                        Toaster.show(
                            findViewById(android.R.id.content),
                            R.string.error_while_creating_product,
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        Toaster.show(
                            findViewById(android.R.id.content),
                            R.string.error_while_updating_group,
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                Status.DONE -> {
                    hideMifosProgressDialog()
                    if (productAction == ProductAction.CREATE) {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.product_identifier_created_successfully,
                                product.identifier
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.product_identifier_updated_successfully,
                                product.identifier
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            }
        })
    }

    fun setProductDetails(
        identifier: String,
        name: String,
        desc: String,
        currentCode: String,
        minorCurrencyUnitDigits: Int,
        parameters: String,
        patternPackage: String
    ) {
        product.identifier = identifier
        product.name = name
        product.description = desc
        product.currencyCode = currentCode
        product.minorCurrencyUnitDigits = minorCurrencyUnitDigits
        product.parameters = parameters
        product.patternPackage = patternPackage
    }

    fun setTermRange(temporalUnit: String, maxTermRange: Double) {
        product.termRange = TermRange(temporalUnit, maxTermRange)
    }

    fun setBalanceRange(minBalanceRange: Double, maxBalanceRange: Double) {
        product.balanceRange = BalanceRange(minBalanceRange, maxBalanceRange)
    }

    fun setInterestRange(minInterestRange: Double, maxInterestRange: Double) {
        product.interestRange = InterestRange(minInterestRange, maxInterestRange)
    }

    fun setInterestBasis(interestBasis: InterestBasis) {
        product.interestBasis = interestBasis
    }

    fun setAccountAssignments(accountAssignments: List<AccountAssignment>) {
        product.accountAssignments = accountAssignments
    }

    fun getProduct(): Product {
        return product
    }

    override fun onCompleted(p0: View?) {
        when (productAction) {
            ProductAction.CREATE -> {
                productViewModel.createProduct(product)
            }
            ProductAction.EDIT -> product.identifier?.let {
                productViewModel.updateProduct(product)
            }
        }
    }

    override fun onError(p0: VerificationError?) {}

    override fun onStepSelected(p0: Int) {}

    override fun onReturn() {}
}