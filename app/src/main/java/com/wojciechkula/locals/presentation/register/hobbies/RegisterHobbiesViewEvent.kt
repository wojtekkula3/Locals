package com.wojciechkula.locals.presentation.register.hobbies

import com.wojciechkula.locals.domain.model.HobbyModel

sealed class RegisterHobbiesViewEvent {

    object GetGroupsForExplore : RegisterHobbiesViewEvent()

    data class GetGeneralHobbies(
        val generalHobbiesList: ArrayList<HobbyModel>?,
        val selectedHobbiesList: ArrayList<HobbyModel>?
    ) : RegisterHobbiesViewEvent()

    data class GetCustomHobbies(
        val customHobbiesList: ArrayList<HobbyModel>?,
        val selectedHobbiesList: ArrayList<HobbyModel>?
    ) : RegisterHobbiesViewEvent()
}