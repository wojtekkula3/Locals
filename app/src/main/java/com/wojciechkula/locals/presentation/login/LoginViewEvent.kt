package com.wojciechkula.locals.presentation.login

sealed class LoginViewEvent {
    object OpenDashboard : LoginViewEvent()
    object OpenRegister : LoginViewEvent()
    object OpenForgotPassword : LoginViewEvent()

    data class Error(val message: String?) : LoginViewEvent()
}