package com.wojciechkula.locals.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.common.validator.EmailValidator
import com.wojciechkula.locals.common.validator.NotBlankValidator
import com.wojciechkula.locals.domain.interactor.LogInUserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserInteractor: LogInUserInteractor,
    private val emailValidator: EmailValidator,
    private val notBlankValidator: NotBlankValidator
) : ViewModel() {

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var _viewEvent = LiveEvent<LoginViewEvent>()
    val viewEvent: LiveData<LoginViewEvent>
        get() = _viewEvent

    fun onLogInClick(email: String, password: String) {
        _showLoading.postValue(true)

        viewModelScope.launch {
            val emailValid = emailValidator.validate(email)
            val passwordValid = notBlankValidator.validate(password)
            if (emailValid && passwordValid) {
                loginUserInteractor.invoke(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _viewEvent.postValue(LoginViewEvent.OpenDashboard)
                        } else {
                            Log.e("Authentication failed", "Authentication failed")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Exception occured", it.message.toString())
                    }
                _showLoading.postValue(false)
            } else {
                _showLoading.postValue(false)
            }
        }
    }

    fun onRegisterClick() {
        _viewEvent.postValue(LoginViewEvent.OpenRegister)
    }
}