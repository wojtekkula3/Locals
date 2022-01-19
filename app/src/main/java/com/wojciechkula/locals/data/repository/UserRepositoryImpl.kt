package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.UserDataSource
import com.wojciechkula.locals.data.mapper.MemberMapper
import com.wojciechkula.locals.data.mapper.UserMapper
import com.wojciechkula.locals.domain.model.MemberModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
    private val mapper: UserMapper,
    private val memberMapper: MemberMapper
) : UserRepository {

    override suspend fun getFirestoreUser(): UserModel = mapper.mapToDomain(dataSource.getUser())

    override suspend fun getFirestoreUserFlow(): Flow<UserModel> =
        dataSource.getUserFlow().map { user -> mapper.mapToDomain(user) }

    override suspend fun getFirestoreUserById(userId: String): UserModel =
        mapper.mapToDomain(dataSource.getUserById(userId))

    override suspend fun getUsersByGroupMembers(membersId: ArrayList<String>): List<MemberModel> =
        dataSource.getUsersByGroupMembers(membersId).map { member -> memberMapper.mapToDomain(member) }

    override suspend fun changeEmailVisibility(isVisible: Boolean): Boolean =
        dataSource.changeEmailVisibility(isVisible, dataSource.getUser())

    override suspend fun changePhoneVisibility(isVisible: Boolean): Boolean =
        dataSource.changePhoneNumberVisibility(isVisible, dataSource.getUser())

    override suspend fun changeHobbiesVisibility(isVisible: Boolean): Boolean =
        dataSource.changeHobbiesVisibility(isVisible, dataSource.getUser())
}