package com.wojciechkula.locals.presentation.explore.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.JoinToGroupInteractor
import com.wojciechkula.locals.presentation.explore.ExploreViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ExploreDialogViewModel @Inject constructor(
    private val joinToGroupInteractor: JoinToGroupInteractor
) : ViewModel() {

    private var _viewEvent = LiveEvent<ExploreDialogViewEvent>()
    val viewEvent: LiveData<ExploreDialogViewEvent>
        get() = _viewEvent

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    fun onJoinButtonClick(groupId: String) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            try {
                joinToGroupInteractor(groupId)
                _viewEvent.postValue(ExploreDialogViewEvent.JoinGroup)
            } catch (exception: Exception){
                _viewEvent.postValue(ExploreDialogViewEvent.ShowError(exception))
            }
            _showLoading.postValue(false)

        }
    }

}