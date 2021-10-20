package com.wojciechkula.locals.initializer

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}