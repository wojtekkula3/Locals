package com.wojciechkula.locals.data.entity

data class Member constructor(
    val userId: String = "",
    val name: String = "",
    val surname: String? = "",
    val avatar: String? = "",
)