package com.wojciechkula.locals.di

import com.wojciechkula.locals.data.repository.*
import com.wojciechkula.locals.domain.repository.*
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

    @Binds
    abstract fun groupRepository(repository: GroupRepositoryImpl): GroupRepository

    @Binds
    abstract fun userRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun messageRepository(repository: MessageRepositoryImpl): MessageRepository
}