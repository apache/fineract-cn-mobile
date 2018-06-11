package org.apache.fineract.ui.online.review

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_add_loan_review.*
import org.apache.fineract.R
import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot
import org.apache.fineract.ui.adapters.LoanDebtIncomeAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.loanaccounts.loanapplication.OnNavigationBarListener
import java.util.*
import javax.inject.Inject

class AddLoanReviewFragment : FineractBaseFragment(), Step {

    var onNavigationBarListener: OnNavigationBarListener.ReviewLoan? = null
    var creditWorthinessSnapshot: List<CreditWorthinessSnapshot>? = null

    lateinit var repayUnitType: Array<String>
    lateinit var days: Array<String>
    lateinit var months: Array<String>
    lateinit var repayOnMonths: Array<String>
    lateinit var timeSlots: Array<String>
    lateinit var reviewStep: Array<String>

    @Inject
    lateinit var incomeAdapter: LoanDebtIncomeAdapter

    @Inject
    lateinit var debtAdapter: LoanDebtIncomeAdapter

    @Inject
    lateinit var incomeCoSignerAdapter: LoanDebtIncomeAdapter

    @Inject
    lateinit var debtCoSignerAdapter: LoanDebtIncomeAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnNavigationBarListener.ReviewLoan) {
            onNavigationBarListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnNavigationBarListener.ReviewLoan")
        }
    }

    companion object {
        fun newInstance(): AddLoanReviewFragment = AddLoanReviewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repayUnitType = resources.getStringArray(R.array.repay_unit_type)
        days = resources.getStringArray(R.array.week_names)
        months = resources.getStringArray(R.array.month_names)
        repayOnMonths = resources.getStringArray(R.array.repay_on_months)
        timeSlots = resources.getStringArray(R.array.time_slots)
        reviewStep = resources.getStringArray(R.array.loan_application_steps)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as FineractBaseActivity).activityComponent.inject(this)
        val view = inflater.inflate(R.layout.fragment_add_loan_review, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()
    }

    private fun showUserInterface() {
        val layoutManagerDebt = LinearLayoutManager(activity)
        layoutManagerDebt.orientation = LinearLayoutManager.VERTICAL
        rvDebt.layoutManager = layoutManagerDebt
        rvDebt.setHasFixedSize(true)
        rvDebt.adapter = debtAdapter

        val layoutManagerIncome = LinearLayoutManager(activity)
        layoutManagerIncome.orientation = LinearLayoutManager.VERTICAL
        rvIncome.layoutManager = layoutManagerIncome
        rvIncome.setHasFixedSize(true)
        rvIncome.adapter = incomeAdapter

        val layoutManagerCoSignerDebt = LinearLayoutManager(activity)
        layoutManagerCoSignerDebt.orientation = LinearLayoutManager.VERTICAL
        rvCosignerDebt.layoutManager = layoutManagerCoSignerDebt
        rvCosignerDebt.setHasFixedSize(true)
        rvCosignerDebt.adapter = debtCoSignerAdapter

        val layoutManagerCoSignerIncome = LinearLayoutManager(activity)
        layoutManagerCoSignerIncome.orientation = LinearLayoutManager.VERTICAL
        rvCosignerIncome.layoutManager = layoutManagerCoSignerIncome
        rvCosignerIncome.setHasFixedSize(true)
        rvCosignerIncome.adapter = incomeCoSignerAdapter

        debtAdapter.isReview(true)
        debtCoSignerAdapter.isReview(true)
        incomeAdapter.isReview(true)
        incomeCoSignerAdapter.isReview(true)

        tvHeadOne.text = reviewStep[0]
        tvHeadTwo.text = reviewStep[1]
        tvHeadThree.text = reviewStep[2]

    }

    override fun onSelected() {

        val loanAccount = onNavigationBarListener?.loanAccount
        val loanParameters = loanAccount?.getLoanParameters()
        val selectedProduct = onNavigationBarListener?.selectedProduct
        creditWorthinessSnapshot = onNavigationBarListener?.creditWorthinessSnapshot
        val paymentCycle = loanParameters?.paymentCycle
        val repayUnit = loanParameters?.paymentCycle?.temporalUnit?.toLowerCase()

        tvProduct.text = selectedProduct
        tvShortName.text = loanAccount?.identifier
        tvPrincipalAmount.text = loanParameters?.maximumBalance.toString()
        tvTerm.text = loanParameters?.termRange?.maximum.toString()
        tvTermUnitType.text = loanParameters?.termRange?.temporalUnit
        tvRepay.text = loanParameters?.paymentCycle?.period.toString()
        tvRepayUnitType.text = repayUnit

        creditWorthinessSnapshot?.forEachIndexed { index, creditWorthinessSnapshot ->

            when (index) {
                0 -> {

                    if (creditWorthinessSnapshot.incomeSources.isEmpty() &&
                            creditWorthinessSnapshot.debts.isEmpty()) {
                        llHeadTwo.visibility = View.GONE
                    } else {
                        llHeadTwo.visibility = View.VISIBLE
                    }

                    if (creditWorthinessSnapshot.incomeSources.isEmpty()) {
                        llIncome.visibility = View.GONE
                    } else {
                        llIncome.visibility = View.VISIBLE
                        incomeAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.incomeSources)
                        incomeAdapter.notifyDataSetChanged()
                        tvTotalIncome.text = getString(R.string.total_income,
                                setPrecision(showTotalIncome(0)))
                    }

                    if (creditWorthinessSnapshot.debts.isEmpty()) {
                        llDebts.visibility = View.GONE
                    } else {
                        llDebts.visibility = View.VISIBLE
                        debtAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.debts)
                        debtAdapter.notifyDataSetChanged()
                        tvTotalDebt.text = getString(R.string.total_debt,
                                setPrecision(showTotalDebts(0)))
                    }
                }

                1 -> {

                    if (creditWorthinessSnapshot.incomeSources.isEmpty()) {
                        llCosignerIncome.visibility = View.GONE
                    } else {
                        llCosignerIncome.visibility = View.VISIBLE
                        incomeCoSignerAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.incomeSources)
                        incomeCoSignerAdapter.notifyDataSetChanged()
                        tvCosignerTotalIncome.text = getString(R.string.total_income,
                                setPrecision(showTotalIncome(1)))
                    }

                    if (creditWorthinessSnapshot.debts.isEmpty()) {
                        llCosignerDebts.visibility = View.GONE
                    } else {
                        llCosignerDebts.visibility = View.VISIBLE
                        debtCoSignerAdapter.setCreditWorthinessFactors(creditWorthinessSnapshot.debts)
                        debtCoSignerAdapter.notifyDataSetChanged()
                        tvCosignerTotalDebt.text = getString(R.string.total_debt,
                                setPrecision(showTotalDebts(1)))
                    }

                    tvCoSignerName.text = creditWorthinessSnapshot.forCustomer
                }
            }
        }

        repayUnitType.forEachIndexed { index, repayType ->

            if (repayType == repayUnit) {

                when (index) {

                    0 -> {
                        llOn.visibility = View.VISIBLE
                        llOnThe.visibility = View.GONE
                        llIn.visibility = View.GONE
                        tvRepayUnitWeek.text = days[paymentCycle?.alignmentDay!!]
                    }

                    1 -> {
                        llOnThe.visibility = View.VISIBLE
                        llOn.visibility = View.GONE
                        llIn.visibility = View.GONE

                        if (paymentCycle?.alignmentWeek == null) {
                            tvOnThe.text = repayOnMonths[paymentCycle?.alignmentDay!!]
                        } else {
                            tvOnThe.text = timeSlots[paymentCycle.alignmentWeek!!]
                            tvOnType.text = days[paymentCycle.alignmentDay!!]
                        }
                    }

                    2 -> {
                        llIn.visibility = View.VISIBLE
                        llOnThe.visibility = View.VISIBLE
                        llOn.visibility = View.GONE

                        if (paymentCycle?.alignmentWeek == null) {
                            tvOnThe.text = repayOnMonths[paymentCycle?.alignmentDay!!]
                            tvOnType.text = getString(R.string.day)
                        } else {
                            tvOnThe.text = timeSlots[paymentCycle.alignmentWeek!!]
                            tvOnType.text = days[paymentCycle.alignmentDay!!]
                        }

                        tvRepayYearMonth.text = months[paymentCycle.alignmentMonth!!]
                    }
                }
            }
        }

    }


    private fun showTotalIncome(pos: Int): Double {

        var totalIncome = 0.0

        creditWorthinessSnapshot?.get(pos)?.incomeSources?.forEach {
            totalIncome += it.amount!!
        }
        return totalIncome
    }

    fun showTotalDebts(pos: Int): Double {

        var totalDebts = 0.0

        creditWorthinessSnapshot?.get(pos)?.debts?.forEach {
            totalDebts += it.amount!!
        }

        return totalDebts
    }

    private fun setPrecision(aDouble: Double?): String {
        return String.format(Locale.ENGLISH, "%.2f", aDouble)
    }

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) {
    }

    override fun onDetach() {
        super.onDetach()
        onNavigationBarListener = null
    }

}
