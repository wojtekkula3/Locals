package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Hobby
import com.wojciechkula.locals.domain.model.HobbyModel
import javax.inject.Inject

class HobbyMapper @Inject constructor() {

    fun mapToDomain(hobby: Hobby): HobbyModel {
        return hobby?.let {
            HobbyModel(hobby.name, hobby.priority, hobby.language)
        }
    }

    fun mapToEntity(hobby: HobbyModel): Hobby =
        Hobby(
            name = hobby.name,
            priority = hobby.priority,
            language = hobby.language
        )
}