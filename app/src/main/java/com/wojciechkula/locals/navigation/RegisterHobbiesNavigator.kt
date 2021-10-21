package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.hobbies.RegisterHobbiesFragmentDirections

internal class RegisterHobbiesNavigator {

    fun openDashboard(navController: NavController) {
        val direction = RegisterHobbiesFragmentDirections.openDashboard()
        navController.navigate(direction)
    }
}