package com.wojciechkula.locals.domain.repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    suspend fun addUserImage(image: Bitmap, userId: String, userEmail: String): Uri?
}