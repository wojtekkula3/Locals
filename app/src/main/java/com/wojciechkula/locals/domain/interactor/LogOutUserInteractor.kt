package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUserInteractor @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.logOut()
}