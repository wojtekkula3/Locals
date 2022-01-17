package com.wojciechkula.locals.presentation.explore.list

data class ExploreItem(
    val id: String,
    val name: String,
    val avatar: String?,
    val hobbies: ArrayList<String>,
    val distance: Double,
    val size: Int = 0,
    val members: ArrayList<String>
)