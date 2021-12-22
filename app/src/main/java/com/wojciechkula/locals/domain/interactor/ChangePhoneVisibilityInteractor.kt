package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.UserRepository
import javax.inject.Inject

class ChangePhoneVisibilityInteractor @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(isVisible: Boolean) = repository.changePhoneVisibility(isVisible)
}