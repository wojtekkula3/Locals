package com.wojciechkula.locals.data.entity

import android.graphics.Bitmap
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class User constructor(

    @DocumentId
    val id: String = "",
    val name: String = "",
    val surname: String? = "",
    val email: String = "",
    val avatarReference: String? = "",
    @get:Exclude var avatar: Bitmap? = null,
    val phoneNumber: String? = "",
    val hobbies: ArrayList<Hobby>? = arrayListOf(),
    val about: String? = "",
    val elementsVisibility: PersonalElementsVisibility = PersonalElementsVisibility(),
    val groups: ArrayList<DocumentReference> = arrayListOf()
)