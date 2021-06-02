package org.apache.fineract.ui.online.geo_location.visited_customer_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_visted_customers_list.view.*
import org.apache.fineract.R
import org.apache.fineract.ui.adapters.VisitedClientLocationAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.geo_location.visit_customer.VisitCustomersActivity
import javax.inject.Inject

class VisitedClientLocationListFragment : FineractBaseFragment() {

    private lateinit var rootView: View
    private lateinit var viewModel: VisitedClientLocationViewModel

    @Inject
    lateinit var factory: VisitedClientLocationViewModelFactory

    @Inject
    lateinit var adapter: VisitedClientLocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.visited_customers))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_visted_customers_list, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(VisitedClientLocationViewModel::class.java)
        rootView.rvVisitedClient.adapter = adapter

        rootView.btnVisitCustomer?.setOnClickListener {
            startActivity(Intent(activity, VisitCustomersActivity::class.java))
        }

        subscribeUI()
        return rootView
    }

    private fun subscribeUI() {
        viewModel.getVisitedClientLocationList()
        viewModel.locationList.observe(this, Observer {
            it?.let { list ->
                adapter.submitList(list)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                VisitedClientLocationListFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}