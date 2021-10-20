package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.forgotpassword.ForgotPasswordFragmentDirections
import com.wojciechkula.locals.presentation.login.LoginFragmentDirections
import javax.inject.Inject

internal class LoginNavigator @Inject constructor() {

    fun openRegister(navController: NavController) {
        val direction = LoginFragmentDirections.openRegister()
        navController.navigate(direction)
    }

    fun openForgotPassword(navController: NavController){
        val direction = LoginFragmentDirections.openForgotPassword()
        navController.navigate(direction)
    }

    fun openDashboard(navController: NavController) {
        val direction = LoginFragmentDirections.openDashboard()
        navController.navigate(direction)
    }
}