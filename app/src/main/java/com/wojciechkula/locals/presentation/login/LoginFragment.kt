package com.wojciechkula.locals.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentLoginBinding
import com.wojciechkula.locals.extension.showSnackbarError
import com.wojciechkula.locals.navigation.LoginNavigator
import com.wojciechkula.locals.presentation.common.SharedViewEvent
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.login.LoginViewEvent.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var navigator: LoginNavigator

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
            forgotPasswordButton.setOnClickListener { viewModel.onForgotPasswordClick() }
            registerButton.setOnClickListener { viewModel.onRegisterClick() }
        }
    }

    private fun observeViewModel() {
        viewModel.showLoading.observe(viewLifecycleOwner) { isLoading ->
            LoadingDialogFragment.toggle(childFragmentManager, isLoading)
        }
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        sharedViewModel.viewEvent.observe(viewLifecycleOwner, ::handleSharedEvents)
    }

    private fun handleEvents(event: LoginViewEvent) {
        when (event) {
            GetGroupsForExplore -> onGetGroupsForExplore()
            OpenForgotPassword -> onOpenForgotPassword()
            OpenRegister -> onOpenRegister()
            is Error -> onError(event.exception)
        }
    }

    private fun handleSharedEvents(event: SharedViewEvent) {
        when (event) {
            is SharedViewEvent.OpenDashboard -> onOpenDashboard()
        }
    }

    private fun onGetGroupsForExplore() {
        sharedViewModel.getGroupsForExplore()
    }

    private fun onOpenForgotPassword() {
        navigator.openForgotPassword(findNavController())
    }

    private fun onOpenRegister() {
        navigator.openRegister(findNavController())
    }

    private fun onOpenDashboard() {
        navigator.openDashboard(findNavController())
    }

    private fun onError(exception: Exception) {
        if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthEmailException) {
            binding.showSnackbarError(getString(R.string.log_in_exception_wrong_email_or_password))
        } else {
            binding.showSnackbarError(exception.message.toString())
        }
    }
}