package org.apache.fineract.ui.product.createproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_step_group_review.*
import kotlinx.android.synthetic.main.fragment_step_product_review.*
import kotlinx.android.synthetic.main.fragment_step_product_review.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.loan.AccountAssignment
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.adapters.ProductAccountAssignmentsAdapter
import org.apache.fineract.ui.base.FineractBaseFragment
import javax.inject.Inject


/*
 * Created by Varun Jain on 15th August 2021
 */

class ProductReviewStepFragment : FineractBaseFragment(), Step{

    lateinit var rootView: View

    @Inject
    lateinit var productAccountAssignmentsAdapter: ProductAccountAssignmentsAdapter

    companion object {
        fun newInstance(): ProductReviewStepFragment {
            return ProductReviewStepFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_product_review, container, false)
        (activity as CreateProductActivity).activityComponent.inject(this)
        rootView.rvProductAccountAssignmentsStepReview.adapter = productAccountAssignmentsAdapter
        productAccountAssignmentsAdapter.setReview(true)

        return rootView
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
        populateView((activity as CreateProductActivity).getProduct())
    }

    private fun populateView(product: Product) {
        tvProductIdentifier.text = product.identifier
        tvProductName.text = product.name
        tvPatternPackageProduct.text = product.patternPackage
        tvProductDescription.text = product.description
        tvProductCurrencyCode.text = product.currencyCode
        tvProductMinorCurrencyUnit.text = product.minorCurrencyUnitDigits.toString()
        tvParameters.text = product.parameters
        tvInterestBasis.text = product.interestBasis.toString()
        product.accountAssignments?.let {
            productAccountAssignmentsAdapter.submitList(it as ArrayList<AccountAssignment>)
        }
        tvMinProductBalanceRange.text = product.balanceRange?.minimum.toString()
        tvMaxProductBalanceRange.text = product.balanceRange?.maximum.toString()
        tvMinInterestRange.text = product.interestRange?.minimum.toString()
        tvMaxInterestRange.text = product.interestRange?.maximum.toString()
        tvProductTemporalUnit.text = product.termRange?.temporalUnit.toString()
        tvMaximumTermRange.text = product.termRange?.maximum.toString()
    }

    override fun onError(p0: VerificationError) {
    }
}