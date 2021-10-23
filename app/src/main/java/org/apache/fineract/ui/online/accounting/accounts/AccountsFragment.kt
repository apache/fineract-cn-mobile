package org.apache.fineract.ui.online.accounting.accounts

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.text.TextUtils
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import androidx.lifecycle.Observer
import butterknife.OnClick
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.adapters.AccountsAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.ui.online.accounting.accounts.accountDetails.AccountDetailsActivity
import org.apache.fineract.ui.online.accounting.accounts.createaccount.CreateAccountActivity
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModel
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModelFactory
import org.apache.fineract.utils.Constants
import javax.inject.Inject
import kotlin.collections.ArrayList


class AccountsFragment : FineractBaseFragment(), OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var rootView: View

    lateinit var accountsViewModel: AccountsViewModel

    @Inject
    lateinit var accountsViewModelFactory: AccountsViewModelFactory

    @Inject
    lateinit var accountsAdapter: AccountsAdapter

    lateinit var accountList : List<Account>

    val searchedAccount: (ArrayList<Account>) -> Unit = { accounts ->
        accountsAdapter.setAccountsList(accounts)
    }

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
        accountsViewModel = ViewModelProviders.of(this, accountsViewModelFactory).get(AccountsViewModel::class.java)
        ButterKnife.bind(this, rootView)
        return rootView
    }


    override fun onStart() {
        super.onStart()
        accountsViewModel.getAccounts()?.observe(this, Observer {
            it?.let {
                accountList = it
                accountsAdapter.setAccountsList(it)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountsAdapter.setItemClickListener(this)
        showUserInterface()

        btn_try_again.setOnClickListener {
            showProgressbar()
            layoutError.visibility = View.GONE
            accountsViewModel.getAccounts()
            hideProgressbar()
        }
    }

    fun showUserInterface() {
        setToolbarTitle(getString(R.string.accounts))
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        rvAccount.layoutManager = layoutManager
        rvAccount.setHasFixedSize(true)

        rvAccount.adapter = accountsAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.account_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                accountsViewModel.searchAccount(accountList as ArrayList<Account>, query, searchedAccount)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    accountsAdapter.setAccountsList(accountList)
                }
                accountsViewModel.searchAccount(accountList as ArrayList<Account>, newText, searchedAccount)
                return false
            }
        })

    }

    fun searchedAccount(accounts: List<Account>) {
        showRecyclerView(true)
        accountsAdapter.setAccountsList(accounts)
    }

    override fun onRefresh() {
        showProgressbar()
        accountsViewModel.getAccounts()?.observe(this, Observer {
            it?.let {
                accountList = it
                accountsAdapter.setAccountsList(it)
            }
        })
        hideProgressbar()
    }


    fun showAccounts(accounts: List<Account>) {
        showRecyclerView(true)
        accountList = accounts
        accountsAdapter.setAccountsList(accountList)
    }

    fun showEmptyAccounts() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.account), getString(R.string.account),
            R.drawable.ic_person_outline_black_24dp)
    }

    fun showRecyclerView(status: Boolean) {
        if (status) {
            rvAccount.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvAccount.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }

    fun showProgressbar() {
        swipeContainer.isRefreshing = true
    }

    fun hideProgressbar() {
        swipeContainer.isRefreshing = false
    }

    fun showNoInternetConnection() {
        showRecyclerView(false)
        showFineractNoInternetUI()
    }

    fun showError(message: String) {
        showRecyclerView(false)
        showFineractErrorUI(getString(R.string.accounts))
    }

    override fun onItemClick(childView: View?, position: Int) {
        val intent = Intent(context, AccountDetailsActivity::class.java).apply {
            putExtra(Constants.ACCOUNT, accountList[position])
        }
        startActivity(intent)
    }

    override fun onItemLongPress(childView: View?, position: Int) {}

    @OnClick(R.id.fabAddAccount)
    fun addAccount() {
        val intent = Intent(activity, CreateAccountActivity::class.java).apply {
            putExtra(Constants.ACCOUNT_ACTION, AccountAction.CREATE)
        }
        startActivity(intent)
    }
}
