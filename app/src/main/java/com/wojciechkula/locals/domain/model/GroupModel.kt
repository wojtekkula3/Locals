package com.wojciechkula.locals.domain.model

data class GroupModel constructor(
    val id: String,
    val name: String,
    val location: LocationModel,
    val distance: Double,
    val avatar: String?,
    val hobbies: ArrayList<String>,
    val members: ArrayList<MemberModel>,
)