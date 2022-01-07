package com.wojciechkula.locals.data.repository

import com.wojciechkula.locals.data.datasource.ChatDataSource
import com.wojciechkula.locals.data.mapper.MessageMapper
import com.wojciechkula.locals.domain.model.MessageModel
import com.wojciechkula.locals.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatDataSource,
    private val mapper: MessageMapper
) : ChatRepository {

    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> = dataSource.getMessages(groupId)
        .map { message -> mapper.mapListToDomain(message) }

    override suspend fun sendMessage(groupId: String, messageModel: MessageModel): Boolean =
        dataSource.sendMessage(groupId, mapper.mapToEntity(messageModel))
}