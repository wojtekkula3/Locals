package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.data.repository.AuthRepository
import javax.inject.Inject

class GetFirebaseUserInteractor @Inject constructor(private val authRepository: AuthRepository){

    suspend operator fun invoke() = authRepository.getUser()
}