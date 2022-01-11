package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.HobbyModel

interface HobbyRepository {

    suspend fun getHobbiesGeneral(): List<HobbyModel>
    suspend fun getHobbies(id: String): List<HobbyModel>
    suspend fun createNewHobbies(newHobbies: List<HobbyModel>): Boolean
}