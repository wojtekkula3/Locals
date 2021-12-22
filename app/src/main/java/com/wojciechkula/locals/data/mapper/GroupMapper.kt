package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Group
import com.wojciechkula.locals.data.entity.Location
import com.wojciechkula.locals.data.entity.Member
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.LocationModel
import com.wojciechkula.locals.domain.model.MemberModel
import javax.inject.Inject

class GroupMapper @Inject constructor(private val mapper: MemberMapper) {

    fun mapToEntity(group: GroupModel): Group =
        Group(
            id = group.id,
            name = group.name,
            location = Location(group.location.geohash, group.location.latitude, group.location.longitude),
            distance = group.distance,
            avatar = group.avatar,
            hobbies = group.hobbies,
            members = group.members.map { member -> mapper.mapToEntity(member) } as ArrayList<Member>
        )

    fun mapToDomain(group: Group): GroupModel =
        GroupModel(
            id = group.id,
            name = group.name,
            location = LocationModel(group.location.geohash, group.location.latitude, group.location.longitude),
            distance = group.distance,
            avatar = group.avatar,
            hobbies = group.hobbies,
            members = group.members.map { member -> mapper.mapToDomain(member) } as ArrayList<MemberModel>
        )
}