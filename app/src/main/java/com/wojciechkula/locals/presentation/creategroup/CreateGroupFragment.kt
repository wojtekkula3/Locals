package com.wojciechkula.locals.presentation.creategroup

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.wojciechkula.locals.R
import com.wojciechkula.locals.databinding.FragmentCreateGroupBinding
import com.wojciechkula.locals.domain.model.LocationModel
import com.wojciechkula.locals.navigation.CreateGroupNavigator
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.common.SharedViewState
import com.wojciechkula.locals.presentation.creategroup.list.HobbyItem
import com.wojciechkula.locals.presentation.creategroup.list.HobbyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

@AndroidEntryPoint
internal class CreateGroupFragment : Fragment() {

    private var _binding: FragmentCreateGroupBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: CreateGroupViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: CreateGroupNavigator

    private val name get() = binding.nameInput.text.toString()
    private val description get() = binding.descriptionInput.text.toString()
    private val hobbiesSize get() = binding.selectedHobbiesChipGroup.size
    private val searchInputText get() = binding.searchInput.text.toString().lowercase()
    private var selectedLocation: LocationModel? = null
    private val groupCreatedMessage = getString(R.string.create_group_group_created_latest_message)

    private val adapter: HobbyListAdapter by lazy {
        HobbyListAdapter { name -> viewModel.selectHobby(name) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getResultFromMap()
    }

    private fun getResultFromMap() {
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            with(binding)
            {
                selectedLocation = bundle.getParcelable("selectedLocation")!!
                placeStatusLabel.text = getString(R.string.create_group_location_established)
                placeStatusLabel.setTextColor(ContextCompat.getColor(context!!, R.color.green_600))
                createGroupLayout.smoothScrollTo(0, createGroupLayout.getChildAt(0).height)
                viewModel.showChipsWithHobbies()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateGroupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        viewModel.setLocation(selectedLocation)
    }

    private fun initViews() {
        with(binding) {
            searchLayout.isEndIconVisible = false
            hobbiesRecyclerView.adapter = adapter
            backButton.setOnClickListener { findNavController().popBackStack() }

            nameInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onNameChange(name, description, hobbiesSize, selectedLocation) }
                .launchIn(lifecycleScope)

            descriptionInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onDescriptionChange(name, description, hobbiesSize, selectedLocation) }
                .launchIn(lifecycleScope)

            searchInput.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    searchResultsLayout.visibility = View.INVISIBLE
                }
            }

            searchInput.addTextChangedListener { text ->
                searchLayout.isEndIconVisible = text.toString().isNotBlank()
                viewModel.onSearchChange(text.toString())
                createGroupLayout.smoothScrollTo(0, 630)
            }

            searchLayout.setEndIconOnClickListener {
                viewModel.selectHobby(searchInputText)
            }

            selectPlaceButton.setOnClickListener {
                navigator.openMap(findNavController())
            }

            placeStatusLabel.setOnClickListener {
                navigator.openMap(findNavController())
            }

            nextButton.setOnClickListener {
                viewModel.onNextClick(name, description, selectedLocation, groupCreatedMessage)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        sharedViewModel.viewState.observe(viewLifecycleOwner, ::bindSharedState)
    }

    private fun bindSharedState(state: SharedViewState) {
        viewModel.setUser(state.user)
    }

    private fun bindState(state: CreateGroupViewState) {
        adapter.submitList(state.searchHobbies)
        if (state.searchHobbies.isNotEmpty()) {
            binding.searchResultsLayout.visibility = View.VISIBLE
            binding.hobbiesRecyclerView.smoothScrollToPosition(0)
        } else {
            binding.searchResultsLayout.visibility = View.INVISIBLE
        }

        binding.nextButton.isEnabled = state.nextActionEnabled
    }

    private fun handleEvents(event: CreateGroupViewEvent) {
        when (event) {
            is CreateGroupViewEvent.ShowChipsWithSelectedHobbies -> showChips(event.selectedHobbies)
            is CreateGroupViewEvent.SetLocation -> viewModel.onLocationChange(
                name,
                description,
                hobbiesSize,
                event.location
            )
            is CreateGroupViewEvent.OpenDialog -> openDialog(event.newHobbies)
            CreateGroupViewEvent.OpenMyGroups -> openMyGroups()
        }
    }

    private fun openMyGroups() {
        navigator.openMyGroups(findNavController())
    }

    private fun showChips(hobbies: ArrayList<HobbyItem>?) {
        binding.selectedHobbiesChipGroup.removeAllViews()
        if (hobbies?.isNotEmpty() == true)
            for (hobby in hobbies) {
                binding.selectedHobbiesChipGroup.addView(createChip(hobby, true))
                binding.searchInput.text = null
            }
        viewModel.onHobbiesChange(name, description, hobbiesSize, selectedLocation)
    }

    private fun openDialog(newHobbies: ArrayList<HobbyItem>) {
        val hobbiesString = newHobbies.joinToString(", ") { hobbyItem -> hobbyItem.name }

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage(getString(R.string.create_group_new_hobbies_alert_dialog, hobbiesString))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.common_confirm)) { dialog, id ->
                viewModel.createGroup(name, description, selectedLocation, groupCreatedMessage)
            }
            .setNegativeButton(getString(R.string.common_decline)) { dialog, id ->
                dialog.cancel()
            }

        val alert = dialog.create()
        alert.setTitle(getString(R.string.create_group_confirm_to_create_group))
        alert.show()
    }

    private fun createChip(hobby: HobbyItem, isChecked: Boolean): Chip {
        val chip = Chip(context)
        val drawable =
            ChipDrawable.createFromAttributes(layoutInflater.context, null, 0, R.style.Widget_Locals_SearchChip)
        chip.setChipDrawable(drawable)
        chip.text = hobby.name
        chip.isChecked = isChecked
        chip.isCheckedIconVisible = false
        chip.isCloseIconVisible = isChecked

        chip.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                viewModel.removeHobby(hobby.name)
                chip.visibility = View.GONE
            }
        }
        return chip
    }
}