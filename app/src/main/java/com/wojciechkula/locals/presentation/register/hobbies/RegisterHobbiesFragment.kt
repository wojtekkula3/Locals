package com.wojciechkula.locals.presentation.register.hobbies

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentRegisterHobbiesBinding
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.extension.showSnackbarError
import com.wojciechkula.locals.navigation.RegisterHobbiesNavigator
import com.wojciechkula.locals.presentation.common.SharedViewEvent
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

@AndroidEntryPoint
internal class RegisterHobbiesFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val REQUIRE_CODE_LOCATION_PERMISSION = 0

    @Inject
    lateinit var navigator: RegisterHobbiesNavigator
    private val viewModel: RegisterHobbiesViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentRegisterHobbiesBinding? = null
    private val binding
        get() = _binding!!

    private val args: RegisterHobbiesFragmentArgs by navArgs()

    private val searchInput get() = binding.searchInput.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterHobbiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        with(binding) {
            searchInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { onSearchInput() }
                .launchIn(lifecycleScope)

            nextButton.setOnClickListener {
                viewModel.onNextCLick()
            }
            backButton.setOnClickListener { findNavController().popBackStack() }

        }
    }

    private fun onSearchInput() {
        binding.searchHobbiesChipGroup.removeAllViews()
        if (!binding.searchInput.text.isNullOrEmpty())
            viewModel.onSearchChange(this@RegisterHobbiesFragment.searchInput)
        else
            viewModel.onSearchChangeEmptyString()
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
        sharedViewModel.viewEvent.observe(viewLifecycleOwner, ::handleSharedEvents)
    }

    private fun bindState(state: RegisterHobbiesViewState) {
        with(binding) {
            nextButton.isEnabled = state.registerActionEnabled

            if (!state.selectedHobbiesList.isNullOrEmpty()) {
                selectedHobbiesChipGroup.removeAllViews()
                for (hobby in state.selectedHobbiesList) {
                    selectedHobbiesChipGroup.addView(createChip(hobby, true))
                }
            }
        }
    }

    private fun handleEvents(event: RegisterHobbiesViewEvent) {
        when (event) {
            is RegisterHobbiesViewEvent.GetGeneralHobbies -> getGeneralHobbies(event)
            is RegisterHobbiesViewEvent.GetCustomHobbies -> getCustomHobbies(event)
            RegisterHobbiesViewEvent.CheckLocationPermissions -> checkLocationPermissions()
            RegisterHobbiesViewEvent.GetGroupsForExplore -> onGetGroupsForExplore()
            is RegisterHobbiesViewEvent.ShowError -> onError(event.exception)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun handleSharedEvents(event: SharedViewEvent) {
        when (event) {
            is SharedViewEvent.OpenDashboard -> onOpenDashboard()
        }
    }

    private fun getGeneralHobbies(event: RegisterHobbiesViewEvent.GetGeneralHobbies) {
        lifecycleScope.launch {
            if (event.generalHobbiesList != null) {
                binding.searchHobbiesChipGroup.removeAllViews()
                for (hobby in event.generalHobbiesList) {
                    binding.searchHobbiesChipGroup.addView(createChip(hobby, false))
                }

                val chipGroup = binding.searchHobbiesChipGroup.children
                    .map { it as Chip }

                if (event.selectedHobbiesList != null) {
                    for (chip in chipGroup) {
                        for (hobby in event.selectedHobbiesList) {
                            if (chip.text == hobby.name)
                                chip.visibility = View.GONE
                        }
                    }
                }
            } else {
                binding.searchHobbiesChipGroup.removeAllViews()
            }
        }
    }

    private fun getCustomHobbies(event: RegisterHobbiesViewEvent.GetCustomHobbies) {
        lifecycleScope.launch {
            if (event.customHobbiesList != null) {

                binding.searchHobbiesChipGroup.removeAllViews()
                for (hobby in event.customHobbiesList) {
                    binding.searchHobbiesChipGroup.addView(createChip(hobby, false))

                }

                val chipGroup = binding.searchHobbiesChipGroup.children
                    .map { it as Chip }

                if (event.selectedHobbiesList != null) {
                    for (chip in chipGroup) {
                        for (hobby in event.selectedHobbiesList) {
                            if (chip.text == hobby.name)
                                chip.visibility = View.GONE
                        }
                    }
                }
            } else {
                binding.searchHobbiesChipGroup.removeAllViews()
            }
        }
    }

    private fun onError(exception: Exception) {
        binding.showSnackbarError(exception.message.toString())
    }

    private fun checkLocationPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            viewModel.onPermissionChecked(args)
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else
            checkLocationPermissions()
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {
        viewModel.onPermissionChecked(args)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun onGetGroupsForExplore() {
        sharedViewModel.getGroupsForExplore()
    }

    private fun onOpenDashboard() {
        navigator.openDashboard(findNavController())
    }

    private fun createChip(hobby: HobbyModel, isChecked: Boolean): Chip {
        val chip = Chip(context)
        val drawable =
            ChipDrawable.createFromAttributes(layoutInflater.context, null, 0, R.style.Widget_Locals_RegisterChip)
        chip.setChipDrawable(drawable)
        chip.text = hobby.name
        chip.isChecked = isChecked

        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addHobby(hobby)
                chip.visibility = View.GONE
            } else {
                viewModel.removeHobby(hobby)
                chip.visibility = View.GONE
            }
        }
        return chip
    }
}