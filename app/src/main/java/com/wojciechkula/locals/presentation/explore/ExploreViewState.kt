package com.wojciechkula.locals.presentation.explore

import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.presentation.explore.list.ExploreItem

data class ExploreViewState(
    val groupsList: ArrayList<ExploreItem>? = arrayListOf(),
    val searchHobbiesList: ArrayList<HobbyModel>? = arrayListOf(),
    val selectedHobbiesList: ArrayList<HobbyModel>? = arrayListOf(),
    var distance: Int = 10,
    val hasInitGroups: Boolean = false

)