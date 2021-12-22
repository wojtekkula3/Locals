package com.wojciechkula.locals.presentation.profile

import com.wojciechkula.locals.domain.model.UserModel

data class ProfileViewState constructor(
    val user: UserModel? = null,
    val emailVisibility: Boolean = false,
    val phoneNumberVisibility: Boolean = false,
    val hobbiesVisibility: Boolean = false,
)