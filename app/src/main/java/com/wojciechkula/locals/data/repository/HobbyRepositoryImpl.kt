package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.HobbiesDataSource
import com.wojciechkula.locals.data.mapper.HobbyMapper
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.repository.HobbyRepository
import javax.inject.Inject

class HobbyRepositoryImpl @Inject constructor(
    private val dataSource: HobbiesDataSource,
    private val mapper: HobbyMapper
) : HobbyRepository {

    override suspend fun getHobbiesGeneral(): List<HobbyModel> =
        dataSource.getHobbiesGeneral().map { hobby ->
            mapper.mapToDomain(hobby)
        }

    override suspend fun getHobbies(name: String): List<HobbyModel> =
        dataSource.getHobbies(name).map { hobby -> mapper.mapToDomain(hobby) }
}