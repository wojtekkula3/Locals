package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.RegisterDataSource
import com.wojciechkula.locals.data.mapper.UserMapper
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val dataSource: RegisterDataSource,
    private val userMapper: UserMapper
) : RegisterRepository {

    override suspend fun registerUser(email: String, password: String) = dataSource.registerUser(email, password)

    override suspend fun registerUserData(user: UserModel) = dataSource.registerUserData(userMapper.mapToEntity(user))
}