package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.HobbyModel
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    suspend fun getGroupsByDistanceAndHobbies(distance: Int, selectedHobbies: ArrayList<HobbyModel>?): List<GroupModel>
    suspend fun getUserGroups(): Flow<List<GroupModel>>
    suspend fun getGroup(groupId: String): Flow<GroupModel>
    suspend fun joinGroup(groupId: String): Boolean
    suspend fun leaveGroup(groupId: String): Boolean
}