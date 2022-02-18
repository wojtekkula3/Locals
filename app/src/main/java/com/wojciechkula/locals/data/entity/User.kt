package com.wojciechkula.locals.data.entity

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class User constructor(

    @DocumentId
    val id: String = "",
    val name: String = "",
    val surname: String? = "",
    val email: String = "",
    val avatarReference: String? = "",
    val phoneNumber: String? = "",
    val hobbies: ArrayList<String>? = arrayListOf(),
    val about: String? = "",
    val elementsVisibility: PersonalElementsVisibility = PersonalElementsVisibility(),
    val groups: ArrayList<DocumentReference> = arrayListOf()
)