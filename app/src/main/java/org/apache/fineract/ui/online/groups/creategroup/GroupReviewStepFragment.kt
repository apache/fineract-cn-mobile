package org.apache.fineract.ui.online.groups.creategroup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_step_group_review.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.base.FineractBaseFragment


/*
 * Created by saksham on 04/July/2019
*/

class GroupReviewStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View

    companion object {
        fun newInstance(): GroupReviewStepFragment {
            return GroupReviewStepFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_group_review, container, false)
        return rootView
    }

    private fun populateView(group: Group) {
        tvIdentifier.text = group.identifier
        tvGroupDefinitionIdentifier.text = group.groupDefinitionIdentifier
        tvName.text = group.name
        tvOffice.text = group.office
        tvAssignedEmployee.text = group.assignedEmployee

        llMembers.removeAllViews()
        group.members?.forEach {
            addTextView(llMembers, it)
        }

        llLeaders.removeAllViews()
        group.leaders?.forEach {
            addTextView(llLeaders, it)
        }

        tvStreet.text = group.address?.street
        tvCity.text = group.address?.city
        tvRegion.text = group.address?.region
        tvPostalCode.text = group.address?.postalCode
        tvCountry.text = group.address?.country
    }

    private fun addTextView(linearLayout: LinearLayout, text: String) {
        var textView = TextView(context)
        var layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(5, 5, 5, 5)
        textView.setPadding(5, 5, 5, 5)
        textView.text = text
        textView.layoutParams = layoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(android.R.style.TextAppearance_Large)
        }
        textView.background = resources.getDrawable(R.drawable.border)
        linearLayout.addView(textView)
    }

    override fun onSelected() {
        populateView((activity as CreateGroupActivity).getGroup())
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }
}