package org.apache.fineract.ui.online.groups.creategroup

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_step_add_group_leader.view.*
import kotlinx.android.synthetic.main.fragment_step_add_group_member.view.rv_name
import kotlinx.android.synthetic.main.member_leader_layout.view.*
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
        showAddEditDialog(action, name)
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
        MaterialDialog.Builder().init(context).apply {
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(getString(R.string.dialog_message_confirm_name_deletion, leaders[position]))
            setPositiveButton(getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                leaders.removeAt(position)
                nameLisAdapter.submitList(leaders)
                if (leaders.size == 0) {
                    showRecyclerView(false)
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }

    private fun showAddEditDialog(action: GroupAction, name: String?) {
        val layout = layoutInflater.inflate(R.layout.member_leader_layout, null)
        val dialog = AlertDialog.Builder(context)
                .setView(layout)
                .create()
        dialog.show()

        if (action == GroupAction.CREATE) {
            layout.btnAddMember.text = getString(R.string.add)
            layout.tv_member_head.text = getString(R.string.add_new_leader)
        }
        else if (action == GroupAction.EDIT) {
            layout.etNewMember.setText(name)
            layout.btnAddMember.text = getString(R.string.update)
            layout.tv_member_head.text = getString(R.string.edit_leader)
        }

        layout.btnAddMember.setOnClickListener {
            if (layout.etNewMember.validator()
                            .nonEmpty()
                            .noNumbers()
                            .addErrorCallback { layout.etNewMember.error = it }.check()) {
                if (currentAction == GroupAction.CREATE) {
                    leaders.add(layout.etNewMember.text.toString())
                } else {
                    leaders[editItemPosition] = layout.etNewMember.text.toString()
                }
                Utils.hideKeyboard(context, layout.etNewMember)
                showRecyclerView(true)
                nameLisAdapter.submitList(leaders)
                dialog.dismiss()
            }
        }

        layout.btnCancelAddMember.setOnClickListener { dialog.dismiss() }
    }
}