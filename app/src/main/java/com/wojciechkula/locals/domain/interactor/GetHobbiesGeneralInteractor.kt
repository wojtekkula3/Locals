package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.HobbyRepository
import javax.inject.Inject

class GetHobbiesGeneralInteractor @Inject constructor(private val repository: HobbyRepository) {

    suspend operator fun invoke() = repository.getHobbiesGeneral()
}