package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUserDataInteractor @Inject constructor(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(user: UserModel) = repository.registerUserData(user)
}