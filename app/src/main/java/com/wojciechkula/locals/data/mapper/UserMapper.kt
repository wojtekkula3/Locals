package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Hobby
import com.wojciechkula.locals.data.entity.User
import com.wojciechkula.locals.domain.model.HobbyModel
import com.wojciechkula.locals.domain.model.UserModel
import javax.inject.Inject

class UserMapper @Inject constructor(
    private val hobbyMapper: HobbyMapper
) {

    fun mapToEntity(user: UserModel): User = User(
        name = user.name,
        surname = user.surname,
        email = user.email,
        avatar = user.avatar,
        phoneNumber = user.phoneNumber,
        hobbies = user.hobbies?.map { hobby -> hobbyMapper.mapToEntity(hobby = hobby) } as ArrayList<Hobby>,
        about = user.about,
    )

    fun mapToDomain(user: User): UserModel = UserModel(
        name = user.name,
        surname = user.surname,
        email = user.email,
        avatar = user.avatar,
        phoneNumber = user.phoneNumber,
        hobbies = user.hobbies?.map { hobby -> hobbyMapper.mapToDomain(hobby = hobby) } as ArrayList<HobbyModel>,
        about = user.about,
    )
}