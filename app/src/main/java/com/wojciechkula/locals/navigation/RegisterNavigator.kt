package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.userdata.RegisterFragmentDirections

internal class RegisterNavigator {

    fun openRegisterHobbies(navController: NavController) {
        val direction = RegisterFragmentDirections.openRegisterHobbies()
        navController.navigate(direction)
    }
}