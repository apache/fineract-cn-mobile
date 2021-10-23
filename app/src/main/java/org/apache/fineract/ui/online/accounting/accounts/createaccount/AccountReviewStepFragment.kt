package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_account_details.view.*
import kotlinx.android.synthetic.main.fragment_step_account_review.*
import kotlinx.android.synthetic.main.fragment_step_account_review.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import javax.inject.Inject


/*
 * Created by Varun Jain on 21/July/2021
*/

class AccountReviewStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View

    @Inject
    lateinit var holdersAdapter: NameListAdapter

    @Inject
    lateinit var signatureAuthoritiesAdapter: NameListAdapter

    companion object {
        fun newInstance(): AccountReviewStepFragment {
            return AccountReviewStepFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_account_review, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rvSignatureAuthoritiesAccountStepReview.adapter = signatureAuthoritiesAdapter
        signatureAuthoritiesAdapter.setReview(true)
        rootView.rvHoldersAccountStepReview.adapter = holdersAdapter
        holdersAdapter.setReview(true)

        return rootView
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    private fun populateView(account : Account) {
        tvIdentifierAccountStepReview.text = account.identifier
        tvTypeAccountStepReview.text = account.type.toString()
        tvNameAccountStepReview.text = account.name
        account.holders?.let {
            holdersAdapter.submitList(it as ArrayList<String>)
        }
        account.signatureAuthorities?.let {
            signatureAuthoritiesAdapter.submitList(it as ArrayList<String>)
        }
        tvBalanceAccountStepReview.text = account.balance.toString()
        tvRefAccountStepReview.text = account.referenceAccount
        tvLedgerAccountStepReview.text = account.ledger
        tvAltAccountNoAccountStepReview.text = account.alternativeAccountNumber
    }

    override fun onSelected() {
        populateView((activity as CreateAccountActivity).getAccount())
    }

    override fun onError(p0: VerificationError) {}
}