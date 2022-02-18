package com.wojciechkula.locals

import android.app.Application
import com.wojciechkula.locals.initializer.AppInitializersContainer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LocalsApp : Application() {

    @Inject
    lateinit var initializersContainer: AppInitializersContainer

    override fun onCreate() {
        super.onCreate()
        initializersContainer.init(this)
    }
}