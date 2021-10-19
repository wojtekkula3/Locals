package com.wojciechkula.locals.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentLoginBinding
import com.wojciechkula.locals.navigation.LoginNavigator
import com.wojciechkula.locals.presentation.login.LoginViewEvent.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var navigator: LoginNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding)
        {
            loginButton.setOnClickListener {
                viewModel.onLogInClick(
                    binding.emailInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            }
            registerButton.setOnClickListener { viewModel.onRegisterClick() }
        }
    }

    private fun observeViewModel() {

        viewModel.showLoading.observe(viewLifecycleOwner) { isLoading ->
            LoadingDialogFragment.toggle(childFragmentManager, isLoading)
        }

        viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                OpenDashboard -> openDashboard()
                OpenForgotPassword -> TODO()
                OpenRegister -> openRegister()
            }
        }
    }

    private fun openDashboard() {
        navigator.openDashboard(findNavController())
    }

    private fun openRegister() {
        navigator.openRegister(findNavController())
    }

}