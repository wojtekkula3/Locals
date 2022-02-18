package com.wojciechkula.locals.presentation.group.userdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetFirestoreUserByIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDialogViewModel @Inject constructor(
    private val getFirestoreUserByIdInteractor: GetFirestoreUserByIdInteractor
) : ViewModel() {

    private var _viewState = MutableLiveData<UserDialogViewState>()
    val viewState: LiveData<UserDialogViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<UserDialogViewEvent>()
    val viewEvent: LiveData<UserDialogViewEvent>
        get() = _viewEvent


    fun getUser(userId: String) {
        viewModelScope.launch {
            try {
                val user = getFirestoreUserByIdInteractor(userId)
                _viewState.value = UserDialogViewState(user)
            } catch (exception: Exception) {
                Timber.d(exception.message)
                _viewEvent.postValue(UserDialogViewEvent.ShowError(exception))
            }
        }
    }
}