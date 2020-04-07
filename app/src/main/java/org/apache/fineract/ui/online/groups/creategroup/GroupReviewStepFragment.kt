package org.apache.fineract.ui.online.groups.creategroup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_step_add_group_member.view.*
import kotlinx.android.synthetic.main.fragment_step_group_review.*
import kotlinx.android.synthetic.main.fragment_step_group_review.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import javax.inject.Inject


/*
 * Created by saksham on 04/July/2019
*/

class GroupReviewStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View

    @Inject
    lateinit var memberNameAdapter: NameListAdapter

    @Inject
    lateinit var leaderNameAdapter: NameListAdapter

    companion object {
        fun newInstance(): GroupReviewStepFragment {
            return GroupReviewStepFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_group_review, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rv_leaders.adapter = leaderNameAdapter
        leaderNameAdapter.setReview(true)
        rootView.rv_members.adapter = memberNameAdapter
        memberNameAdapter.setReview(true)

        return rootView
    }

    private fun populateView(group: Group) {
        tvIdentifier.text = group.identifier
        tvGroupDefinitionIdentifier.text = group.groupDefinitionIdentifier
        tvName.text = group.name
        tvOffice.text = group.office
        tvAssignedEmployee.text = group.assignedEmployee
        group.members?.let {
            memberNameAdapter.submitList(it as ArrayList<String>)
        }
        group.leaders?.let {
            leaderNameAdapter.submitList(it as ArrayList<String>)
        }
        tvStreet.text = group.address?.street
        tvCity.text = group.address?.city
        tvRegion.text = group.address?.region
        tvPostalCode.text = group.address?.postalCode
        tvCountry.text = group.address?.country
    }

    override fun onSelected() {
        populateView((activity as CreateGroupActivity).getGroup())
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {}
}