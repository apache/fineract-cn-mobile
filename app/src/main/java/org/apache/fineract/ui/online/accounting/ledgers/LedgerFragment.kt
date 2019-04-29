package org.apache.fineract.ui.online.accounting.ledgers


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import kotlinx.android.synthetic.main.fragment_ledger.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.LedgerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.accounting.accounts.LedgerContract
import javax.inject.Inject
import kotlin.collections.ArrayList


class LedgerFragment : FineractBaseFragment(), LedgerContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var ledgerAdapter: LedgerAdapter

    @Inject
    lateinit var ledgerPresenter: LedgerPresenter

    lateinit var ledgerList: List<Ledger>

    companion object {
        fun newInstance(): LedgerFragment = LedgerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        ledgerList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_ledger, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ledgerPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            ledgerPresenter.getLedgersPage()
        }

        ledgerPresenter.getLedgersPage()
    }

    override fun showUserInterface() {

        setToolbarTitle(getString(R.string.ledger))
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvLedger.layoutManager = layoutManager
        rvLedger.setHasFixedSize(true)

        rvLedger.adapter = ledgerAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        ledgerPresenter.getLedgersPage()
    }

    override fun showLedgers(ledgers: List<Ledger>) {
        showRecyclerView(true)
        this.ledgerList = ledgers
        ledgerAdapter.setLedgerList(ledgers)
    }

    override fun showEmptyLedgers() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.ledger), getString(R.string.ledger),
                R.drawable.ic_person_outline_black_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_ledger_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.ledger_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ledgerPresenter.searchLedger(ledgerList, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    ledgerAdapter.setLedgerList(ledgerList)
                }
                ledgerPresenter.searchLedger(ledgerList, newText)
                return false
            }
        })

    }

    override fun searchedLedger(ledgers: List<Ledger>) {
        showRecyclerView(true)
        ledgerAdapter.setLedgerList(ledgers)
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
        ledgerPresenter.detachView()
    }
}
