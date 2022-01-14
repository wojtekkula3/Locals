package com.wojciechkula.locals.presentation.register.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val surname: String?,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val image: Bitmap?
) : Parcelable