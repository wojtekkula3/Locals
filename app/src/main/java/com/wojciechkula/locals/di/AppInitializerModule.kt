package com.wojciechkula.locals.di

import com.wojciechkula.locals.initializer.AppInitializersContainer
import com.wojciechkula.locals.initializer.TimberInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppInitializerModule {

    @Provides
    fun appInitializersContainer(
        timberInitializer: TimberInitializer
    ) = AppInitializersContainer(
        timberInitializer
    )
}