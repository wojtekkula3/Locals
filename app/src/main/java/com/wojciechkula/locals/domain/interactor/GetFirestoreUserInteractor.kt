package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.domain.repository.UserRepository
import javax.inject.Inject

class GetFirestoreUserInteractor @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(): UserModel = repository.getFirestoreUser()
}