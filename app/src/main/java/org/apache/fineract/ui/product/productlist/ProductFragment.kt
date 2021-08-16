package org.apache.fineract.ui.product.productlist

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
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.adapters.ProductAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.ui.product.productdetails.ProductDetailsActivity
import org.apache.fineract.ui.product.viewmodel.ProductViewModel
import org.apache.fineract.ui.product.viewmodel.ProductViewModelFactory
import org.apache.fineract.utils.Constants
import javax.inject.Inject
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import butterknife.OnClick
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.creategroup.CreateGroupActivity
import org.apache.fineract.ui.product.ProductAction
import org.apache.fineract.ui.product.createproduct.CreateProductActivity

class ProductFragment : FineractBaseFragment(), OnItemClickListener {

    lateinit var rootView: View

    lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var productAdapter: ProductAdapter

    @Inject
    lateinit var productViewModelFactory: ProductViewModelFactory

    lateinit var productList: ArrayList<Product>

    companion object {
        fun newInstance() = ProductFragment().apply {
            val args = Bundle()
            arguments = args
        }
    }

    val searchedProduct: (ArrayList<Product>) -> Unit = { products ->
        productAdapter.setProductsList(products)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_product, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        initializeFineractUIErrorHandler(activity, rootView)
        viewModel =
            ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel::class.java)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, rootView)
        productAdapter.setItemClickListener(this)

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            viewModel.getProducts()
        }

        rvProduct.adapter = productAdapter
        rvProduct.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProducts()?.observe(this, Observer {
            it?.let {
                productList = it
                productAdapter.setProductsList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_product_search, menu)
        setUpSearchInterface(menu)
    }

    private fun setUpSearchInterface(menu: Menu?) {

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.product_search)?.actionView as? SearchView

        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchProducts(productList, query, searchedProduct)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    productAdapter.setProductsList(productList)
                }
                viewModel.searchProducts(productList, newText, searchedProduct)
                return true
            }
        })
    }

    override fun onItemClick(childView: View?, position: Int) {
        val intent = Intent(context, ProductDetailsActivity::class.java).apply {
            putExtra(Constants.PRODUCT, productList[position])
        }
        startActivity(intent)

    }

    override fun onItemLongPress(childView: View?, position: Int) {}

    @OnClick(R.id.fabAddProduct)
    fun addGroup() {
        val intent = Intent(activity, CreateProductActivity::class.java).apply {
            putExtra(Constants.PRODUCT_ACTION, ProductAction.CREATE)
        }
        startActivity(intent)
    }
}
