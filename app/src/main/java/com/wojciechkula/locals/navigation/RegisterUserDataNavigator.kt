package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.model.User
import com.wojciechkula.locals.presentation.register.userdata.RegisterUserDataFragmentDirections
import javax.inject.Inject

internal class RegisterUserDataNavigator @Inject constructor() {

    fun openRegisterHobbies(navController: NavController, user: User) {
        val direction = RegisterUserDataFragmentDirections.openRegisterHobbies(user)
        navController.navigate(direction)
    }
}