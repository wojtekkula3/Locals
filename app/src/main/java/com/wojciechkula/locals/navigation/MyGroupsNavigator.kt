package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.mygroups.MyGroupsFragmentDirections
import javax.inject.Inject

internal class MyGroupsNavigator @Inject constructor() {

    fun openGroup(navController: NavController, id: String, name: String, userId: String) {
        val destination = MyGroupsFragmentDirections.openGroup(id, name, userId)
        navController.navigate(destination)
    }
}