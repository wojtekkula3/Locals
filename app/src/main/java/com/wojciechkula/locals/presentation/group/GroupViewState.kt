package com.wojciechkula.locals.presentation.group

import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.group.list.MessageItem

data class GroupViewState(
    val messages: List<MessageItem> = emptyList(),
    val user: UserModel = UserModel(),
    val group: GroupModel = GroupModel()
)