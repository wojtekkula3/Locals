package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class GetFirebaseUserInteractor @Inject constructor(private val repository: AuthRepositoryImpl) {

    suspend operator fun invoke() = repository.getUser()
}