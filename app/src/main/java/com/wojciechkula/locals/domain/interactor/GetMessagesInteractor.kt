package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesInteractor @Inject constructor(private val repository: ChatRepository) {

    suspend operator fun invoke(groupId: String) = repository.getMessages(groupId)
}