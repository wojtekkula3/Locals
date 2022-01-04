package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.GroupsDataSource
import com.wojciechkula.locals.data.datasource.UserDataSource
import com.wojciechkula.locals.data.mapper.GroupMapper
import com.wojciechkula.locals.data.mapper.HobbyMapper
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val dataSource: GroupsDataSource,
    private val userDataSource: UserDataSource,
    private val groupMapper: GroupMapper,
    private val hobbyMapper: HobbyMapper
) : GroupRepository {

    override suspend fun getGroupsByDistanceAndHobbies(
        distance: Int,
        selectedHobbies: ArrayList<HobbyModel>?
    ): List<GroupModel> {
        val selectedHobbiesEntity =
            selectedHobbies?.map { hobbyModel -> hobbyMapper.mapToEntity(hobbyModel) }
        return dataSource.getGroupsByDistanceAndHobbies(distance, selectedHobbiesEntity, userDataSource.getUser())
            .map { group -> groupMapper.mapToDomain(group) }

    }

    override suspend fun getUserGroups(): Flow<List<GroupModel>> = dataSource.getUserGroups(userDataSource.getUser())
        .map { group -> groupMapper.mapListToDomain(group) }

    override suspend fun joinGroup(groupId: String): Boolean = dataSource.joinGroup(groupId, userDataSource.getUser())
}