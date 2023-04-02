package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.ui.online.accounting.accounts.AccountAction

/*
 * Created by Varun Jain on 21/July/2021
*/

class CreateAccountAdapter constructor(fm: FragmentManager,
                                     context: Context,
                                     val accountAction: AccountAction
) : AbstractFragmentStepAdapter(fm, context) {

    private var createAccountSteps = context.resources.getStringArray(R.array.create_account_steps)

    override fun getCount(): Int {
        return createAccountSteps.size
    }

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> return AccountDetailsStepFragment.newInstance(accountAction)
            1 -> return AddAccountHoldersStepFragment.newInstance(accountAction)
            2 -> return AddAccountSignAuthoritiesFragment.newInstance(accountAction)
            3 -> return AccountReviewStepFragment.newInstance()
        }
        return null
    }

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context).setTitle(createAccountSteps[position]).create()
    }
}