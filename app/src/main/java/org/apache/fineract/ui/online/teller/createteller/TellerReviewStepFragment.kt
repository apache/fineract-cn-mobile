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
        tvTellerReviewCode.text = teller.code
        tvTellerReviewPassword.text = teller.password
        tvTellerReviewCashdrawLimit.text = teller.cashdrawLimit.toString()
        tvTellerReviewAccountIdentifier.text = teller.tellerAccountIdentifier
        tvTellerReviewVaultAccountIdentifier.text = teller.vaultAccountIdentifier
        tvTellerReviewDenomination.isChecked = teller.denominationRequired
        tvTellerReviewCra.text = teller.chequesReceivableAccount
        tvTellerReviewCashoverShortAccount.text = teller.cashOverShortAccount
        tvTellerReviewAssignedEmployee.text = teller.assignedEmployee
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
        fillViews((activity as CreateTellerActivity).getTeller())
    }

    override fun onError(error: VerificationError) {}
}