package org.apache.fineract.ui.online.teller.createteller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_step_teller_review.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.base.FineractBaseFragment

/*
 * Created by Varun Jain on 16.06.2021
*/

class TellerReviewStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View

    companion object {
        fun newInstance(): TellerReviewStepFragment {
            return TellerReviewStepFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_teller_review, container, false)
        (activity as CreateTellerActivity).activityComponent.inject(this)

        return rootView
    }

    private fun fillViews(teller: Teller) {
        tv_teller_review_code.text = teller.code
        tv_teller_review_password.text = teller.password
        tv_teller_review_cashwithdraw_limit.text = teller.cashdrawLimit.toString()
        tv_teller_review_acc_identifier.text = teller.tellerAccountIdentifier
        tv_teller_review_vault_acc_identifier.text = teller.vaultAccountIdentifier
        tv_teller_review_denomination_req.isChecked = teller.denominationRequired
        tv_teller_review_cheaques_recievable_amount.text = teller.chequesReceivableAccount
        tv_teller_review_cashovershortacc.text = teller.cashOverShortAccount
        tv_teller_review_assigned_employee.text = teller.assignedEmployee
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
        fillViews((activity as CreateTellerActivity).getTeller())
    }

    override fun onError(error: VerificationError) {}
}