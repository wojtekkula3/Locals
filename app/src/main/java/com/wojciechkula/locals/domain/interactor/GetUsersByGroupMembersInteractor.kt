package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersByGroupMembersInteractor @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(membersId: ArrayList<String>) = repository.getUsersByGroupMembers(membersId)
}