package com.wojciechkula.locals.presentation.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.wojciechkula.locals.domain.interactor.GetFirebaseUserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getFirebaseUserInteractor: GetFirebaseUserInteractor
) : ViewModel() {

    private var _viewEvent = LiveEvent<SplashScreenViewEvent>()
    val viewEvent: LiveData<SplashScreenViewEvent>
        get() = _viewEvent

    init {
        viewModelScope.launch {
            if (getFirebaseUserInteractor() != null) {
                _viewEvent.postValue(SplashScreenViewEvent.OpenDashboard)
            } else {
                _viewEvent.postValue(SplashScreenViewEvent.OpenLogin)
            }
        }
    }

}