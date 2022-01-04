package com.wojciechkula.locals.domain.model

import com.google.firebase.Timestamp

data class LatestMessage constructor(
    val authorId: String,
    val authorName: String,
    val message: String,
    val sentAt: Timestamp,
)