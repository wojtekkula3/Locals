package com.wojciechkula.locals.presentation.creategroup

import com.wojciechkula.locals.domain.model.LocationModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.creategroup.list.HobbyItem

data class CreateGroupViewState(
    val user: UserModel = UserModel(),
    val searchHobbies: ArrayList<HobbyItem> = arrayListOf(),
    val selectedHobbies: ArrayList<HobbyItem>? = arrayListOf(),
    val selectedLocation: LocationModel? = LocationModel(),
    val newHobbies: ArrayList<HobbyItem>? = arrayListOf(),
    val nameValid: Boolean = false,
    val descriptionValid: Boolean = false,
    val hobbiesValid: Boolean = false,
    val locationValid: Boolean = false,
    val nextActionEnabled: Boolean = false
)