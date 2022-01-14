package com.wojciechkula.locals.domain.model

import android.graphics.Bitmap
import com.google.firebase.firestore.DocumentReference

data class UserModel constructor(
    val id: String,
    val name: String,
    val surname: String?,
    val email: String,
    val avatarReference: String?,
    val avatar: Bitmap?,
    val phoneNumber: String?,
    val hobbies: ArrayList<HobbyModel>?,
    val about: String?,
    val elementsVisibility: PersonalElementsVisibilityModel,
    val groups: ArrayList<DocumentReference>? = arrayListOf()
) {
    constructor() : this("", "", "", "", "", null, "", arrayListOf(), "", PersonalElementsVisibilityModel())
    constructor(
        name: String,
        surname: String?,
        email: String,
        phoneNumber: String?,
        hobbies: ArrayList<HobbyModel>?,
        about: String?,
        elementsVisibility: PersonalElementsVisibilityModel
    ) : this("", name, surname, email, null, null, phoneNumber, hobbies, about, elementsVisibility)
}