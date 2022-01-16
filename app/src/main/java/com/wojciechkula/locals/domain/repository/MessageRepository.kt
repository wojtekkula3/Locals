package com.wojciechkula.locals.domain.repository

import com.wojciechkula.locals.domain.model.MemberModel
import com.wojciechkula.locals.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(groupId: String, members: List<MemberModel>): Flow<List<MessageModel>>
    suspend fun sendMessage(groupId: String, messageModel: MessageModel): Boolean
}