package com.wojciechkula.locals.data.entity

data class Group constructor(
    val id: String = "",
    val name: String = "",
    val location: Location,
    val distance: Double = 0.0,
    val avatar: String? = "",
    val hobbies: ArrayList<String> = arrayListOf(),
    val members: ArrayList<Member> = arrayListOf(),
)
