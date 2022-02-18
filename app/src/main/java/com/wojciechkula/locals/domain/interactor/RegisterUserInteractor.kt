package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUserInteractor @Inject constructor(private val repository: RegisterRepository) {

    suspend operator fun invoke(email: String, password: String) = repository.registerUser(email, password)
}