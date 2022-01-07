package com.wojciechkula.locals.presentation.mygroups

import com.wojciechkula.locals.presentation.mygroups.list.MyGroupsItem

sealed class MyGroupsViewEvent {

    data class SearchGroups(val groups: List<MyGroupsItem>) : MyGroupsViewEvent()
    data class OpenGroup(val group: MyGroupsItem, val userId: String) : MyGroupsViewEvent()
}