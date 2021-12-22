package com.wojciechkula.locals.presentation.explore

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentExploreBinding
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.navigation.ExploreNavigator
import com.wojciechkula.locals.presentation.common.SharedViewEvent
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.explore.ExploreViewEvent.*
import com.wojciechkula.locals.presentation.explore.list.ExploreItem
import com.wojciechkula.locals.presentation.explore.list.ExploreListAdapter
import com.wojciechkula.locals.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
internal class ExploreFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val REQUIRE_CODE_LOCATION_PERMISSION = 0

    @Inject
    lateinit var navigator: ExploreNavigator

    private var _binding: FragmentExploreBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ExploreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val exploreAdapter by lazy {
        ExploreListAdapter { groupId -> viewModel.joinTheGroup(groupId) }
    }

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.explore_toolbar_menu, menu)
        val item = menu.findItem(R.id.search)
        searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                if (searchText.isNullOrEmpty()) {
                    binding.searchHobbiesCardView.visibility = View.GONE
                } else {
                    binding.searchHobbiesCardView.visibility = View.VISIBLE
                    viewModel.searchHobbies(searchText)
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideLoading()
        initViews()
        requestPermissions()
        observeViewModel()
    }

    private fun hideLoading() {
        LoadingDialogFragment.toggle(childFragmentManager, false)
    }

    private fun initViews() {
        with(binding) {
            toolbar.setupWithNavController(findNavController())
            groupsRecyclerView.adapter = exploreAdapter
            groupsRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                    viewModel.onDistanceChange(value)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    getGroupsByDistanceAndHobbies()
                }
            })
        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            getGroupsByDistanceAndHobbies()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.explore_you_need_accept_location_permissions),
                REQUIRE_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.explore_you_need_accept_location_permissions),
                REQUIRE_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        sharedViewModel.viewEvent.observe(viewLifecycleOwner, ::handleSharedEvents)
    }

    private fun bindState(state: ExploreViewState) {
        with(binding) {
            if (!state.hasInitGroups) {
                sharedViewModel.setGroupsForExplore()
            }
            if (!state.groupsList.isNullOrEmpty()) {
                emptyListInfoLabel.visibility = View.GONE
                exploreAdapter.submitList(state.groupsList)
            } else {
                emptyListInfoLabel.visibility = View.VISIBLE
                exploreAdapter.submitList(listOf())
            }

            if (!state.selectedHobbiesList.isNullOrEmpty()) {
                selectedHobbiesChipGroup.removeAllViews()
                for (hobby in state.selectedHobbiesList) {
                    selectedHobbiesChipGroup.addView(createChip(hobby, true))
                }
            }
        }
    }

    private fun handleEvents(event: ExploreViewEvent) {
        when (event) {
            ShowGroups -> onShowGroups()
            is ShowSearchHobbies -> onShowSearchHobbies(event)
            ChangeDistance -> onChangeDistance()
            HideJoinedGroup -> onHideJoinedGroup()
        }
    }

    private fun handleSharedEvents(event: SharedViewEvent?) {
        when (event) {
            is SharedViewEvent.SetGroupsForExplore -> setInitialGroups(event.groups)
        }
    }

    private fun setInitialGroups(groups: ArrayList<ExploreItem>?) {
        viewModel.setInitialGroups(groups)
    }

    private fun onShowGroups() {
        getGroupsByDistanceAndHobbies()
    }

    private fun onShowSearchHobbies(event: ShowSearchHobbies) {
        lifecycleScope.launch {
            binding.searchHobbiesChipGroup.removeAllViews()
            if (event.searchHobbies.isNullOrEmpty()) {
                binding.thereIsNoSuchInterestLabel.visibility = View.VISIBLE

            } else {
                binding.thereIsNoSuchInterestLabel.visibility = View.GONE
                for (hobby in event.searchHobbies) {
                    binding.searchHobbiesChipGroup.addView(createChip(hobby, false))
                }

                val chipGroup = binding.searchHobbiesChipGroup.children
                    .map { it as Chip }

                if (event.selectedHobbies != null) {
                    for (chip in chipGroup) {
                        for (hobby in event.selectedHobbies) {
                            if (chip.text == hobby.name)
                                chip.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun onChangeDistance() {
        binding.distanceKMLabel.text = "${binding.distanceSeekBar.progress} km"
    }

    private fun onHideJoinedGroup() {
        getGroupsByDistanceAndHobbies()
    }

    private fun getGroupsByDistanceAndHobbies() {
        viewModel.getGroupsByDistanceAndHobbies()
    }

    private fun createChip(hobby: HobbyModel, isChecked: Boolean): Chip {
        val chip = Chip(context)
        val drawable =
            ChipDrawable.createFromAttributes(layoutInflater.context, null, 0, R.style.Widget_Locals_SearchChip)
        chip.setChipDrawable(drawable)
        chip.text = hobby.name
        chip.isChecked = isChecked
        chip.isCheckedIconVisible = false
        chip.isCloseIconVisible = isChecked

        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addHobby(hobby)
                chip.visibility = View.GONE
                binding.searchHobbiesCardView.visibility = View.GONE
                searchView.setQuery("", false)
                searchView.clearFocus()
            } else {
                viewModel.removeHobby(hobby)
                chip.visibility = View.GONE
            }
        }
        return chip
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else
            requestPermissions()
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {
        getGroupsByDistanceAndHobbies()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}