package com.wojciechkula.locals.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val surname: String?,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val photoUrl: String?
) : Parcelable