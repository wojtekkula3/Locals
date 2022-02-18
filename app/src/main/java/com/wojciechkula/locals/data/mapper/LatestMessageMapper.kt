package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.LatestMessage
import com.wojciechkula.locals.domain.model.LatestMessageModel
import javax.inject.Inject

class LatestMessageMapper @Inject constructor() {

    fun mapToEntity(latestMessageModel: LatestMessageModel): LatestMessage =
        LatestMessage(
            authorId = latestMessageModel.authorId,
            authorName = latestMessageModel.authorName,
            message = latestMessageModel.message,
            sentAt = latestMessageModel.sentAt
        )

    fun mapToDomain(latestMessage: LatestMessage): LatestMessageModel =
        LatestMessageModel(
            authorId = latestMessage.authorId,
            authorName = latestMessage.authorName,
            message = latestMessage.message,
            sentAt = latestMessage.sentAt
        )
}