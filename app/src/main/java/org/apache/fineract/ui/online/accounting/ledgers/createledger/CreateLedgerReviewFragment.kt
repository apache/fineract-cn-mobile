package org.apache.fineract.ui.online.accounting.ledgers.createledger


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_create_ledger_review.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.SubLedgerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.online.accounting.ledgers.OnNavigationBarListener
import javax.inject.Inject


class CreateLedgerReviewFragment : Fragment(), Step {

    private lateinit var rootView: View
    private lateinit var onNavigationBarListener:
            OnNavigationBarListener.LedgerReview
    private var ledger: Ledger? = null

    @Inject
    lateinit var ledgerAdapter: SubLedgerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_create_ledger_review, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rv_sub_ledgers.adapter = ledgerAdapter
        return rootView
    }

    private fun showDataOnViews() {
        ledger?.let {
            rootView.tv_type.text = it.type.toString()
            rootView.tv_identifier.text = it.identifier
            rootView.tv_name.text = it.name
            rootView.tv_description.text = it.description
            rootView.cb_show_acc_in_chart.isChecked = it.showAccountsInChart == true
            it.subLedgers?.let { ledgerList ->
                if (ledgerList.isEmpty()) {
                    showRecyclerView(false)
                } else {
                    showRecyclerView(true)
                    ledgerAdapter.isToReview(true)
                    ledgerAdapter.submitList(ledgerList as ArrayList<Ledger>)
                }
            }
        }
    }

    private fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rv_sub_ledgers.visibility = View.VISIBLE
            rootView.tv_empty_sub_ledger.visibility = View.GONE
        } else {
            rootView.rv_sub_ledgers.visibility = View.GONE
            rootView.tv_empty_sub_ledger.visibility = View.VISIBLE
        }
    }


    override fun onSelected() {
        ledger = onNavigationBarListener.getLedger()
        showDataOnViews()
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNavigationBarListener.LedgerReview) {
            onNavigationBarListener = context
        } else {
            throw RuntimeException(
                    "${CreateLedgerReviewFragment::class.simpleName}" +
                            "You must implement OnNavigationBarListener.LedgerReview")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateLedgerReviewFragment()
    }
}
