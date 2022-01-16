package com.wojciechkula.locals.domain.interactor

import com.wojciechkula.locals.domain.model.MemberModel
import com.wojciechkula.locals.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessagesInteractor @Inject constructor(private val repository: MessageRepository) {

    suspend operator fun invoke(
        groupId: String,
        members: List<MemberModel>
    ) = repository.getMessages(groupId, members)
}