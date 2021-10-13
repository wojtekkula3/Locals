package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.splashscreen.SplashScreenFragmentDirections

internal class SplashScreenNavigator {

    fun openLogin(navController: NavController) {
        val direction = SplashScreenFragmentDirections.openLogin()
        navController.navigate(direction)
    }

    fun openDashboard(navController: NavController) {
        val direction = SplashScreenFragmentDirections.openDashboard()
        navController.navigate(direction)
    }
}