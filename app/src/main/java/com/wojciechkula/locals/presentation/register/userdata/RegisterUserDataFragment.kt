package com.wojciechkula.locals.presentation.register.userdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentRegisterUserDataBinding
import com.wojciechkula.locals.extension.showSnackbar
import com.wojciechkula.locals.navigation.RegisterUserDataNavigator
import com.wojciechkula.locals.presentation.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.checkedChanges
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

@AndroidEntryPoint
internal class RegisterUserDataFragment : Fragment() {

    private val viewModel: RegisterUserDataViewModel by viewModels()

    @Inject
    lateinit var navigator: RegisterUserDataNavigator

    private var _binding: FragmentRegisterUserDataBinding? = null
    private val binding
        get() = _binding!!

    private val name get() = binding.nameInput.text.toString()
    private val surname get() = binding.surnameInput.text.toString()
    private val email get() = binding.emailInput.text.toString()
    private val password get() = binding.passwordInput.text.toString()
    private val phoneNumber get() = binding.phoneNumberInput.text.toString()
    private val terms get() = binding.termsAndConditionsCheckBox.isChecked


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterUserDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {

        with(binding) {
            nameInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onNameChange(name, email, password, phoneNumber, terms) }
                .launchIn(lifecycleScope)

            emailInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onEmailChange(name, email, password, phoneNumber, terms) }
                .launchIn(lifecycleScope)

            passwordInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onPasswordChange(name, email, password, phoneNumber, terms) }
                .launchIn(lifecycleScope)

            phoneNumberInput
                .textChanges()
                .dropWhile { it.isBlank() }
                .onEach { viewModel.onPhoneChange(name, email, password, phoneNumber, terms) }
                .launchIn(lifecycleScope)

            termsAndConditionsCheckBox
                .checkedChanges()
                .onEach { viewModel.onTermsChange(name, email, password, phoneNumber, terms) }
                .launchIn(lifecycleScope)

            backButton.setOnClickListener { findNavController().popBackStack() }
            nextButton.setOnClickListener { viewModel.onNextClick(emailInput.text.toString()) }
        }

    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun bindState(state: RegisterUserDataViewState) {
        with(binding) {

            if (state.nameValid) {
                nameLayout.error = null
                nameLayout.isErrorEnabled = false
            } else {
                nameLayout.error = getString(R.string.register_user_data_name_is_required)
            }

            if (state.emailValid) {
                emailLayout.error = null
                emailLayout.isErrorEnabled = false
            } else {
                emailLayout.error = getString(R.string.register_user_data_wrong_email)
            }

            if (state.passwordValid) {
                passwordLayout.error = null
                passwordLayout.isErrorEnabled = false
            } else {
                passwordLayout.error = getString(R.string.register_user_data_password_requirements)
            }

            if (state.phoneNumberValid) {
                phoneNumberLayout.error = null
                phoneNumberLayout.isErrorEnabled = false
            } else {
                phoneNumberLayout.error = getString(R.string.register_user_data_insert_correct_number_or_leave_empty)
            }

            nextButton.isEnabled = state.nextActionEnabled
        }
    }

    private fun handleEvents(event: RegisterUserDataViewEvent?) {
        when (event) {
            RegisterUserDataViewEvent.OpenRegisterHobbies -> onOpenRegisterHobbies()
            RegisterUserDataViewEvent.ErrorUserExists -> onShowError()
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun onOpenRegisterHobbies() {
        val user = User(name, surname, email, password, phoneNumber, photoUrl = null)
        navigator.openRegisterHobbies(findNavController(), user)
    }

    private fun onShowError() {
        binding.showSnackbar(getString(R.string.register_data_this_email_already_exists_in_database))
    }

}