package com.wojciechkula.locals

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LocalsApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}