package org.apache.fineract.ui.product

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.text.TextUtils
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product.layoutError
import kotlinx.android.synthetic.main.fragment_product.swipeContainer
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.adapters.ProductAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import java.util.*
import javax.inject.Inject


class ProductFragment : FineractBaseFragment(), ProductContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var productPresenter: ProductPresenter

    @Inject
    lateinit var productAdapter: ProductAdapter

    lateinit var productList: List<Product>

    private var searchView: SearchView? = null

    companion object {
        fun newInstance() = ProductFragment().apply {
            val args = Bundle()
            arguments = args
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        productList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_product, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        productPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserInterface()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            productPresenter.getProductsPage()
        }

        productPresenter.getProductsPage()
    }

    override fun showUserInterface() {

        setToolbarTitle(getString(R.string.products))
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        rvProduct.layoutManager = layoutManager
        rvProduct.setHasFixedSize(true)
        rvProduct.adapter = productAdapter

        swipeContainer.setColorSchemeColors(*activity!!
                .resources.getIntArray(R.array.swipeRefreshColors))
        swipeContainer.setOnRefreshListener(this)

    }

    override fun onRefresh() {
        if (searchView!!.query.toString().isEmpty()) {
            productPresenter.getProductsPage()
        } else {
            productPresenter.searchProduct(productList, searchView!!.query.toString())
        }
        swipeContainer.isRefreshing = !swipeContainer.isRefreshing
    }

    override fun showProduct(products: List<Product>) {
        showRecyclerView(true)
        this.productList = products
        productAdapter.setProductsList(products)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_product_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        searchView = menu?.findItem(R.id.product_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                productPresenter.searchProduct(productList, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    showRecyclerView(true)
                    productAdapter.setProductsList(productList)

                } else {
                    productPresenter.searchProduct(productList, newText)
                }

                return false
            }
        })

    }

    override fun showEmptyProduct() {
        showRecyclerView(false)
        showFineractEmptyUI(getString(R.string.products), getString(R.string.products),
                R.drawable.ic_person_outline_black_24dp)
    }

    override fun showRecyclerView(status: Boolean) {
        if (status) {
            rvProduct.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        } else {
            rvProduct.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }

    override fun showProgressbar() {
        swipeContainer.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeContainer.isRefreshing = false
    }

    override fun searchedProduct(products: List<Product>) {
        //showRecyclerView(true)
        productAdapter.setProductsList(products)
    }

    override fun showNoInternetConnection() {
        showRecyclerView(false)
        showNoInternetConnection()
    }

    override fun showError(message: String) {
        showRecyclerView(false)
        showFineractErrorUI(getString(R.string.products))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productPresenter.detachView()
    }

}
