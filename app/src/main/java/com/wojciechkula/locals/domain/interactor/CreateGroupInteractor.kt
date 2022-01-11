package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupInteractor @Inject constructor(private val repository: GroupRepository) {

    suspend operator fun invoke(group: GroupModel) = repository.createGroup(group)
}