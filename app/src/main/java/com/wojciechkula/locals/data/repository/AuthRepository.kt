package com.wojciechkula.locals.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wojciechkula.locals.data.datasource.FirebaseAuthDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) {

    suspend fun getUser(): FirebaseUser? = firebaseAuthDataSource.getUser()

    suspend fun logIn(email: String, password: String) = firebaseAuthDataSource.logIn(email, password)

    suspend fun createUser(email: String, password: String) = firebaseAuthDataSource.createUser(email, password)
}