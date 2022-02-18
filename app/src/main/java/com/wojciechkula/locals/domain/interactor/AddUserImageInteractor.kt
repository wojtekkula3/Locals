package com.wojciechkula.locals.domain.interactor

import android.graphics.Bitmap
import com.wojciechkula.locals.domain.repository.ImageRepository
import javax.inject.Inject

class AddUserImageInteractor @Inject constructor(private val repository: ImageRepository) {

    suspend operator fun invoke(image: Bitmap, userId: String) =
        repository.addUserImage(image, userId)
}