package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.UserDataSource
import com.wojciechkula.locals.data.mapper.UserMapper
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun getFirestoreUser(): UserModel = mapper.mapToDomain(dataSource.getUser())

    override suspend fun changeEmailVisibility(isVisible: Boolean): Boolean =
        dataSource.changeEmailVisibility(isVisible, dataSource.getUser())

    override suspend fun changePhoneVisibility(isVisible: Boolean): Boolean =
        dataSource.changePhoneNumberVisibility(isVisible, dataSource.getUser())

    override suspend fun changeHobbiesVisibility(isVisible: Boolean): Boolean =
        dataSource.changeHobbiesVisibility(isVisible, dataSource.getUser())
}