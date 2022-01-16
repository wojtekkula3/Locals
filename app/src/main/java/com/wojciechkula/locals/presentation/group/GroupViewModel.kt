package com.wojciechkula.locals.presentation.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetGroupInteractor
import com.wojciechkula.locals.domain.interactor.GetMessagesInteractor
import com.wojciechkula.locals.domain.interactor.GetUsersByGroupMembersInteractor
import com.wojciechkula.locals.domain.interactor.SendMessageInteractor
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.domain.model.MessageModel
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.presentation.group.list.MessageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val getMessagesInteractor: GetMessagesInteractor,
    private val getGroupInteractor: GetGroupInteractor,
    private val getUsersByGroupMembersInteractor: GetUsersByGroupMembersInteractor,
    private val sendMessageInteractor: SendMessageInteractor
) : ViewModel() {

    private var _viewState = MutableStateFlow(GroupViewState())
    val viewState: StateFlow<GroupViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<GroupViewEvent>()
    val viewEvent: LiveData<GroupViewEvent>
        get() = _viewEvent

    fun getMessagesAndGroup(groupId: String) {
        viewModelScope.launch {
            getGroupInteractor(groupId)
                .catch { exception -> handleException(exception) }
                .collect(::getGroup)
        }
    }

    private fun getGroup(group: GroupModel) {
        viewModelScope.launch {
            val members = getUsersByGroupMembersInteractor(group.members)
            _viewState.value = _viewState.value.copy(group = group, members = members)
            getMessagesInteractor(group.id, members)
                .catch { exception -> handleException(exception) }
                .collect(::getMessages)
        }
    }

    private fun getMessages(messages: List<MessageModel>) {
        val messageItems: List<MessageItem> = messages.map { message ->
            MessageItem(
                id = message.id,
                authorId = message.authorId,
                authorName = message.authorName,
                authorAvatar = message.authorAvatar,
                message = message.message,
                sentAt = message.sentAt.toDate()
            )
        }
        _viewState.value = _viewState.value.copy(messages = messageItems)
        if (messageItems.isEmpty()) {
            _viewEvent.postValue(GroupViewEvent.ShowNoMessagesYetLabel)
        }
    }

    private fun handleException(exception: Throwable) {
        Timber.d("Exception happened: $exception")
    }

    fun setUser(user: UserModel) {
        _viewState.value = _viewState.value.copy(user = user)
    }

    fun sendMessage(messageText: String, groupId: String) {
        val user = viewState.value.user
        val message = MessageModel(
            authorId = user.id,
            authorName = user.name,
            authorAvatar = user.avatarReference,
            message = messageText
        )
        viewModelScope.launch {
            sendMessageInteractor(groupId = groupId, message)
        }
    }

    fun openInfo() {
        _viewEvent.postValue(GroupViewEvent.OpenInfo(_viewState.value.group))
    }
}