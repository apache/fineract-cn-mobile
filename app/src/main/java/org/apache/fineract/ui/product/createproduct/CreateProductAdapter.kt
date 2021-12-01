package org.apache.fineract.ui.product.createproduct

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.ui.product.ProductAction


/*
 * Created by Varun Jain on 15th August 2021
 */


class CreateProductAdapter constructor(fm: FragmentManager,
                                       context: Context,
                                       val productAction: ProductAction)
    : AbstractFragmentStepAdapter(fm, context) {

    private var createProductSteps = context.resources.getStringArray(R.array.create_product_steps)

    override fun getCount(): Int {
        return 3
    }

    override fun createStep(p0: Int): Step? {
        when (p0) {
            0 -> return ProductDetailsStepFragment.newInstance(productAction)
            1 -> return AddAccountAssignmentsFragment.newInstance(productAction)
            2 -> return ProductReviewStepFragment.newInstance()
        }
        return null
    }

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context).setTitle(createProductSteps[position]).create()
    }
}