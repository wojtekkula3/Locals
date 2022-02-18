package com.wojciechkula.locals.data.entity

import com.google.firebase.Timestamp

data class LatestMessage constructor(
    val authorId: String = "",
    val authorName: String = "",
    val message: String = "",
    val sentAt: Timestamp = Timestamp.now()
)