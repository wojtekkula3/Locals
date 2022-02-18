package com.wojciechkula.locals.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class MessageModel(

    @DocumentId
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String?,
    val message: String,
    val sentAt: Timestamp,
) {
    constructor(
        authorId: String,
        authorName: String,
        authorAvatar: String?,
        message: String,
    ) : this(
        "",
        authorId = authorId,
        authorName = authorName,
        authorAvatar = authorAvatar,
        message = message,
        sentAt = Timestamp.now()
    )
}