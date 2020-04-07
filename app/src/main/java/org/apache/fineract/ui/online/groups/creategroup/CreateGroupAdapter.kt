package org.apache.fineract.ui.online.groups.creategroup

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.ui.online.groups.GroupAction


/*
 * Created by saksham on 02/July/2019
*/

class CreateGroupAdapter constructor(fm: FragmentManager,
                                     context: Context,
                                     val groupAction: GroupAction)
    : AbstractFragmentStepAdapter(fm, context) {

    private var createGroupSteps = context.resources.getStringArray(R.array.create_group_steps)

    override fun getCount(): Int {
        return createGroupSteps.size
    }

    override fun createStep(position: Int): Step? {
        when (position) {
            0 -> return GroupDetailsStepFragment.newInstance(groupAction)
            1 -> return AddGroupMemberStepFragment.newInstance(groupAction)
            2 -> return AddGroupLeaderStepFragment.newInstance(groupAction)
            3 -> return GroupAddressStepFragment.newInstance(groupAction)
            4 -> return GroupReviewStepFragment.newInstance()
        }
        return null
    }

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context).setTitle(createGroupSteps[position]).create()
    }
}