package org.apache.fineract.ui.online.teller

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.text.TextUtils
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_teller.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.adapters.TellerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import java.util.*
import javax.inject.Inject


class TellerFragment : FineractBaseFragment(), TellerContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var tellPresenter: TellerPresenter

    @Inject
    lateinit var tellerAdapter: TellerAdapter

    lateinit var tellerList: List<Teller>

    companion object {
        fun newInstance(): TellerFragment = TellerFragment().apply {
            val args = Bundle()
            this.arguments = args
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        tellerList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_teller, container, false)

        (activity as FineractBaseActivity).activityComponent.inject(this)
        tellPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserInterface()
        tellPresenter.fetchTellers()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            tellPresenter.fetchTellers()
        }
    }

    override fun showUserInterface() {

        setToolbarTitle(getString(R.string.teller))
        val llManager = LinearLayoutManager(activity)
        llManager.orientation = RecyclerView.VERTICAL
        rvTellers.layoutManager = llManager
        rvTellers.setHasFixedSize(true)
        rvTellers.adapter = tellerAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)
    }

    override fun showTellers(tellers: List<Teller>) {
        showRecyclerView(true)
        tellerList = tellers
        tellerAdapter.setTellerList(tellers)
    }

    override fun onRefresh() {
        tellPresenter.fetchTellers()
    }

    override fun showEmptyTellers() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.teller), getString(R.string.teller),
                R.drawable.ic_person_outline_black_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_teller_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.teller_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                tellPresenter.searchTeller(tellerList, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    tellerAdapter.setTellerList(tellerList)
                }
                tellPresenter.searchTeller(tellerList, newText)
                return false
            }
        })

        baseSearchView = searchView
    }

    override fun searchedTeller(tellers: List<Teller>) {
        showRecyclerView(true)
        tellerAdapter.setTellerList(tellers)
    }

    override fun showRecyclerView(status: Boolean) {
        if (status) {
            rvTellers.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvTellers.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }

    override fun showProgressbar() {
        swipeContainer.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeContainer.isRefreshing = false
    }

    override fun showError(message: String) {
        showRecyclerView(false)
        showFineractErrorUI(getString(R.string.teller))
    }

    override fun showNoInternetConnection() {
        showRecyclerView(false)
        showFineractNoInternetUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tellPresenter.detachView()
    }
}
