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
import kotlinx.android.synthetic.main.fragment_step_add_group_member.*
import kotlinx.android.synthetic.main.fragment_step_add_group_member.view.*
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

class AddGroupMemberStepFragment : FineractBaseFragment(), Step, NameListAdapter.OnItemClickListener {

    lateinit var rootView: View
    var members: ArrayList<String> = ArrayList()
    private var currentAction = GroupAction.CREATE
    private var editItemPosition = 0
    private lateinit var groupAction: GroupAction

    @Inject
    lateinit var nameLisAdapter: NameListAdapter

    companion object {
        fun newInstance(groupAction: GroupAction) = AddGroupMemberStepFragment().apply {
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
        rootView = inflater.inflate(R.layout.fragment_step_add_group_member, container, false)
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
        members = group.members as ArrayList<String>
        if (members.size == 0) {
            showRecyclerView(false)
        } else {
            showRecyclerView(true)
        }
        nameLisAdapter.submitList(members)
    }

    @Optional
    @OnClick(R.id.ibAddMember)
    fun showAddMemberView() {
        showAddMemberView(GroupAction.CREATE, null)
    }

    private fun showAddMemberView(action: GroupAction, name: String?) {
        currentAction = action
        llAddMember.visibility = View.VISIBLE
        when (action) {
            GroupAction.CREATE -> {
                btnAddMember.text = getString(R.string.add)
            }
            GroupAction.EDIT -> {
                etNewMember.setText(name)
                btnAddMember.text = getString(R.string.update)
            }
        }
    }

    @Optional
    @OnClick(R.id.btnAddMember)
    fun addMember() {
        if (etNewMember.validator()
                        .nonEmpty()
                        .noNumbers()
                        .addErrorCallback { etNewMember.error = it }.check()) {
            if (currentAction == GroupAction.CREATE) {
                members.add(etNewMember.text.toString())
            } else {
                members[editItemPosition] = etNewMember.text.toString()
            }
            etNewMember.text.clear()
            llAddMember.visibility = View.GONE
            Utils.hideKeyboard(context, etNewMember)
            showRecyclerView(true)
            nameLisAdapter.submitList(members)
        }
    }

    fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rv_name.visibility = View.VISIBLE
            rootView.tvAddedMember.visibility = View.GONE
        } else {
            rootView.rv_name.visibility = View.GONE
            rootView.tvAddedMember.visibility = View.VISIBLE
        }

    }

    @Optional
    @OnClick(R.id.btnCancelAddMember)
    fun cancelMemberAddition() {
        etNewMember.text.clear()
        llAddMember.visibility = View.GONE
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (members.size == 0) {
            Toast.makeText(context, getString(R.string.error_group_atleast_1_member), Toast.LENGTH_SHORT).show()
            return VerificationError("")
        }
        (activity as CreateGroupActivity).setMembers(members)
        return null
    }

    override fun onError(error: VerificationError) {

    }

    override fun onEditClicked(position: Int) {
        editItemPosition = position
        showAddMemberView(GroupAction.EDIT, members[position])
    }

    override fun onDeleteClicked(position: Int) {
        MaterialDialog.Builder().init(context).apply {
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(getString(R.string.dialog_message_confirm_name_deletion, members[position]))
            setPositiveButton(getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                members.removeAt(position)
                nameLisAdapter.submitList(members)
                if (members.size == 0) {
                    showRecyclerView(false)
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }
}