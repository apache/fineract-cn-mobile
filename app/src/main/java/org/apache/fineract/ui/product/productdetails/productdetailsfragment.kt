package org.apache.fineract.ui.product.productdetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_product_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.loan.AccountAssignment
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.adapters.ProductAccountAssignmentsAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.creategroup.CreateGroupActivity
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.Utils
import javax.inject.Inject

/*
 * Created by Varun Jain on 07th August 2021
 */

class ProductDetailsFragment : FineractBaseFragment() {

    lateinit var rootView: View
    lateinit var  product: Product

    @Inject
    lateinit var productAccountAssignmentsAdapter: ProductAccountAssignmentsAdapter

    companion object {
        fun newInstance(product: Product): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            val args = Bundle()
            args.putParcelable(Constants.PRODUCT, product)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.get(Constants.PRODUCT) as Product
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_product_details, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        rootView.rvProductAccountAssignments.adapter = productAccountAssignmentsAdapter
        productAccountAssignmentsAdapter.setReview(true)
        setToolbarTitle(product.identifier)
        setHasOptionsMenu(true)
        return  rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Utils.setToolbarIconColor(context, menu, R.color.white)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_product_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_group -> {
                Toaster.show(rootView, "Under Construction", Toast.LENGTH_SHORT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvIdentifierProduct.text = product.identifier
        tvNameProduct.text = product.name
        tvPatternPackageProduct.text = product.patternPackage
        tvProductTemporalUnit.text = product.termRange?.temporalUnit
        tvMaximumTermRange.text = product.termRange?.maximum.toString()
        tvMinProductBalanceRange.text = product.balanceRange?.minimum.toString()
        tvMaxProductBalanceRange.text = product.balanceRange?.maximum.toString()
        tvMaximumTermRange.text = product.balanceRange?.maximum.toString()
        tvMaxInterestRange.text = product.interestRange?.maximum.toString()
        tvMinInterestRange.text = product.interestRange?.minimum.toString()
        tvProductDescription.text = product.description
        tvInterestBasis.text = product.interestBasis.toString()
        tvProductCurrencyCode.text = product.currencyCode.toString()
        tvProductMinorCurrencyUnit.text = product.minorCurrencyUnitDigits.toString()
        tvProductParams.text = product.parameters.toString()
        product.accountAssignments?.let {
            productAccountAssignmentsAdapter.submitList(it as ArrayList<AccountAssignment>)
        }
    }
}