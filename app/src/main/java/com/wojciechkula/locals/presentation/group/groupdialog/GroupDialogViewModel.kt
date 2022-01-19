package com.wojciechkula.locals.presentation.group.groupdialog

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.AddGroupImageInteractor
import com.wojciechkula.locals.domain.interactor.LeaveGroupInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDialogViewModel @Inject constructor(
    private val leaveGroupInteractor: LeaveGroupInteractor,
    private val addGroupImageInteractor: AddGroupImageInteractor,
) : ViewModel() {

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var _viewEvent = LiveEvent<GroupDialogViewEvent>()
    val viewEvent: LiveData<GroupDialogViewEvent>
        get() = _viewEvent

    fun leaveGroup(groupId: String) {
        viewModelScope.launch {
            leaveGroupInteractor(groupId)
            _viewEvent.postValue(GroupDialogViewEvent.OpenMyGroups)
        }
    }

    fun changeGroupImage(bitmap: Bitmap, groupId: String) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            try {
                addGroupImageInteractor(bitmap, groupId)
                _viewEvent.postValue(GroupDialogViewEvent.ShowImageChangeSuccess)
            } catch (exception: Exception) {
                _viewEvent.postValue(GroupDialogViewEvent.ShowError(exception))
            }
            _showLoading.postValue(false)
        }
    }
}