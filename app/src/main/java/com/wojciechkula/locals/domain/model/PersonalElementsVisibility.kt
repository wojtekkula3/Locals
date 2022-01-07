package com.wojciechkula.locals.domain.model

data class PersonalElementsVisibility(
    var email: Boolean,
    val phoneNumber: Boolean,
    val hobbies: Boolean,
) {
    constructor() : this(false, false, true)
}