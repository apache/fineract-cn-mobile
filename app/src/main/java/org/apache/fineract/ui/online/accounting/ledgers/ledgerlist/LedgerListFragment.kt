package org.apache.fineract.ui.online.accounting.ledgers.ledgerlist


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_ledger_list.*
import kotlinx.android.synthetic.main.fragment_ledger_list.view.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.LedgerListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.accounting.accounts.LedgerListContract
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity.CreateLedgerActivity
import org.apache.fineract.ui.online.accounting.ledgers.ledgerdetails.LedgerDetailsActivity
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject


class LedgerListFragment : FineractBaseFragment(), LedgerListContract.View,
        SwipeRefreshLayout.OnRefreshListener, LedgerListAdapter.OnItemClickListener {

    @Inject
    lateinit var ledgerListAdapter: LedgerListAdapter

    @Inject
    lateinit var ledgerListPresenter: LedgerListPresenter

    lateinit var ledgerList: List<Ledger>

    companion object {
        fun newInstance(): LedgerListFragment = LedgerListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        ledgerList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_ledger_list, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ledgerListPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)
        rootView.fab_create_ledger.setOnClickListener {
            val intent = Intent(activity, CreateLedgerActivity::class.java).apply {
                putExtra(ConstantKeys.LEDGER_ACTION, LedgerAction.CREATE)
            }
            startActivity(intent)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            ledgerListPresenter.getLedgersPage()
        }

        ledgerListPresenter.getLedgersPage()
        ledgerListAdapter.setItemClickListener(this)
    }

    override fun showUserInterface() {

        setToolbarTitle(getString(R.string.ledger))
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        rvLedger.layoutManager = layoutManager
        rvLedger.setHasFixedSize(true)

        rvLedger.adapter = ledgerListAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        ledgerListPresenter.getLedgersPage()
    }

    override fun showLedgers(ledgers: List<Ledger>) {
        showRecyclerView(true)
        this.ledgerList = ledgers
        ledgerListAdapter.setLedgerList(ledgers)
    }

    override fun showEmptyLedgers() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.ledger), getString(R.string.ledger),
                R.drawable.ic_person_outline_black_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_ledger_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.ledger_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ledgerListPresenter.searchLedger(ledgerList, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    ledgerListAdapter.setLedgerList(ledgerList)
                }
                ledgerListPresenter.searchLedger(ledgerList, newText)
                return false
            }
        })

    }

    override fun searchedLedger(ledgers: List<Ledger>) {
        showRecyclerView(true)
        ledgerListAdapter.setLedgerList(ledgers)
    }

    override fun showRecyclerView(status: Boolean) {
        if (status) {
            rvLedger.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvLedger.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }

    override fun showProgressbar() {
        swipeContainer.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeContainer.isRefreshing = false
    }

    override fun showNoInternetConnection() {
        showRecyclerView(false)
        showFineractNoInternetUI()
    }

    override fun showError(message: String) {
        showRecyclerView(false)
        showFineractErrorUI(getString(R.string.ledger))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ledgerListPresenter.detachView()
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, LedgerDetailsActivity::class.java).apply {
            putExtra(ConstantKeys.LEDGER, ledgerList[position])
            putExtra(ConstantKeys.LEDGER_ACTION, LedgerAction.EDIT)
        }
        startActivity(intent)
    }
}
