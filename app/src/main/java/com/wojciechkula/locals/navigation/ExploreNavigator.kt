package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.explore.ExploreFragmentDirections
import javax.inject.Inject

internal class ExploreNavigator @Inject constructor() {

    fun openCreateGroup(navController: NavController) {
        val direction = ExploreFragmentDirections.openCreateGroup()
        navController.navigate(direction)
    }
}