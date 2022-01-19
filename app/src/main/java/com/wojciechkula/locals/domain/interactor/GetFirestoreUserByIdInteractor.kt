package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.UserRepository
import javax.inject.Inject

class GetFirestoreUserByIdInteractor @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(groupId: String) = repository.getFirestoreUserById(groupId)
}