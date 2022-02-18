package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.repository.GroupRepository
import javax.inject.Inject

class GetGroupsByDistanceAndHobbiesInteractor @Inject constructor(private val repository: GroupRepository) {

    suspend operator fun invoke(distance: Int, selectedHobbies: ArrayList<HobbyModel>?) =
        repository.getGroupsByDistanceAndHobbies(distance, selectedHobbies)
}