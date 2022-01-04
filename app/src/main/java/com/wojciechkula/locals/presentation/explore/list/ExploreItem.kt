package com.wojciechkula.locals.presentation.explore.list

data class ExploreItem(
    val avatar: String?,
    val name: String,
    val id: String,
    val hobbies: ArrayList<String>,
    val distance: Double,
    val size: Int = 0,
    val members: ArrayList<String>
)