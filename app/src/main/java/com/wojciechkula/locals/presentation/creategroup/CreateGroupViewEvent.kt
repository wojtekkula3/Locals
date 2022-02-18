package com.wojciechkula.locals.presentation.creategroup

import com.wojciechkula.locals.domain.model.LocationModel
import com.wojciechkula.locals.presentation.creategroup.list.HobbyItem

sealed class CreateGroupViewEvent {

    data class ShowChipsWithSelectedHobbies(val selectedHobbies: ArrayList<HobbyItem>?) : CreateGroupViewEvent()
    data class SetLocation(val location: LocationModel?) : CreateGroupViewEvent()
    data class OpenDialog(val newHobbies: ArrayList<HobbyItem>) : CreateGroupViewEvent()
    object OpenMyGroups : CreateGroupViewEvent()
}