package org.apache.fineract.ui.online.accounting.accounts

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.adapters.AccountsAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import java.util.*
import javax.inject.Inject


class AccountsFragment : FineractBaseFragment(), AccountContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var accountsPresenter: AccountsPresenter

    @Inject
    lateinit var accountsAdapter: AccountsAdapter

    lateinit var accountList : List<Account>

    companion object {
        fun newInstance() = AccountsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        accountList= ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_accounts, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        accountsPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            accountsPresenter.getAccountsPage()
        }

        accountsPresenter.getAccountsPage()
    }

    override fun showUserInterface() {
        setToolbarTitle(getString(R.string.accounts))
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvAccount.layoutManager = layoutManager
        rvAccount.setHasFixedSize(true)

        rvAccount.adapter = accountsAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_account_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.account_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                accountsPresenter.searchAccount(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    accountsAdapter.setAccountsList(accountList)
                }

                return false
            }
        })

    }

    override fun searchedAccount(account: Account) {
        showRecyclerView(true)
        accountsAdapter.setAccountsList(Collections.singletonList(account))
    }

    override fun onRefresh() {
        accountsPresenter.getAccountsPage()
    }


    override fun showAccounts(accounts: List<Account>) {
        showRecyclerView(true)
        accountList = accounts
        accountsAdapter.setAccountsList(accountList)
    }

    override fun showEmptyAccounts() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.accounts), getString(R.string.accounts),
                R.drawable.ic_person_outline_black_24dp)
    }

    override fun showRecyclerView(status: Boolean) {
        if (status) {
            rvAccount.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvAccount.visibility = View.GONE
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
        showFineractErrorUI(getString(R.string.accounts))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountsPresenter.detachView()
    }

}
