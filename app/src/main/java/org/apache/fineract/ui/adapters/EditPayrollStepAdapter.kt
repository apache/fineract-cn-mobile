package org.apache.fineract.ui.adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.EditPayrollAllocationFragment
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.EditPayrollFragment

class EditPayrollStepAdapter(fm: FragmentManager, context: Context,
                             val payrollConfig: PayrollConfiguration)
    : AbstractFragmentStepAdapter(fm, context) {

    val payrollStep: Array<String> = context.resources.getStringArray(R.array.edit_payroll_steps)

    override fun getViewModel(position: Int): StepViewModel = StepViewModel.Builder(context)
            .setTitle(payrollStep[position])
            .create()

    override fun createStep(position: Int): Step? = when (position) {
        0 -> EditPayrollFragment.newInstance(payrollConfig)
        1 -> EditPayrollAllocationFragment.newInstance(payrollConfig)
        else -> null
    }

    override fun getCount(): Int = payrollStep.size

}