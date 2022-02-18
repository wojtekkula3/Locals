package com.wojciechkula.locals.domain.model

data class HobbyModel constructor(
    val name: String,
    val general: Boolean?,
    val language: String?
) {
    constructor(
        name: String,
    ) : this(name = name, null, null)
}