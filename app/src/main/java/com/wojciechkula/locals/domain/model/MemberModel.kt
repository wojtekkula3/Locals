package com.wojciechkula.locals.domain.model

data class MemberModel constructor(
    val userId: String,
    val name: String,
    val surname: String?,
    val avatar: String?,
)