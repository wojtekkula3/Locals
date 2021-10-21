package com.wojciechkula.locals.navigation

import androidx.navigation.NavController
import com.wojciechkula.locals.presentation.register.information.RegisterInformationFragmentDirections
import javax.inject.Inject

internal class RegisterInformationNavigator @Inject constructor(){

    fun openRegisterData(navController: NavController){
        val direction = RegisterInformationFragmentDirections.openRegisterData()
        navController.navigate(direction)
    }
}