package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.GroupRepository
import javax.inject.Inject

class LeaveGroupInteractor @Inject constructor(private val repository: GroupRepository) {

    suspend operator fun invoke(groupId: String) = repository.leaveGroup(groupId)
}