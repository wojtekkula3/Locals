package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.hobbies.RegisterHobbiesFragmentDirections
import javax.inject.Inject

internal class RegisterHobbiesNavigator @Inject constructor() {

    fun openDashboard(navController: NavController) {
        val direction = RegisterHobbiesFragmentDirections.openDashboard()
        navController.navigate(direction)
    }
}