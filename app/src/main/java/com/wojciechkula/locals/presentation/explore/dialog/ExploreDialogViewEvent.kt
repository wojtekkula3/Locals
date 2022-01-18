package com.wojciechkula.locals.presentation.explore.dialog

import java.lang.Exception

sealed class ExploreDialogViewEvent {
    object JoinGroup : ExploreDialogViewEvent()
    data class ShowError(val exception: Exception) : ExploreDialogViewEvent()
}