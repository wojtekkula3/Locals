package com.wojciechkula.locals.presentation.group.list

import java.util.*

data class MessageItem(
    val id: String,
    val authorId: String,
    val authorName: String,
    val message: String,
    val sentAt: Date,
)