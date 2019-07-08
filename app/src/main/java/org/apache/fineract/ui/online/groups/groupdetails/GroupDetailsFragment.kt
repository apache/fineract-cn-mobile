package org.apache.fineract.ui.online.groups.groupdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_groups_details.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.views.CircularImageView
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.Utils


/*
 * Created by saksham on 22/June/2019
*/

class GroupDetailsFragment : FineractBaseFragment() {

    lateinit var rootView: View
    lateinit var group: Group

    companion object {
        fun newInstance(group: Group): GroupDetailsFragment {
            var fragment = GroupDetailsFragment()
            var args = Bundle()
            args.putParcelable(Constants.GROUP, group)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = arguments?.get(Constants.GROUP) as Group
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_groups_details, container, false)
        setToolbarTitle(group.identifier)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvIdentifier.text = group.identifier
        tvGroupId.text = group.groupDefinitionIdentifier
        tvName.text = group.name
        tvStatus.text = group.status?.name
        setGroupStatusCircularIcon(group.status, civStatus)
        group.leaders?.forEach {
            tvLeaders.append("$it\n")
        }
        group.members?.forEach {
            tvMembers.append("$it\n")
        }
        tvOffice.text = group.office
        tvAssignedEmployee.text = group.assignedEmployee
        tvWeekday.text = DateUtils.getWeekDay(group.weekday!!)
        tvStreet.text = group.address?.street
        tvCity.text = group.address?.city
        tvRegion.text = group.address?.region
        tvPostalCode.text = group.address?.postalCode
        tvCountry.text = group.address?.country
    }

    private fun setGroupStatusCircularIcon(status: Group.Status?, civStatus: CircularImageView) {

        when (status) {
            Group.Status.PENDING -> {
                civStatus.setImageDrawable(Utils.setCircularBackground(R.color.blue, context))
            }
            Group.Status.ACTIVE -> {
                civStatus.setImageDrawable(Utils.setCircularBackground(R.color.deposit_green, context))
            }
            Group.Status.CLOSED -> {
                civStatus.setImageDrawable(Utils.setCircularBackground(R.color.red_dark, context))
            }
        }
    }
}