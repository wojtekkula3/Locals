package com.wojciechkula.locals.domain.model

data class UserModel constructor(
    val id: String,
    val name: String,
    val surname: String?,
    val email: String,
    val avatar: String?,
    val phoneNumber: String?,
    val hobbies: ArrayList<HobbyModel>?,
    val about: String?,
    val elementsVisibility: PersonalElementsVisibility
) {
    constructor() : this("", "", "", "", "", "", arrayListOf(), "", PersonalElementsVisibility())
    constructor(
        name: String,
        surname: String?,
        email: String,
        avatar: String?,
        phoneNumber: String?,
        hobbies: ArrayList<HobbyModel>?,
        about: String?,
        elementsVisibility: PersonalElementsVisibility
    ) : this("", name, surname, email, avatar, phoneNumber, hobbies, about, elementsVisibility)
}