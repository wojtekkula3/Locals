package com.wojciechkula.locals.presentation.splashscreen

sealed class SplashScreenViewEvent {
    object OpenLogin : SplashScreenViewEvent()
    object CheckLocationPermissions : SplashScreenViewEvent()
    object GetGroupsForExplore : SplashScreenViewEvent()
}