package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Group
import com.wojciechkula.locals.data.entity.Location
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.LocationModel
import javax.inject.Inject

class GroupMapper @Inject constructor(
    private val latestMessageMapper: LatestMessageMapper
) {

    fun mapToEntity(group: GroupModel): Group =
        Group(
            id = group.id,
            name = group.name,
            description = group.description,
            location = Location(group.location.geohash, group.location.latitude, group.location.longitude),
            distance = group.distance,
            avatar = group.avatar,
            hobbies = group.hobbies,
            members = group.members,
            latestMessage = latestMessageMapper.mapToEntity(group.latestMessage)
        )

    fun mapToDomain(group: Group): GroupModel =
        GroupModel(
            id = group.id,
            name = group.name,
            description = group.description,
            location = LocationModel(group.location.geohash, group.location.latitude, group.location.longitude),
            distance = group.distance,
            avatar = group.avatar,
            hobbies = group.hobbies,
            members = group.members,
            latestMessage = latestMessageMapper.mapToDomain(group.latestMessage)
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