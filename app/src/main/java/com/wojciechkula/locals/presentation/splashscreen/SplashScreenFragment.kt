package com.wojciechkula.locals.presentation.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.databinding.FragmentSplashScreenBinding
import com.wojciechkula.locals.navigation.SplashScreenNavigator
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenViewEvent.*

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashScreenFragment : Fragment() {

    @Inject
    lateinit var navigator: SplashScreenNavigator

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding
        get() = _binding!!

    val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() {
        binding.button.setOnClickListener { navigator.openLogin(findNavController()) }
    }

    private fun observeViewModel() {
        viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                OpenLogin -> onOpenLogin()
                OpenDashboard -> onOpenDashboard()
            }
        }
    }

    private fun onOpenLogin() {
        navigator.openLogin(findNavController())
    }

    private fun onOpenDashboard() {
        navigator.openDashboard(findNavController())
    }

}