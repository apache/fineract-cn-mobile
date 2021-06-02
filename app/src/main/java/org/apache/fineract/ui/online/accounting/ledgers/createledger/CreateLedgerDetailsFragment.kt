package org.apache.fineract.ui.online.accounting.ledgers.createledger


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_create_ledger_details.*
import kotlinx.android.synthetic.main.fragment_create_ledger_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.OnNavigationBarListener
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.ValidateIdentifierUtil.showTextInputLayoutError

/**
 * A simple [Fragment] subclass.
 */
class CreateLedgerDetailsFragment : Fragment(), Step {

    private lateinit var rootView: View
    private var ledger: Ledger? = null
    private var ledgerAction = LedgerAction.CREATE

    private var onNavigationBarListener:
            OnNavigationBarListener.LedgerDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ledger = it.getParcelable(ConstantKeys.LEDGER)
            ledgerAction = it.getSerializable(ConstantKeys.LEDGER_ACTION) as LedgerAction
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_create_ledger_details, container, false)

        if (ledgerAction == LedgerAction.EDIT) {
            rootView.et_identifier.isEnabled = false
            showDataOnViews()
        }

        rootView.et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    til_name.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        rootView.et_identifier.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    til_identifier.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        return rootView
    }


    override fun onSelected() {}

    private fun showDataOnViews() {
        setSpinnerItem(ledger?.type)
        rootView.et_identifier.setText(ledger?.identifier)
        rootView.et_name.setText(ledger?.name)
        rootView.et_description.setText(ledger?.description)
        rootView.cb_show_acc_in_chart.isChecked = ledger?.showAccountsInChart == true
    }

    private fun setSpinnerItem(type: AccountType?) {
        when (type) {
            AccountType.ASSET -> setSpinnerSelection(getString(R.string.asset))
            AccountType.LIABILITY -> setSpinnerSelection(getString(R.string.liability))
            AccountType.EQUITY -> setSpinnerSelection(getString(R.string.equity))
            AccountType.REVENUE -> setSpinnerSelection(getString(R.string.revenue))
            AccountType.EXPENSE -> setSpinnerSelection(getString(R.string.expense))
        }
    }

    private fun setSpinnerSelection(text: String) {
        val accountTypes: Array<String> = resources.getStringArray(R.array.ledger_account_types)
        for ((index, item) in accountTypes.withIndex()) {
            if (text == item) rootView.sp_account_type.setSelection(index)
        }
    }

    private fun validateName(): Boolean {
        if (TextUtils.isEmpty(et_name.text.toString().trim { it <= ' ' })) {
            showTextInputLayoutError(til_name, getString(R.string.required))
            return false
        }
        showTextInputLayoutError(til_name, null)
        return true
    }

    private fun validateIdentifier(): Boolean {
        if (TextUtils.isEmpty(et_identifier.text.toString().trim { it <= ' ' })) {
            showTextInputLayoutError(til_identifier, getString(R.string.required))
            return false
        }
        showTextInputLayoutError(til_identifier, null)
        return true
    }

    override fun verifyStep(): VerificationError? {
        if (!validateIdentifier() || !validateName()) {
            return VerificationError(null)
        } else {
            onNavigationBarListener?.setLedgerDetails(
                    sp_account_type.selectedItem.toString(),
                    et_identifier.text.toString(),
                    et_name.text.toString(),
                    et_description.text.toString(),
                    cb_show_acc_in_chart.isChecked
            )
        }
        return null
    }

    override fun onError(error: VerificationError) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNavigationBarListener.LedgerDetails) {
            onNavigationBarListener = context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnNavigationBarListener.LedgerDetails")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(ledger: Ledger?, ledgerAction: LedgerAction) =
                CreateLedgerDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ConstantKeys.LEDGER_ACTION, ledgerAction)
                        putParcelable(ConstantKeys.LEDGER, ledger)
                    }
                }
    }
}
