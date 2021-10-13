package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.hobbies.RegisterHobbiesDirections

internal class RegisterHobbiesNavigator {

    fun openDashboard(navController: NavController) {
        val direction = RegisterHobbiesDirections.openDashboard()
        navController.navigate(direction)
    }
}