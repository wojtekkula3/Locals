package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.MessageModel
import com.wojciechkula.locals.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(groupId: String, message: MessageModel) = repository.sendMessage(groupId, message)
}