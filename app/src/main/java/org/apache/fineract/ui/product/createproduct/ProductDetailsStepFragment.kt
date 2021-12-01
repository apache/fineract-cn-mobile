package org.apache.fineract.ui.product.createproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_product_details.*
import kotlinx.android.synthetic.main.fragment_step_product_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.product.InterestBasis
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.product.ProductAction
import org.apache.fineract.utils.Constants


/*
 * Created by Varun Jain on 14th August 2021
 */

class ProductDetailsStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    private lateinit var productAction: ProductAction

    companion object {
        fun newInstance(productAction: ProductAction) = ProductDetailsStepFragment().apply {
            val bundle = Bundle().apply {
                putSerializable(Constants.PRODUCT_ACTION, productAction)
            }
            arguments = bundle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.PRODUCT_ACTION)?.let {
            productAction = it as ProductAction
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_product_details, container, false)
        if (productAction == ProductAction.EDIT) {
            populateViews()
        }
        return rootView
    }

    private fun populateViews() {
        val product = (activity as CreateProductActivity).getProduct()
        rootView.etIdentifier.setText(product.identifier)
        rootView.etIdentifier.isEnabled = false
        rootView.etName.setText(product.name)
        rootView.etPatternPackage.setText(product.patternPackage)
        rootView.etDescription.setText(product.description)
        rootView.etCurrencyCode.setText(product.currencyCode)
        rootView.etParameters.setText(product.parameters)
        rootView.etMinorCurrencyUnitDigits.setText(product.minorCurrencyUnitDigits.toString())
        rootView.spinnerInterestBasis.setSelection(getIndexFromInterestBasisType(product.interestBasis))
        rootView.etTermRangeMax.setText(product.termRange?.maximum.toString())
        rootView.etTemporalUnit.setText(product.termRange?.temporalUnit)
        rootView.etBalanceRangeMinimum.setText(product.balanceRange?.minimum.toString())
        rootView.etMaximumBalanceRange.setText(product.balanceRange?.maximum.toString())
        rootView.etMinInterestRange.setText(product.interestRange?.minimum.toString())
        rootView.etMaximumInterestRange.setText(product.interestRange?.maximum.toString())
    }


    private fun getIndexFromInterestBasisType(interestBasis: InterestBasis?): Int {
        return when (interestBasis) {
            InterestBasis.BEGINNING_BALANCE -> 0
            InterestBasis.CURRENT_BALANCE -> 1
            else -> return 0
        }
    }

    private fun getInterestBasisTypeFromIndex(index: Int): InterestBasis {
        return when (index) {
            0 -> InterestBasis.BEGINNING_BALANCE
            1 -> InterestBasis.CURRENT_BALANCE
            else -> InterestBasis.BEGINNING_BALANCE
        }
    }

    override fun verifyStep(): VerificationError? {
        if (!validateBalanceRangeMax() || !validateName() || !validateIdentifier() || !validatePatternPackage() || !validateTermRangeMax() || !validateInterestRangeMax() || !validateInterestRangeMin() || !validateBalanceRangeMax() || !validateBalanceRangeMin() || !validateDescription() || !validateCurrencyCode() || !validateMinorCurrencyUnitDigits() || !validateParameters()) {
            return VerificationError(null)
        }
        (activity as CreateProductActivity)
            .setProductDetails(
                etIdentifier.text.toString(),
                etName.text.toString(),
                etDescription.text.toString(),
                etCurrencyCode.text.toString(),
                etMinorCurrencyUnitDigits.text.toString().toInt(),
                etParameters.text.toString(),
                etPatternPackage.text.toString()
            )
        (activity as CreateProductActivity)
            .setTermRange(etTemporalUnit.text.toString(), etTermRangeMax.text.toString().toDouble())
        (activity as CreateProductActivity)
            .setBalanceRange(
                etBalanceRangeMinimum.text.toString().toDouble(),
                etMaximumBalanceRange.text.toString().toDouble()
            )
        (activity as CreateProductActivity)
            .setInterestRange(
                etMinInterestRange.text.toString().toDouble(),
                etMaximumInterestRange.text.toString().toDouble()
            )
        (activity as CreateProductActivity)
            .setInterestBasis(getInterestBasisTypeFromIndex(spinnerInterestBasis.selectedItemPosition))
        return null
    }

    private fun validateIdentifier(): Boolean {
        return etIdentifier.validator()
            .nonEmpty()
            .minLength(5)
            .addErrorCallback {
                etIdentifier.error = it
            }
            .check()
    }

    private fun validateName(): Boolean {
        return etName.validator()
            .nonEmpty()
            .minLength(5)
            .addErrorCallback {
                etName.error = it
            }
            .check()
    }

    private fun validatePatternPackage(): Boolean {
        return etPatternPackage.validator()
            .minLength(5)
            .nonEmpty()
            .addErrorCallback {
                etPatternPackage.error = it
            }
            .check()
    }

    private fun validateDescription(): Boolean {
        return etDescription.validator()
            .minLength(5)
            .nonEmpty()
            .addErrorCallback {
                etDescription.error = it
            }
            .check()
    }

    private fun validateCurrencyCode(): Boolean {
        return etCurrencyCode.validator()
            .minLength(5)
            .nonEmpty()
            .addErrorCallback {
                etCurrencyCode.error = it
            }
            .check()
    }

    private fun validateMinorCurrencyUnitDigits(): Boolean {
        return etMinorCurrencyUnitDigits.validator()
            .nonEmpty()
            .addErrorCallback {
                etMinorCurrencyUnitDigits.error = it
            }
            .check()
    }

    private fun validateParameters(): Boolean {
        return etParameters.validator()
            .minLength(5)
            .addErrorCallback {
                etParameters.error = it
            }
            .check()
    }

    private fun validateInterestRangeMax(): Boolean {
        return etMaximumInterestRange.validator()
            .nonEmpty()
            .addErrorCallback {
                etMaximumInterestRange.error = it
            }
            .check()
    }

    private fun validateInterestRangeMin(): Boolean {
        return etMinInterestRange.validator()
            .nonEmpty()
            .addErrorCallback {
                etMinInterestRange.error = it
            }
            .check()
    }

    private fun validateBalanceRangeMin(): Boolean {
        return etBalanceRangeMinimum.validator()
            .nonEmpty()
            .addErrorCallback {
                etBalanceRangeMinimum.error = it
            }
            .check()
    }

    private fun validateBalanceRangeMax(): Boolean {
        return etMaximumBalanceRange.validator()
            .nonEmpty()
            .addErrorCallback {
                etMaximumBalanceRange.error = it
            }
            .check()
    }

    private fun validateTermRangeMax(): Boolean {
        return etTermRangeMax.validator()
            .nonEmpty()
            .addErrorCallback {
                etTermRangeMax.error = it
            }
            .check()
    }

    override fun onSelected() {}

    override fun onError(p0: VerificationError) {}
}