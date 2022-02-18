package com.wojciechkula.locals.presentation.group

import com.wojciechkula.locals.domain.model.GroupModel

sealed class GroupViewEvent {

    object ShowNoMessagesYetLabel : GroupViewEvent()
    data class OpenInfo(val group: GroupModel) : GroupViewEvent()
}