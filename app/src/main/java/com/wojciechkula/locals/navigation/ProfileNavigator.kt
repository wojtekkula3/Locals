package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.profile.ProfileFragmentDirections
import javax.inject.Inject

internal class ProfileNavigator @Inject constructor() {

    fun openLogin(navController: NavController) {
        val direction = ProfileFragmentDirections.openLogin()
        navController.navigate(direction)

    }
}