package com.wojciechkula.locals.presentation.mygroups

import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsItem

data class MyGroupsViewState(
    val groups: List<MyGroupsItem> = emptyList(),
    val user: UserModel = UserModel()
)
