package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.HobbyRepository
import javax.inject.Inject

class GetHobbiesInteractor @Inject constructor(
    private val repository: HobbyRepository
) {
    suspend operator fun invoke(name: String) = repository.getHobbies(name)
}