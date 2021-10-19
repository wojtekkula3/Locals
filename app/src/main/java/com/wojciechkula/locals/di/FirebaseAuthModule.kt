package com.wojciechkula.locals.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseAuthModule {

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}