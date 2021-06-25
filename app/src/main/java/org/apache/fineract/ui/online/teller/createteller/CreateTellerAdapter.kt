package org.apache.fineract.ui.online.teller.createteller

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.ui.online.teller.TellerAction

/*
 * Created by Varun Jain on 16.06.2021
*/

class CreateTellerAdapter constructor(fm: FragmentManager,
                                      context: Context,
                                      val tellerAction: TellerAction
)
    : AbstractFragmentStepAdapter(fm, context){

    private var createTellerSteps = context.resources.getStringArray(R.array.create_teller_steps)

    override fun getCount(): Int {
        return createTellerSteps.size
    }

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> return TellerDetailsStepFragment.newInstance(tellerAction)
            1 -> return TellerReviewStepFragment.newInstance()
        }
        return null
    }

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context).setTitle(createTellerSteps[position]).create()
    }
}