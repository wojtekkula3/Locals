package com.wojciechkula.locals.presentation.common

import com.wojciechkula.locals.presentation.explore.list.ExploreItem

sealed class SharedViewEvent {

    object OpenDashboard : SharedViewEvent()

    data class SetGroupsForExplore(
        val groups: ArrayList<ExploreItem>?
    ) : SharedViewEvent()
}