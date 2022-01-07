package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.MessageDataSource
import com.wojciechkula.locals.data.mapper.MessageMapper
import com.wojciechkula.locals.domain.model.MessageModel
import com.wojciechkula.locals.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val dataSource: MessageDataSource,
    private val mapper: MessageMapper
) : MessageRepository {

    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> = dataSource.getMessages(groupId)
        .map { message -> mapper.mapListToDomain(message) }

    override suspend fun sendMessage(groupId: String, messageModel: MessageModel): Boolean =
        dataSource.sendMessage(groupId, mapper.mapToEntity(messageModel))
}