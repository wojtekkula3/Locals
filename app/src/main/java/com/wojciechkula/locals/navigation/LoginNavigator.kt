package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.login.LoginFragmentDirections

internal class LoginNavigator constructor() {

    fun openRegister(navController: NavController) {
        val direction = LoginFragmentDirections.openRegister()
        navController.navigate(direction)
    }

    fun openDashboard(navController: NavController) {
        val direction = LoginFragmentDirections.openDashboard()
        navController.navigate(direction)
    }
}