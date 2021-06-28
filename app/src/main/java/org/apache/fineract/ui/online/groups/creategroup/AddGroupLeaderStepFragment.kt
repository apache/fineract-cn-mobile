package org.apache.fineract.ui.online.groups.creategroup

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_add_group_leader.*
import kotlinx.android.synthetic.main.fragment_step_add_group_leader.view.*
import kotlinx.android.synthetic.main.fragment_step_add_group_member.view.rv_name
import org.apache.fineract.R
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject


/*
 * Created by saksham on 02/July/2019
*/

class AddGroupLeaderStepFragment : FineractBaseFragment(), Step, NameListAdapter.OnItemClickListener {

    lateinit var rootView: View
    var leaders: ArrayList<String> = ArrayList()
    private var currentAction = GroupAction.CREATE
    private var editItemPosition = 0
    private lateinit var groupAction: GroupAction

    @Inject
    lateinit var nameLisAdapter: NameListAdapter

    companion object {
        fun newInstance(groupAction: GroupAction) = AddGroupLeaderStepFragment().apply {
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
        rootView = inflater.inflate(R.layout.fragment_step_add_group_leader, container, false)
        ButterKnife.bind(this, rootView)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rv_name.adapter = nameLisAdapter
        nameLisAdapter.setOnItemClickListener(this)
        if (groupAction == GroupAction.EDIT) {
            showDataOnViews()
        }
        return rootView
    }

    private fun showDataOnViews() {
        val group = (activity as CreateGroupActivity).getGroup()
        leaders = group.leaders as ArrayList<String>
        if (leaders.size == 0) {
            showRecyclerView(false)
        } else {
            showRecyclerView(true)
        }
        nameLisAdapter.submitList(leaders)
    }

    @Optional
    @OnClick(R.id.ibAddLeader)
    fun showAddLeaderView() {
        showAddLeaderView(GroupAction.CREATE, null)
    }

    private fun showAddLeaderView(action: GroupAction, name: String?) {
        currentAction = action
        llAddLeader.visibility = View.VISIBLE
        when (action) {
            GroupAction.CREATE -> {
                btnAddLeader.text = getString(R.string.add)
            }
            GroupAction.EDIT -> {
                etNewLeader.setText(name)
                btnAddLeader.text = getString(R.string.update)
            }
        }
    }

    @Optional
    @OnClick(R.id.btnAddLeader)
    fun addLeader() {
        if (etNewLeader.validator()
                        .nonEmpty()
                        .noNumbers()
                        .addErrorCallback { etNewLeader.error = it }.check()) {
            if (currentAction == GroupAction.CREATE) {
                leaders.add(etNewLeader.text.toString())
            } else {
                if (leaders[editItemPosition] != etNewLeader.text.toString()) {
                    showUpdateDeleteDialog(editItemPosition, getString(R.string.update),
                            getString(R.string.dialog_title_confirm_updation),
                            getString(R.string.dialog_message_confirm_name_updation, leaders[editItemPosition]))
                } else {
                    hideAddLeaderView()
                }
            }
        }
    }

    private fun hideAddLeaderView() {
        etNewLeader.text.clear()
        llAddLeader.visibility = View.GONE
        Utils.hideKeyboard(context, etNewLeader)
        showRecyclerView(true)
        nameLisAdapter.submitList(leaders)
    }

    private fun showUpdateDeleteDialog(selectedItem: Int, action: String?, title: String?, msg: String?) {
        MaterialDialog.Builder().init(context).apply {
            setTitle(title)
            setMessage(msg)
            setPositiveButton(action)
            { dialog: DialogInterface?, _ ->
                if (action.equals(getString(R.string.update))) {
                    leaders[selectedItem] = etNewLeader.text.toString()
                    hideAddLeaderView()
                } else if (action.equals(getString(R.string.delete))) {
                    leaders.removeAt(selectedItem)
                    nameLisAdapter.submitList(leaders)
                    if (leaders.size == 0) {
                        showRecyclerView(false)
                    }
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }

    fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rv_name.visibility = View.VISIBLE
            rootView.tvAddLeader.visibility = View.GONE
        } else {
            rootView.rv_name.visibility = View.GONE
            rootView.tvAddLeader.visibility = View.VISIBLE
        }

    }

    @Optional
    @OnClick(R.id.btnCancelAddLeader)
    fun cancelLeaderAddition() {
        etNewLeader.text.clear()
        llAddLeader.visibility = View.GONE
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (leaders.size == 0) {
            Toast.makeText(context, getString(R.string.error_group_atleast_1_member), Toast.LENGTH_SHORT).show()
            return VerificationError("")
        }
        (activity as CreateGroupActivity).setLeaders(leaders)
        return null
    }

    override fun onError(error: VerificationError) {

    }

    override fun onEditClicked(position: Int) {
        editItemPosition = position
        showAddLeaderView(GroupAction.EDIT, leaders[position])
    }

    override fun onDeleteClicked(position: Int) {
        showUpdateDeleteDialog(position, getString(R.string.delete),
                getString(R.string.dialog_title_confirm_deletion),
                getString(R.string.dialog_message_confirm_name_deletion, leaders[position]))
    }

}