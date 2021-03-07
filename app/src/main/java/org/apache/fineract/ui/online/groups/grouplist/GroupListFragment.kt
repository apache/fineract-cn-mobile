package org.apache.fineract.ui.online.groups.grouplist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_group_list.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.adapters.GroupsAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.creategroup.CreateGroupActivity
import org.apache.fineract.ui.online.groups.groupdetails.GroupDetailsActivity
import org.apache.fineract.utils.Constants
import javax.inject.Inject


/*
 * Created by saksham on 15/June/2019
*/

class GroupListFragment : FineractBaseFragment(), OnItemClickListener {

    lateinit var rootView: View

    lateinit var viewModel: GroupViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    @Inject
    lateinit var groupViewModelFactory: GroupViewModelFactory

    lateinit var groupList: ArrayList<Group>

    val searchedGroup: (ArrayList<Group>) -> Unit = { groups ->
        adapter.setGroupList(groups)
    }

    companion object {
        fun newInstance(): GroupListFragment {
            return GroupListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_group_list, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this,
                groupViewModelFactory).get(GroupViewModel::class.java)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, rootView)
        adapter.setItemClickListener(this)

        rvGroups.adapter = adapter
        rvGroups.layoutManager = LinearLayoutManager(context)
        viewModel.getGroups().observe(this,

                Observer {
                    groupList = it
                    adapter.setGroupList(it)
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_group_search, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.group_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchGroup(groupList, query, searchedGroup)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    adapter.setGroupList(groupList)
                }
                viewModel.searchGroup(groupList, newText, searchedGroup)
                return true
            }
        })
    }

    override fun onItemClick(childView: View?, position: Int) {
        val intent = Intent(context, GroupDetailsActivity::class.java).apply {
            putExtra(Constants.GROUP, groupList[position])
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

    override fun onItemLongPress(childView: View?, position: Int) {
    }

    @OnClick(R.id.fabAddGroup)
    fun addGroup() {
        val intent = Intent(activity, CreateGroupActivity::class.java).apply {
            putExtra(Constants.GROUP_ACTION, GroupAction.CREATE)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

}