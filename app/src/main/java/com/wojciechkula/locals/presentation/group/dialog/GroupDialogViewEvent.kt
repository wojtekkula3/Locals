package com.wojciechkula.locals.presentation.group.dialog

import java.lang.Exception

sealed class GroupDialogViewEvent {
    object OpenMyGroups : GroupDialogViewEvent()
    object ShowImageChangeSuccess : GroupDialogViewEvent()
    data class ShowError(val exception: Exception) : GroupDialogViewEvent()
}