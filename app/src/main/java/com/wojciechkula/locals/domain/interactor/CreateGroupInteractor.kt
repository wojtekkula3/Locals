package com.wojciechkula.locals.domain.interactor

import android.graphics.Bitmap
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupInteractor @Inject constructor(private val repository: GroupRepository) {

    suspend operator fun invoke(image: Bitmap?, group: GroupModel) = repository.createGroup(image, group)
}