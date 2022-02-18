package com.wojciechkula.locals.domain.model

import com.google.firebase.firestore.DocumentReference

data class UserModel constructor(
    val id: String,
    val name: String,
    val surname: String?,
    val email: String,
    val avatarReference: String?,
    val phoneNumber: String?,
    val hobbies: ArrayList<String>?,
    val about: String?,
    val elementsVisibility: PersonalElementsVisibilityModel,
    val groups: ArrayList<DocumentReference>? = arrayListOf()
) {
    constructor() : this("", "", "", "", "", "", arrayListOf(), "", PersonalElementsVisibilityModel())
    constructor(
        name: String,
        surname: String?,
        email: String,
        phoneNumber: String?,
        hobbies: ArrayList<String>?,
        about: String?,
        elementsVisibility: PersonalElementsVisibilityModel
    ) : this("", name, surname, email, null, phoneNumber, hobbies, about, elementsVisibility)
}