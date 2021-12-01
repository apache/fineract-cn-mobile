package org.apache.fineract.ui.online.teller

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log.e
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_teller.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.adapters.TellerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.ui.online.teller.createteller.CreateTellerActivity
import org.apache.fineract.ui.online.teller.tellerdetails.TellerDetailsActivity
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModel
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModelFactory
import org.apache.fineract.utils.Constants
import javax.inject.Inject


class TellerFragment : FineractBaseFragment(), OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var rootView: View

    lateinit var viewModel: TellerViewModel

    @Inject
    lateinit var tellerAdapter: TellerAdapter

    @Inject
    lateinit var tellerViewModelFactory: TellerViewModelFactory

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

        rootView = inflater.inflate(R.layout.fragment_teller, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this, tellerViewModelFactory).get(TellerViewModel::class.java)
        initializeFineractUIErrorHandler(activity, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, rootView)
        tellerAdapter.setItemClickListener(this)
        showUserInterface()

        btn_try_again.setOnClickListener {
            showProgressbar()
            layoutError.visibility = View.GONE
            viewModel.getTellers()
            hideProgressbar()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTellers()?.observe(this, Observer {
            it?.let {
                tellerList = it
                tellerAdapter.setTellerList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_teller_search, menu)
        setUpSearchInterface(menu)
    }

    fun showUserInterface() {

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

    fun showTellers(tellers: List<Teller>) {
        showRecyclerView(true)
        tellerList = tellers
        tellerAdapter.setTellerList(tellers)
    }

    override fun onRefresh() {
        showProgressbar()
        viewModel.getTellers()?.observe(this, Observer {
            it?.let {
                tellerList = it
                tellerAdapter.setTellerList(it)
            }
        })
        hideProgressbar()
    }

    fun showEmptyTellers() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.teller), getString(R.string.teller),
                R.drawable.ic_person_outline_black_24dp)
    }


    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.teller_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchTeller(tellerList as ArrayList<Teller>, query, searchedTeller)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    tellerAdapter.setTellerList(tellerList)
                }
                viewModel.searchTeller(tellerList as ArrayList<Teller>, newText, searchedTeller)
                return false
            }
        })

    }

    val searchedTeller: (ArrayList<Teller>) -> Unit = { tellers ->
        tellerAdapter.setTellerList(tellers)
    }

    fun showRecyclerView(status: Boolean) {
        if (status) {
            rvTellers.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvTellers.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }

    fun showProgressbar() {
        swipeContainer.isRefreshing = true
    }

    fun hideProgressbar() {
        swipeContainer.isRefreshing = false
    }

    fun showError(message: String) {
        showRecyclerView(false)
        showFineractErrorUI(getString(R.string.teller))
    }

    fun showNoInternetConnection() {
        showRecyclerView(false)
        showFineractNoInternetUI()
    }

    override fun onItemClick(childView: View?, position: Int) {
        val intent = Intent(context, TellerDetailsActivity::class.java).apply {
            putExtra(Constants.TELLER, tellerList[position])
        }
        startActivity(intent)
    }

    override fun onItemLongPress(childView: View?, position: Int) {
    }

    @OnClick(R.id.fabAddTeller)
    fun addTeller() {
        val intent = Intent(activity, CreateTellerActivity::class.java).apply {
            putExtra(Constants.TELLER_ACTION, TellerAction.CREATE)
        }
        startActivity(intent)
    }
}
