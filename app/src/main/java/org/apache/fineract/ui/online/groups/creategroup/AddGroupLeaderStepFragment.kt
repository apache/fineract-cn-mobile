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
import kotlinx.android.synthetic.main.fragment_step_add_group_leader.*
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseFragment


/*
 * Created by saksham on 02/July/2019
*/

class AddGroupLeaderStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    lateinit var leaders: ArrayList<String>

    companion object {
        fun newInstance(): AddGroupLeaderStepFragment {
            return AddGroupLeaderStepFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leaders = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_add_group_leader, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, rootView)
        tvAddedLeader.text = getString(R.string.no_group_leader_added)
    }

    @Optional
    @OnClick(R.id.ibAddLeader)
    fun showAddLeaderView() {
        llAddLeader.visibility = View.VISIBLE
    }

    @Optional
    @OnClick(R.id.btnAddLeader)
    fun addLeader() {
        if (etNewLeader.validator()
                        .nonEmpty()
                        .noNumbers()
                        .addErrorCallback {
                            etNewLeader.error = it
                        }.check()) {
            if (leaders.size == 0) {
                tvAddedLeader.text = "\n"
            }
            tvAddedLeader.append("${etNewLeader.text}\n")
            leaders.add(etNewLeader.text.toString())
            etNewLeader.text.clear()
            llAddLeader.visibility = View.GONE
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
            Toast.makeText(context, getString(R.string.error_group_atleast_1_leader), Toast.LENGTH_SHORT).show()
            return VerificationError("")
        }
        (activity as CreateGroupActivity).setLeaders(leaders)
        return null
    }

    override fun onError(error: VerificationError) {

    }

}