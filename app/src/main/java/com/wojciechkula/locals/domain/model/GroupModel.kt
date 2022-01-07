package com.wojciechkula.locals.domain.model

data class GroupModel constructor(
    val id: String,
    val name: String,
    val location: LocationModel,
    val distance: Double,
    val avatar: String?,
    val hobbies: ArrayList<String>,
    val members: ArrayList<String>,
    val latestMessage: LatestMessageModel
) {
    constructor() : this("", "", LocationModel(), 0.0, "", arrayListOf(), arrayListOf(), LatestMessageModel())
}