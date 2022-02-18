package com.wojciechkula.locals.presentation.group.userdialog

import java.lang.Exception

sealed class UserDialogViewEvent {

    data class ShowError(val exception: Exception) : UserDialogViewEvent()
}