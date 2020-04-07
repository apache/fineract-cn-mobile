package org.apache.fineract.ui.online.groups.creategroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_group_details.*
import kotlinx.android.synthetic.main.fragment_step_group_details.view.*
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.utils.Constants


/*
 * Created by saksham on 02/July/2019
*/

class GroupDetailsStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    private lateinit var groupAction: GroupAction

    companion object {
        fun newInstance(groupAction: GroupAction) =
                GroupDetailsStepFragment().apply {
                    val bundle = Bundle().apply {
                        putSerializable(Constants.GROUP_ACTION, groupAction)
                    }
                    arguments = bundle
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.GROUP_ACTION)?.let {
            groupAction = it as GroupAction
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_group_details, container, false)
        if (groupAction == GroupAction.EDIT) {
            showDataOnViews()
        }
        return rootView
    }

    private fun showDataOnViews() {
        val group = (activity as CreateGroupActivity).getGroup()
        rootView.etIdentifier.setText(group.identifier)
        rootView.etIdentifier.isEnabled = false
        rootView.etGroupDefinitionIdentifier.setText(group.groupDefinitionIdentifier)
        rootView.etName.setText(group.name)
        rootView.etOffice.setText(group.office)
        rootView.etAssignedEmployee.setText(group.assignedEmployee)
    }

    override fun onSelected() {}

    override fun verifyStep(): VerificationError? {
        if (!validateIdentifier() || !validateGroupDefinitionIdentifier() || !validateName()
                || !validateOffice() || !validateAssignedEmployee()) {
            return VerificationError(null)
        }
        (activity as CreateGroupActivity).setGroupDetails(etIdentifier.text.toString(),
                etGroupDefinitionIdentifier.text.toString(),
                etName.text.toString(),
                etOffice.text.toString(),
                etAssignedEmployee.text.toString())
        return null
    }

    override fun onError(error: VerificationError) {
    }

    private fun validateIdentifier(): Boolean {
        return etIdentifier.validator()
                .minLength(5)
                .addErrorCallback {
                    etIdentifier.error = it
                }.check()
    }

    private fun validateGroupDefinitionIdentifier(): Boolean {
        return etGroupDefinitionIdentifier.validator()
                .minLength(5)
                .addErrorCallback {
                    etGroupDefinitionIdentifier.error = it
                }.check()
    }

    private fun validateName(): Boolean {
        return etName.validator()
                .minLength(5)
                .noNumbers()
                .addErrorCallback {
                    etName.error = it
                }.check()
    }

    private fun validateOffice(): Boolean {
        return etOffice.validator()
                .minLength(5)
                .noNumbers()
                .addErrorCallback {
                    etOffice.error = it
                }.check()
    }

    private fun validateAssignedEmployee(): Boolean {
        return etAssignedEmployee.validator()
                .minLength(5)
                .noNumbers()
                .addErrorCallback {
                    etAssignedEmployee.error = it
                }
                .check()
    }
}