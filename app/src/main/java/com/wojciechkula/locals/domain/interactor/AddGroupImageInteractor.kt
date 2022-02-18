package com.wojciechkula.locals.domain.interactor

import android.graphics.Bitmap
import com.wojciechkula.locals.domain.repository.ImageRepository
import javax.inject.Inject

class AddGroupImageInteractor @Inject constructor(private val repository: ImageRepository) {

    suspend operator fun invoke(bitmap: Bitmap, groupId: String) = repository.addGroupImage(bitmap, groupId)
}