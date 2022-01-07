package com.wojciechkula.locals.presentation.group.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.LeaveGroupInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDialogViewModel @Inject constructor(
    private val leaveGroupInteractor: LeaveGroupInteractor,
) : ViewModel() {

    private var _viewEvent = LiveEvent<GroupDialogViewEvent>()
    val viewEvent: LiveData<GroupDialogViewEvent>
        get() = _viewEvent


    fun leaveGroup(groupId: String) {
        viewModelScope.launch {
            leaveGroupInteractor(groupId)
            _viewEvent.postValue(GroupDialogViewEvent.OpenMyGroups)
        }
    }
}