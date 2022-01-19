package com.wojciechkula.locals.presentation.group.groupdialog

sealed class GroupDialogViewEvent {
    object OpenMyGroups : GroupDialogViewEvent()
    object ShowImageChangeSuccess : GroupDialogViewEvent()
    data class ShowError(val exception: Exception) : GroupDialogViewEvent()
}