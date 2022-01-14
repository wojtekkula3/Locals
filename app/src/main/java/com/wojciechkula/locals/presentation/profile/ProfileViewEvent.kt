package com.wojciechkula.locals.presentation.profile

import android.net.Uri

sealed class ProfileViewEvent {

    data class ShowImageChangeSuccess(val uri: Uri?) : ProfileViewEvent()
    data class ShowError(val message: String) : ProfileViewEvent()
}