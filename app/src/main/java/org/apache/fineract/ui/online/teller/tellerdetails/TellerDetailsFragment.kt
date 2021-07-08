package org.apache.fineract.ui.online.teller.tellerdetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_teller_details.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.online.teller.TellerAction
import org.apache.fineract.ui.online.teller.createteller.CreateTellerActivity
import org.apache.fineract.ui.online.teller.tellertasks.TellerTasksBottomSheetFragment
import org.apache.fineract.ui.views.CircularImageView
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.Utils

class TellerDetailsFragment : FineractBaseFragment() {

    lateinit var rootView: View
    lateinit var teller: Teller

    companion object {
        fun newInstance(teller: Teller) : TellerDetailsFragment{
            val fragment = TellerDetailsFragment()
            val args = Bundle()
            args.putParcelable(Constants.TELLER, teller)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teller = arguments?.get(Constants.TELLER) as Teller
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_teller_details, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        setToolbarTitle(teller.tellerAccountIdentifier)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tellerDetailsCode.text = teller.code
        tellerDetailsPassword.text = teller.password
        setTellerStatusIcon(teller.state, tellerDetailsCiv)
        tellerDetailsState.text = teller.state.toString()
        tellerDetailsAccountIdentifier.text = teller.tellerAccountIdentifier
        tellerDetailsVaultIdentifier.text = teller.vaultAccountIdentifier
        tellerCra.text = teller.chequesReceivableAccount
        tellerDetailsCashdrawLimit.text = teller.cashdrawLimit.toString()
        tellerDetailsCosAccount.text = teller.cashOverShortAccount
        tellerDetailsAssignedEmployee.text = teller.assignedEmployee
        tellerDetailsDenominationReq.text = if (teller.denominationRequired) "YES" else "NO"

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Utils.setToolbarIconColor(context, menu, R.color.white)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_teller_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuEditTeller -> {
                val intent = Intent(activity, CreateTellerActivity::class.java).apply {
                    putExtra(Constants.TELLER, teller)
                    putExtra(Constants.TELLER_ACTION, TellerAction.EDIT)
                }
                startActivity(intent)
                activity!!.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.tellerDetailsTasks)
    fun onTasksCardViewClicked() {
        val bottomSheet = TellerTasksBottomSheetFragment(teller)
        bottomSheet.show(childFragmentManager, getString(R.string.tasks))
    }

    private fun setTellerStatusIcon(status: Teller.State?, imageView: CircularImageView) {
        when (status) {
            Teller.State.OPEN -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.blue, context))
            }
            Teller.State.PAUSED -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.light_yellow, context))
            }
            Teller.State.ACTIVE -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.deposit_green, context))
            }
            Teller.State.CLOSED -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.red_dark, context))
            }
        }
    }

}