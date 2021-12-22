package com.wojciechkula.locals.presentation.splashscreen

sealed class SplashScreenViewEvent {
    object OpenLogin : SplashScreenViewEvent()
    object GetGroupsForExplore : SplashScreenViewEvent()
}