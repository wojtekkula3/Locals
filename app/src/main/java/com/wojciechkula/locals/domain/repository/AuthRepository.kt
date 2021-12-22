package com.wojciechkula.locals.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun getUser(): FirebaseUser?
    suspend fun logIn(email: String, password: String): Task<AuthResult>
    suspend fun isNewUser(email: String): Boolean
    fun logOut(): Unit
}