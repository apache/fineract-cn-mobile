package org.apache.fineract.ui.online.groups.creategroup

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
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseFragment


/*
 * Created by saksham on 02/July/2019
*/

class AddGroupMemberStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    lateinit var members: ArrayList<String>

    companion object {
        fun newInstance(): AddGroupMemberStepFragment {
            return AddGroupMemberStepFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        members = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_add_group_member, container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAddedMember.text = getString(R.string.no_group_member_added)
    }

    @Optional
    @OnClick(R.id.ibAddMember)
    fun showAddMemberView() {
        llAddMember.visibility = View.VISIBLE
    }

    @Optional
    @OnClick(R.id.btnAddMember)
    fun addMember() {
        if (etNewMember.validator()
                        .nonEmpty()
                        .noNumbers()
                        .addErrorCallback {
                            etNewMember.error = it
                        }.check()) {

            if (members.size == 0) {
                tvAddedMember.text = "\n"
            }
            tvAddedMember.append("${etNewMember.text}\n")
            members.add(etNewMember.text.toString())
            etNewMember.text.clear()
            llAddMember.visibility = View.GONE
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
}