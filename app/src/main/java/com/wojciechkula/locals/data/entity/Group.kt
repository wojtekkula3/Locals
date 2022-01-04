package com.wojciechkula.locals.data.entity

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Group constructor(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val location: Location = Location(),
    @Exclude
    val distance: Double = 0.0,
    val avatar: String? = "",
    val hobbies: ArrayList<String> = arrayListOf(),
    val members: ArrayList<String> = arrayListOf(),
    val latestMessage: LatestMessage = LatestMessage()
)
