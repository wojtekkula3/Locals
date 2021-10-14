package com.wojciechkula.locals.presentation.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {

    private var _viewEvent = LiveEvent<SplashScreenViewEvent>()
    val viewEvent: LiveData<SplashScreenViewEvent>
        get() = _viewEvent

}