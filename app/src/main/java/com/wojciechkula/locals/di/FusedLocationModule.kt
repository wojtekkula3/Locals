package com.wojciechkula.locals.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FusedLocationModule {

    @Provides
    fun provideFusedLocationClient(@ApplicationContext appContext: Context) =
        LocationServices.getFusedLocationProviderClient(appContext)
}