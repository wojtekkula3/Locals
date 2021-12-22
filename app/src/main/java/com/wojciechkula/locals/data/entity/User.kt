package com.wojciechkula.locals.data.entity

import com.google.firebase.firestore.DocumentId

data class User constructor(

    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val surname: String? = "",
    val email: String = "",
    val avatar: String? = "",
    val phoneNumber: String? = "",
    val hobbies: ArrayList<Hobby>? = arrayListOf(),
    val about: String? = "",
    val elementsVisibility: PersonalElementsVisibility = PersonalElementsVisibility()
)