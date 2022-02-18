package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.NavDashboardMenuDirections
import com.wojciechkula.locals.presentation.creategroup.CreateGroupFragmentDirections
import javax.inject.Inject

internal class CreateGroupNavigator @Inject constructor() {

    fun openMap(navController: NavController) {
        val direction = CreateGroupFragmentDirections.openMap()
        navController.navigate(direction)
    }

    fun openMyGroups(navController: NavController) {
        val direction = NavDashboardMenuDirections.openMyGroups()
        navController.navigate(direction)
    }
}