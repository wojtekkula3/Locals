package com.wojciechkula.locals.data.entity

import com.google.firebase.firestore.Exclude

data class Hobby constructor(
    val name: String = "",
    @get:Exclude val general: Boolean? = null,
    @get:Exclude val language: String? = null
)