package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.data.repository.AuthRepository
import javax.inject.Inject

class LogInUserInteractor @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) = authRepository.logIn(email, password)
}