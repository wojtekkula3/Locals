package com.wojciechkula.locals.data.entity

data class User constructor(
    val name: String = "",
    val surname: String? = "",
    val email: String = "",
    val avatar: String? = "",
    val phoneNumber: String? = "",
    val hobbies: ArrayList<Hobby>? = arrayListOf(),
    val about: String? = ""
)