package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Member
import com.wojciechkula.locals.domain.model.MemberModel
import javax.inject.Inject

class MemberMapper @Inject constructor() {

    fun mapToEntity(memberModel: MemberModel): Member =
        Member(
            userId = memberModel.userId,
            name = memberModel.name,
            surname = memberModel.surname,
            avatar = memberModel.avatar
        )

    fun mapToDomain(member: Member): MemberModel =
        MemberModel(
            userId = member.userId,
            name = member.name,
            surname = member.surname,
            avatar = member.avatar
        )
}