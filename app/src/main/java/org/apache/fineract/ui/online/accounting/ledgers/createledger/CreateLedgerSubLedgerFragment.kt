package org.apache.fineract.ui.online.accounting.ledgers.createledger


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_create_ledger_sub_ledger.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.SubLedgerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.OnNavigationBarListener
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.MaterialDialog
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
class CreateLedgerSubLedgerFragment : Fragment(), Step, SubLedgerAdapter.OnItemClickListener {

    private lateinit var onNavigationBarListener: OnNavigationBarListener.SubLedger
    private var ledger: Ledger? = null
    private var ledgerAction = LedgerAction.CREATE
    private lateinit var rootView: View
    private var subLedgerList: ArrayList<Ledger> = ArrayList()

    @Inject
    lateinit var subLedgerAdapter: SubLedgerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ledger = it.getParcelable(ConstantKeys.LEDGER)
            ledgerAction = it.getSerializable(ConstantKeys.LEDGER_ACTION) as LedgerAction
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_create_ledger_sub_ledger, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rv_sub_ledgers.adapter = subLedgerAdapter
        subLedgerAdapter.setOnItemClickListener(this)

        if (ledgerAction == LedgerAction.EDIT) {
            showDataOnViews()
        }

        rootView.btn_add_ledger.setOnClickListener {
            showAddSubLedgerBottomSheetDialog(LedgerAction.CREATE, null, 0)
        }
        return rootView
    }

    private fun showAddSubLedgerBottomSheetDialog(
            action: LedgerAction, ledger: Ledger?, position: Int) {

        val bottomSheet = AddSubLedgerBottomSheetDialog(ledger, action, position,
                OnAddSubLedgerBottomSheetListener { act, led, pos ->
                    if (act == LedgerAction.EDIT) {
                        led?.let {
                            subLedgerList[pos] = it
                        }
                    } else {
                        led?.let {
                            subLedgerList.add(it)
                        }
                        showRecyclerView(true)
                    }
                    subLedgerAdapter.submitList(subLedgerList)
                    onNavigationBarListener.setSubLedger(subLedgerList)
                }
        )
        bottomSheet.show(childFragmentManager, getString(R.string.create_sub_ledger))
    }


    private fun showDataOnViews() {
        ledger?.subLedgers?.let {
            if (it.isNotEmpty()) {
                showRecyclerView(true)
                subLedgerList = it as ArrayList<Ledger>
                subLedgerAdapter.submitList(subLedgerList)
            } else {
                showRecyclerView(false)
            }
        }
    }

    private fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rv_sub_ledgers.visibility = View.VISIBLE
            rootView.ll_empty_sub_ledger.visibility = View.GONE
        } else {
            rootView.rv_sub_ledgers.visibility = View.GONE
            rootView.ll_empty_sub_ledger.visibility = View.VISIBLE
        }
    }


    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNavigationBarListener.SubLedger) {
            onNavigationBarListener = context
        } else {
            throw RuntimeException(
                    "You mus implement: OnNavigationBarListener.SubLedger"
            )
        }
    }

    override fun onEditClick(position: Int) {
        showAddSubLedgerBottomSheetDialog(LedgerAction.EDIT, subLedgerList[position], position)
    }

    override fun onDeleteClick(position: Int) {
        MaterialDialog.Builder().apply {
            init(context)
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(getString(
                    R.string.dialog_message_confirmation_delete_ledger))
            setPositiveButton(getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                subLedgerList.removeAt(position)
                if (subLedgerList.size != 0)
                    subLedgerAdapter.submitList(subLedgerList)
                else showRecyclerView(false)
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.cancel))
            createMaterialDialog()
        }.run { show() }
    }

    companion object {
        @JvmStatic
        fun newInstance(ledger: Ledger?, ledgerAction: LedgerAction) =
                CreateLedgerSubLedgerFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ConstantKeys.LEDGER_ACTION, ledgerAction)
                        putParcelable(ConstantKeys.LEDGER, ledger)
                    }
                }
    }
}
