package org.apache.fineract.ui.adapters

import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.createledger.CreateLedgerDetailsFragment
import org.apache.fineract.ui.online.accounting.ledgers.createledger.CreateLedgerReviewFragment
import org.apache.fineract.ui.online.accounting.ledgers.createledger.CreateLedgerSubLedgerFragment

class CreateLedgerStepAdapter constructor(
        fragmentManager: FragmentManager,
        context: Context,
        private val ledger: Ledger?,
        private val ledgerAction: LedgerAction
) : AbstractFragmentStepAdapter(fragmentManager, context) {

    private val createLedgerStep =
            context.resources.getStringArray(R.array.create_ledger_steps)

    override fun getCount(): Int = createLedgerStep.size

    override fun createStep(@IntRange(from = 0L) position: Int): Step {
        return return when (position) {
            0 -> CreateLedgerDetailsFragment.newInstance(ledger,ledgerAction)
            1 -> CreateLedgerSubLedgerFragment.newInstance(ledger,ledgerAction)
            else -> CreateLedgerReviewFragment.newInstance()
        }
    }

    override fun getViewModel(@IntRange(from = 0L) position: Int): StepViewModel {
        return StepViewModel.Builder(context)
                .setTitle(createLedgerStep[position])
                .create()
    }
}