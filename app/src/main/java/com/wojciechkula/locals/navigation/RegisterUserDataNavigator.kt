package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.userdata.RegisterUserDataFragmentDirections

internal class RegisterUserDataNavigator {

    fun openRegisterHobbies(navController: NavController) {
        val direction = RegisterUserDataFragmentDirections.openRegisterHobbies()
        navController.navigate(direction)
    }
}