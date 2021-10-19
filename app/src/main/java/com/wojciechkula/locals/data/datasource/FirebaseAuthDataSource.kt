package com.wojciechkula.locals.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun getUser(): FirebaseUser? = auth.currentUser

    suspend fun logIn(email: String, password: String) = try {
        auth.signInWithEmailAndPassword(email, password)
    } catch (error: FirebaseAuthException) {
        throw error
    }

    suspend fun createUser(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)
}