package com.wojciechkula.locals.domain.model

data class UserModel constructor(
    val name: String = "",
    val surname: String? = "",
    val email: String = "",
    val avatar: String? = "",
    val phoneNumber: String? = "",
    val hobbies: ArrayList<HobbyModel>? = arrayListOf(),
    val about: String? = ""
)