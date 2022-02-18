package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.AuthRepository
import javax.inject.Inject

class CheckIfUserIsNewInteractor @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String): Boolean = repository.isNewUser(email)
}