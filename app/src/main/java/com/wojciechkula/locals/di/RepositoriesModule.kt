package com.wojciechkula.locals.di

import com.wojciechkula.locals.data.repository.AuthRepositoryImpl
import com.wojciechkula.locals.data.repository.HobbyRepositoryImpl
import com.wojciechkula.locals.data.repository.RegisterRepositoryImpl
import com.wojciechkula.locals.domain.repository.AuthRepository
import com.wojciechkula.locals.domain.repository.HobbyRepository
import com.wojciechkula.locals.domain.repository.RegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun authRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun registerRepository(repository: RegisterRepositoryImpl): RegisterRepository

    @Binds
    abstract fun hobbyRepository(repository: HobbyRepositoryImpl): HobbyRepository

}