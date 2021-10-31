package com.wojciechkula.locals.presentation.register.userdata

data class RegisterUserDataViewState(
    val nameValid: Boolean = true,
    val emailValid: Boolean = true,
    val passwordValid: Boolean = true,
    val phoneNumberValid: Boolean = true,
    val acceptTermsValid: Boolean = true,
    val signUpActionEnabled: Boolean = false
)