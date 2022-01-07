package com.wojciechkula.locals.presentation.mygroups

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentMyGroupsBinding
import com.wojciechkula.locals.navigation.MyGroupsNavigator
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.common.SharedViewState
import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsItem
import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class MyGroupsFragment : Fragment() {

    private var _binding: FragmentMyGroupsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var searchView: SearchView
    private val viewModel: MyGroupsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: MyGroupsNavigator

    private val adapter by lazy {
        MyGroupsListAdapter { selectedGroup -> viewModel.openGroup(selectedGroup) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.explore_toolbar_menu, menu)
        val item = menu.findItem(R.id.search)
        searchView = item?.actionView as SearchView
        searchView.queryHint=getString(R.string.my_groups_enter_name_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                viewModel.searchGroups(searchText)
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                viewModel.searchGroups(searchText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyGroupsBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        with(binding) {
            groupsRecyclerView.adapter = adapter
            groupsRecyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
        sharedViewModel.viewState.observe(viewLifecycleOwner, ::handleSharedState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::bindState)
            }
        }
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)

    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun handleSharedState(state: SharedViewState) {
        viewModel.setUser(state.user)
    }

    private fun bindState(state: MyGroupsViewState) {
        getGroups(state.groups)
    }

    private fun handleEvents(event: MyGroupsViewEvent) {
        when (event) {
            is MyGroupsViewEvent.SearchGroups -> getGroups(event.groups)
            is MyGroupsViewEvent.OpenGroup -> openGroup(event.group, event.userId)
        }
    }

    private fun getGroups(groups: List<MyGroupsItem>) {
        adapter.submitList(groups)
        if (!groups.isNullOrEmpty()) {
            binding.emptyListInfoLabel.visibility = View.GONE
        } else {
            binding.emptyListInfoLabel.visibility = View.VISIBLE
        }
    }

    private fun openGroup(group: MyGroupsItem, userId: String) {
        navigator.openGroup(findNavController(), group.id, group.name, userId)
    }
}