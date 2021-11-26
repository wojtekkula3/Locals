package com.wojciechkula.locals.presentation.register.hobbies

import com.wojciechkula.locals.domain.model.HobbyModel

data class RegisterHobbiesViewState(
    val priorityHighHobbiesList: ArrayList<HobbyModel>,
    var customHobbiesList: ArrayList<HobbyModel> = arrayListOf(),
    var selectedHobbiesList: ArrayList<HobbyModel> = arrayListOf(),
    var registerActionEnabled: Boolean = false
)