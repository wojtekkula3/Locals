package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.UserModel

interface UserRepository {

    suspend fun getFirestoreUser(): UserModel
    suspend fun changeEmailVisibility(isVisible: Boolean): Boolean
    suspend fun changePhoneVisibility(isVisible: Boolean): Boolean
    suspend fun changeHobbiesVisibility(isVisible: Boolean): Boolean

}