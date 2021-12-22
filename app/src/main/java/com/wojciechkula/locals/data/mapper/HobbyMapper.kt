package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Hobby
import com.wojciechkula.locals.domain.model.HobbyModel
import javax.inject.Inject

class HobbyMapper @Inject constructor() {

    fun mapToDomain(hobby: Hobby): HobbyModel {
        return HobbyModel(hobby.name, hobby.general, hobby.language)
    }

    fun mapToEntity(hobby: HobbyModel): Hobby =
        Hobby(
            name = hobby.name,
            general = hobby.general,
            language = hobby.language
        )
}