package com.wojciechkula.locals.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.wojciechkula.locals.data.datasource.ImageDataSource
import com.wojciechkula.locals.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val dataSource: ImageDataSource
) : ImageRepository {

    override suspend fun addUserImage(image: Bitmap, userId: String): Uri? =
        dataSource.addUserImage(image, userId)

    override suspend fun addGroupImage(image: Bitmap, groupId: String): Uri? =
        dataSource.addGroupImage(image, groupId)
}