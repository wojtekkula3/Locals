package com.wojciechkula.locals.presentation.explore

import com.wojciechkula.locals.domain.model.HobbyModel

sealed class ExploreViewEvent {

    object ShowGroups : ExploreViewEvent()
    object ChangeDistance : ExploreViewEvent()
    object HideJoinedGroup : ExploreViewEvent()

    data class ShowSearchHobbies(
        val searchHobbies: ArrayList<HobbyModel>?,
        val selectedHobbies: ArrayList<HobbyModel>?
    ) : ExploreViewEvent()

}