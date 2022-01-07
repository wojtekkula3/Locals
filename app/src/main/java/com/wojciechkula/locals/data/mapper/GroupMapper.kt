package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Group
import com.wojciechkula.locals.data.entity.Location
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.LatestMessageModel
import com.wojciechkula.locals.domain.model.LocationModel
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
            members = group.members,
        )

    fun mapToDomain(group: Group): GroupModel =
        GroupModel(
            id = group.id,
            name = group.name,
            location = LocationModel(group.location.geohash, group.location.latitude, group.location.longitude),
            distance = group.distance,
            avatar = group.avatar,
            hobbies = group.hobbies,
            members = group.members,
            latestMessage = LatestMessageModel(
                group.latestMessage.authorId,
                group.latestMessage.authorName,
                group.latestMessage.message,
                group.latestMessage.sentAt
            )
        )

    fun mapListToDomain(groups: List<Group>?): List<GroupModel> {
        val groupsModelList: ArrayList<GroupModel> = arrayListOf()
        if (groups != null) {
            for (group in groups) {
                val groupModel = mapToDomain(group)
                groupsModelList.add(groupModel)
            }
        }
        return groupsModelList
    }
}