package com.wojciechkula.locals.domain.model

data class GroupModel constructor(
    val id: String,
    val name: String,
    val description: String,
    val location: LocationModel,
    val distance: Double,
    val avatar: String?,
    val hobbies: ArrayList<String>,
    val members: ArrayList<String>,
    val latestMessage: LatestMessageModel
) {
    constructor() : this("", "", "", LocationModel(), 0.0, "", arrayListOf(), arrayListOf(), LatestMessageModel())
    constructor(
        name: String,
        description: String,
        location: LocationModel,
        hobbies: ArrayList<String>,
        latestMessage: LatestMessageModel,
        members: ArrayList<String>,
    ) : this(
        name = name,
        description = description,
        location = location,
        hobbies = hobbies,
        id = "",
        distance = 0.0,
        avatar = null,
        members = members,
        latestMessage = latestMessage
    )
}