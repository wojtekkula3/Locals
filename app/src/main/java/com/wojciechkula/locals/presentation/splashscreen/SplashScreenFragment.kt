package com.wojciechkula.locals.presentation.splashscreen

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.R
import com.wojciechkula.locals.databinding.FragmentSplashScreenBinding
import com.wojciechkula.locals.navigation.SplashScreenNavigator
import com.wojciechkula.locals.presentation.common.SharedViewEvent
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenViewEvent.GetGroupsForExplore
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenViewEvent.OpenLogin
import com.wojciechkula.locals.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashScreenFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val REQUIRE_CODE_LOCATION_PERMISSION = 0

    @Inject
    lateinit var navigator: SplashScreenNavigator

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SplashScreenViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        sharedViewModel.viewEvent.observe(viewLifecycleOwner, ::handleSharedEvents)
    }

    private fun handleEvents(event: SplashScreenViewEvent) {
        when (event) {
            OpenLogin -> onOpenLogin()
            SplashScreenViewEvent.CheckLocationPermissions -> checkLocationPermissions()
            GetGroupsForExplore -> onGetGroupsForExplore()
        }
    }

    private fun handleSharedEvents(event: SharedViewEvent) {
        when (event) {
            is SharedViewEvent.OpenDashboard -> onOpenDashboard()
        }
    }

    private fun onOpenLogin() {
        navigator.openLogin(findNavController())
    }

    private fun checkLocationPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            viewModel.getGroupsForExplore()
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
        viewModel.getGroupsForExplore()
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

}