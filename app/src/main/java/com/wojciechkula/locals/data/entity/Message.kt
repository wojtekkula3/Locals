package com.wojciechkula.locals.data.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Message(

    @DocumentId
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    @get:Exclude var authorAvatar: String? = "",
    val message: String = "",
    val sentAt: Timestamp = Timestamp.now()
)