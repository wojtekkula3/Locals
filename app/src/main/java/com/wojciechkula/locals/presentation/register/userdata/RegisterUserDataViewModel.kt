package com.wojciechkula.locals.presentation.register.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.common.validator.EmailValidator
import com.wojciechkula.locals.common.validator.NotBlankValidator
import com.wojciechkula.locals.common.validator.PasswordValidator
import com.wojciechkula.locals.common.validator.PhoneValidator
import com.wojciechkula.locals.extension.newBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterUserDataViewModel @Inject constructor(
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


    fun onNameChange(name: String, email: String, password: String, phoneNumber: String, terms: Boolean) {
        val nameValid = notBlankValidator.validate(name)
        val emailValid = emailValidator.validate(email)
        val passwordValid = passwordValidator.validate(password)
        val phoneValid = phoneValidator.validate(phoneNumber)
        _viewState.value = viewState.newBuilder {
            copy(
                nameValid = nameValid,
                signUpActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
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
                signUpActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
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
                signUpActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
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
                signUpActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
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
                signUpActionEnabled = nameValid && emailValid && passwordValid && phoneValid && terms
            )
        }
    }

    fun openRegisterHobbies() {
        if (_viewState.value?.signUpActionEnabled == true) {
            _viewEvent.postValue(RegisterUserDataViewEvent.OpenRegisterHobbies)
        }
    }
}