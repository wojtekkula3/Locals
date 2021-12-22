package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.HobbyModel

interface GroupRepository {

    suspend fun getGroupsByDistanceAndHobbies(distance: Int, selectedHobbies: ArrayList<HobbyModel>?): List<GroupModel>
    suspend fun joinGroup(groupId: String): Boolean
}