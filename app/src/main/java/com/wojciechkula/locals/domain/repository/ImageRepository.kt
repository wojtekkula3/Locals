package com.wojciechkula.locals.domain.repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    suspend fun addUserImage(image: Bitmap, userId: String): Uri?
    suspend fun addGroupImage(image: Bitmap, groupId: String): Uri?
}