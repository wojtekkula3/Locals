package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserGroupsInteractor @Inject constructor(private val repository: GroupRepository) {

    suspend operator fun invoke(): Flow<List<GroupModel>> = repository.getUserGroups()

}