package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMessages(groupId: String): Flow<List<MessageModel>>
    suspend fun sendMessage(groupId: String, messageModel: MessageModel): Boolean
}