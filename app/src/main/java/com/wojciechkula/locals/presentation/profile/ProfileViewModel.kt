package com.wojciechkula.locals.presentation.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.AddUserImageInteractor
import com.wojciechkula.locals.domain.interactor.ChangeEmailVisibilityInteractor
import com.wojciechkula.locals.domain.interactor.ChangeHobbiesVisibilityInteractor
import com.wojciechkula.locals.domain.interactor.ChangePhoneVisibilityInteractor
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.extension.newBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val addUserImageInteractor: AddUserImageInteractor,
    private val changeEmailVisibilityInteractor: ChangeEmailVisibilityInteractor,
    private val changePhoneVisibilityInteractor: ChangePhoneVisibilityInteractor,
    private val changeHobbiesVisibilityInteractor: ChangeHobbiesVisibilityInteractor,
) : ViewModel() {


    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var _viewState = MutableLiveData<ProfileViewState>()
    val viewState: LiveData<ProfileViewState>
        get() = _viewState

    private var _viewEvent = LiveEvent<ProfileViewEvent>()
    val viewEvent: MutableLiveData<ProfileViewEvent>
        get() = _viewEvent

    init {
        _viewState.value = ProfileViewState()
    }

    fun setUserInformation(user: UserModel) {
        _viewState.postValue(
            ProfileViewState(
                user = user,
                emailVisibility = user.elementsVisibility.email,
                phoneNumberVisibility = user.elementsVisibility.phoneNumber,
                hobbiesVisibility = user.elementsVisibility.hobbies
            )
        )
    }

    fun changeEmailVisibility(isVisible: Boolean) {
        viewModelScope.launch {
            changeEmailVisibilityInteractor(isVisible)
            _viewState.value = viewState.newBuilder { copy(emailVisibility = isVisible) }
        }
    }

    fun changePhoneVisibility(isVisible: Boolean) {
        viewModelScope.launch {
            changePhoneVisibilityInteractor(isVisible)
            _viewState.value = viewState.newBuilder { copy(phoneNumberVisibility = isVisible) }
        }
    }

    fun changeHobbiesVisibility(isVisible: Boolean) {
        viewModelScope.launch {
            changeHobbiesVisibilityInteractor(isVisible)
            _viewState.value = viewState.newBuilder { copy(hobbiesVisibility = isVisible) }
        }
    }

    fun changeUserPicture(image: Bitmap) {
        viewModelScope.launch {
            _showLoading.postValue(true)
            val email = viewState.value?.user?.email
            val id = viewState.value?.user?.id
            if (email != null && id != null) {
                try {
                    val uri = addUserImageInteractor(image, id, email)
                    _viewEvent.postValue(ProfileViewEvent.ShowImageChangeSuccess(uri))
                } catch (e: Exception) {
                    _viewEvent.postValue(ProfileViewEvent.ShowError(e.message.toString()))
                }
            }
            _showLoading.postValue(false)
        }
    }
}