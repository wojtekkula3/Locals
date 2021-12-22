package com.wojciechkula.locals.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wojciechkula.locals.data.datasource.FirebaseAuthDataSource
import com.wojciechkula.locals.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun getUser(): FirebaseUser? = firebaseAuthDataSource.getUser()

    override suspend fun logIn(email: String, password: String) = firebaseAuthDataSource.logIn(email, password)

    override suspend fun isNewUser(email: String): Boolean = firebaseAuthDataSource.isNewUser(email)

    override fun logOut(): Unit = firebaseAuthDataSource.logOut()
}