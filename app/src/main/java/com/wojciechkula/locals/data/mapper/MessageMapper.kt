package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Message
import com.wojciechkula.locals.domain.model.MessageModel
import javax.inject.Inject

class MessageMapper @Inject constructor() {

    fun mapToDomain(message: Message): MessageModel =
        MessageModel(
            id = message.id,
            authorId = message.authorId,
            authorName = message.authorName,
            authorAvatar = message.authorAvatar,
            message = message.message,
            sentAt = message.sentAt
        )

    fun mapToEntity(message: MessageModel): Message =
        Message(
            authorId = message.authorId,
            authorName = message.authorName,
            authorAvatar = message.authorAvatar,
            message = message.message,
            sentAt = message.sentAt
        )

    fun mapListToDomain(messages: List<Message>?): List<MessageModel> {
        val messageModelList: ArrayList<MessageModel> = arrayListOf()
        if (messages != null) {
            for (message in messages) {
                val messageModel = mapToDomain(message)
                messageModelList.add(messageModel)
            }
        }
        return messageModelList
    }

}