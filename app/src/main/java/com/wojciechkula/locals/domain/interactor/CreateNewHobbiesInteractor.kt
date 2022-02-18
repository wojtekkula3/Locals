package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.repository.HobbyRepository
import javax.inject.Inject

class CreateNewHobbiesInteractor @Inject constructor(private val repository: HobbyRepository) {

    suspend operator fun invoke(newHobbies: List<HobbyModel>) = repository.createNewHobbies(newHobbies)
}