package com.wojciechkula.locals.presentation.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.databinding.FragmentSplashScreenBinding
import com.wojciechkula.locals.navigation.SplashScreenNavigator
import com.wojciechkula.locals.presentation.common.SharedViewEvent
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenViewEvent.GetGroupsForExplore
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenViewEvent.OpenLogin
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashScreenFragment : Fragment() {

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

    private fun onGetGroupsForExplore() {
        sharedViewModel.getGroupsForExplore()
    }

    private fun onOpenDashboard() {
        navigator.openDashboard(findNavController())
    }

}