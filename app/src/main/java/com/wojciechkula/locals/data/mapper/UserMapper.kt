package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.PersonalElementsVisibility
import com.wojciechkula.locals.data.entity.User
import com.wojciechkula.locals.domain.model.UserModel
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun mapToEntity(user: UserModel): User = User(
        name = user.name,
        surname = user.surname,
        email = user.email,
        phoneNumber = user.phoneNumber,
        hobbies = user.hobbies,
        about = user.about,
        elementsVisibility = PersonalElementsVisibility(
            user.elementsVisibility.email,
            user.elementsVisibility.phoneNumber,
            user.elementsVisibility.hobbies
        )
    )

    fun mapToDomain(user: User): UserModel = UserModel(
        id = user.id,
        name = user.name,
        surname = user.surname,
        email = user.email,
        avatarReference = user.avatarReference,
        phoneNumber = user.phoneNumber,
        hobbies = user.hobbies,
        about = user.about,
        elementsVisibility = com.wojciechkula.locals.domain.model.PersonalElementsVisibilityModel(
            user.elementsVisibility.email,
            user.elementsVisibility.phoneNumber,
            user.elementsVisibility.hobbies
        ),
        groups = user.groups
    )
}