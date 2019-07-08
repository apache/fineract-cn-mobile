package org.apache.fineract.ui.online.groups.creategroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_group_details.*
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseFragment


/*
 * Created by saksham on 02/July/2019
*/

class GroupDetailsStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View

    companion object {
        fun newInstance(): GroupDetailsStepFragment {
            return GroupDetailsStepFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_group_details, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSelected() {

    }

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
                .noSpecialCharacters()
                .addErrorCallback {
                    etIdentifier.error = it
                }.check()
    }

    private fun validateGroupDefinitionIdentifier(): Boolean {
        return etGroupDefinitionIdentifier.validator()
                .minLength(5)
                .noSpecialCharacters()
                .addErrorCallback {
                    etGroupDefinitionIdentifier.error = it
                }.check()
    }

    private fun validateName(): Boolean {
        return etName.validator()
                .minLength(5)
                .noSpecialCharacters()
                .noNumbers()
                .addErrorCallback {
                    etName.error = it
                }.check()
    }

    private fun validateOffice(): Boolean {
        return etOffice.validator()
                .minLength(5)
                .noSpecialCharacters()
                .noNumbers()
                .addErrorCallback {
                    etOffice.error = it
                }.check()
    }

    private fun validateAssignedEmployee(): Boolean {
        return etAssignedEmployee.validator()
                .minLength(5)
                .noSpecialCharacters()
                .noNumbers()
                .addErrorCallback {
                    etAssignedEmployee.error = it
                }
                .check()
    }
}