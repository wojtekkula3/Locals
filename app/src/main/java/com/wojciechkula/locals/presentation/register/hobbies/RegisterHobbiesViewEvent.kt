package com.wojciechkula.locals.presentation.register.hobbies

import com.wojciechkula.locals.domain.model.HobbyModel

sealed class RegisterHobbiesViewEvent {

    object OpenDashboard : RegisterHobbiesViewEvent()

    data class GetHighPriorityHobbies(
        val priorityHighHobbiesList: ArrayList<HobbyModel>?,
        val selectedHobbiesList: ArrayList<HobbyModel>?
    ) : RegisterHobbiesViewEvent()

    data class GetCustomHobbies(
        val customHobbiesList: ArrayList<HobbyModel>?,
        val selectedHobbiesList: ArrayList<HobbyModel>?
    ) : RegisterHobbiesViewEvent()

    data class Error(val message: String?) : RegisterHobbiesViewEvent()
}