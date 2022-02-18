package com.wojciechkula.locals.presentation.common

import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.explore.list.ExploreItem

data class SharedViewState(
    val user: UserModel,
    val exploreGroups: ArrayList<ExploreItem>?
)