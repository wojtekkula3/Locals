package com.wojciechkula.locals.presentation.register.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.common.validator.EmailValidator
import com.wojciechkula.locals.common.validator.NotBlankValidator
import com.wojciechkula.locals.common.validator.PasswordValidator
import com.wojciechkula.locals.common.validator.PhoneValidator
import com.wojciechkula.locals.domain.interactor.CheckIfUserIsNewInteractor
import com.wojciechkula.locals.extension.newBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserDataViewModel @Inject constructor(
    private val checkIfUserIsNewInteractor: CheckIfUserIsNewInteractor,
    private val notBlankValidator: NotBlankValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val phoneValidator: PhoneValidator
) : ViewModel() {

    private val _viewState = MutableLiveData(RegisterUserDataViewState())
    val viewState: LiveData<RegisterUserDataViewState>
        get() = _viewState

    private val _viewEvent = LiveEvent<RegisterUserDataViewEvent>()
    val viewEvent: LiveData<RegisterUserDataViewEvent>
        get() = _viewEvent

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading


    fun onNameChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                nameValid = nameValid,
                nextActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun onEmailChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                emailValid = emailValid,
                nextActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun onPasswordChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                passwordValid = passwordValid,
                nextActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun onPhoneChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                phoneNumberValid = phoneValid,
                nextActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun onTermsChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                acceptTermsValid = terms,
                nextActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun openRegisterHobbies(email: String) {
        _showLoading.postValue(true)
        viewModelScope.launch {
            if (_viewState.value?.nextActionEnabled == true) {
                if (checkIfUserIsNewInteractor(email)) {
                    _viewEvent.postValue(RegisterUserDataViewEvent.OpenRegisterHobbies)
                    _showLoading.postValue(false)
                } else {
                    _showLoading.postValue(false)
                    _viewEvent.postValue(RegisterUserDataViewEvent.ErrorUserExists)
                }
            }
        }
    }
}