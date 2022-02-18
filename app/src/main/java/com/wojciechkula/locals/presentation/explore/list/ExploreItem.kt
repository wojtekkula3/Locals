package com.wojciechkula.locals.presentation.explore.list

import android.graphics.Bitmap
import com.google.firebase.Timestamp

data class ExploreItem(
    val id: String,
    val name: String,
    val avatar: String?,
    var avatarBitmap: Bitmap? = null,
    val hobbies: ArrayList<String>,
    val distance: Double,
    val size: Int = 0,
    val description: String,
    val lastActivity: Timestamp,
    val members: ArrayList<String>
)