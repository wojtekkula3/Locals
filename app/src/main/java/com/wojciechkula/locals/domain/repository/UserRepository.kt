package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.MemberModel
import com.wojciechkula.locals.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getFirestoreUser(): UserModel
    suspend fun getFirestoreUserFlow(): Flow<UserModel>
    suspend fun getFirestoreUserById(userId: String): UserModel
    suspend fun getUsersByGroupMembers(membersId: ArrayList<String>): List<MemberModel>
    suspend fun changeEmailVisibility(isVisible: Boolean): Boolean
    suspend fun changePhoneVisibility(isVisible: Boolean): Boolean
    suspend fun changeHobbiesVisibility(isVisible: Boolean): Boolean

}