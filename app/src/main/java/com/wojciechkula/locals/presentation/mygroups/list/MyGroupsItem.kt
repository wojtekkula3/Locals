package com.wojciechkula.locals.presentation.mygroups.list

import java.util.*

data class MyGroupsItem(
    val id: String,
    val avatar: String?,
    val name: String,
    val author: String,
    val message: String,
    val sentAt: Date
)