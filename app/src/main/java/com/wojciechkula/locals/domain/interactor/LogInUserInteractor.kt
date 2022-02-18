package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class LogInUserInteractor @Inject constructor(private val repository: AuthRepositoryImpl) {

    suspend operator fun invoke(email: String, password: String) = repository.logIn(email, password)
}