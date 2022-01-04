package com.wojciechkula.locals.presentation.mygroups

import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsItem

sealed class MyGroupsViewState {
    data class Success(val groups: List<MyGroupsItem>) : MyGroupsViewState()
}