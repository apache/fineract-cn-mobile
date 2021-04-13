package org.apache.fineract.ui.online.groups.groupdetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_groups_details.*
import kotlinx.android.synthetic.main.fragment_groups_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.creategroup.CreateGroupActivity
import org.apache.fineract.ui.online.groups.grouptasks.GroupTasksBottomSheetFragment
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.Utils
import javax.inject.Inject


/*
 * Created by saksham on 22/June/2019
*/

class GroupDetailsFragment : FineractBaseFragment() {

    lateinit var rootView: View
    lateinit var group: Group

    @Inject
    lateinit var membersNameAdapter: NameListAdapter

    @Inject
    lateinit var leadersNameAdapter: NameListAdapter

    companion object {
        fun newInstance(group: Group): GroupDetailsFragment {
            val fragment = GroupDetailsFragment()
            val args = Bundle()
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
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        rootView.rv_members.adapter = membersNameAdapter
        membersNameAdapter.setReview(true)
        rootView.rv_leaders.adapter = leadersNameAdapter
        leadersNameAdapter.setReview(true)
        setToolbarTitle(group.identifier)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Utils.setToolbarIconColor(context, menu, R.color.white)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_group_detials, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_group -> {
                val intent = Intent(activity, CreateGroupActivity::class.java).apply {
                    putExtra(Constants.GROUP, group)
                    putExtra(Constants.GROUP_ACTION, GroupAction.EDIT)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvIdentifier.text = group.identifier
        tvGroupId.text = group.groupDefinitionIdentifier
        tvName.text = group.name
        statusChip.text = group.status?.name
        setGroupStatusChipIcon(group.status)
        group.leaders?.let {
            leadersNameAdapter.submitList(it as ArrayList<String>)
        }
        group.members?.let {
            membersNameAdapter.submitList(it as ArrayList<String>)
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

    @OnClick(R.id.cv_tasks)
    fun onTasksCardViewClicked() {
        val bottomSheet = GroupTasksBottomSheetFragment(group)
        bottomSheet.show(childFragmentManager, getString(R.string.tasks))
    }

    private fun setGroupStatusChipIcon(status: Group.Status?) {
        when (status) {
            Group.Status.PENDING -> {
                statusChip.chipIcon = ContextCompat.getDrawable(context!!,
                        R.drawable.ic_hourglass_empty_black_24dp)
                statusChip.setChipBackgroundColorResource(R.color.pending_blue)
            }
            Group.Status.ACTIVE -> {
                statusChip.chipIcon = ContextCompat.getDrawable(context!!,
                        R.drawable.ms_ic_check)
                statusChip.setChipBackgroundColorResource(R.color.activate_green)
            }
            Group.Status.CLOSED -> {
                statusChip.chipIcon = ContextCompat.getDrawable(context!!,
                        R.drawable.ic_close_black_24dp)
                statusChip.setChipBackgroundColorResource(R.color.closed_red)
            }
        }
    }
}