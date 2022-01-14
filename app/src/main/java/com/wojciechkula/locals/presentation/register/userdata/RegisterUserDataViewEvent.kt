package com.wojciechkula.locals.presentation.register.userdata

sealed class RegisterUserDataViewEvent {

    object OpenRegisterHobbies : RegisterUserDataViewEvent()
    object ShowErrorUserExists : RegisterUserDataViewEvent()
}