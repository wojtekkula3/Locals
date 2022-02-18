package com.wojciechkula.locals.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthDataSource @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun getUser(): FirebaseUser? = auth.currentUser

    suspend fun logIn(email: String, password: String) = try {
        auth.signInWithEmailAndPassword(email, password)
    } catch (error: FirebaseAuthException) {
        Timber.d(error.message)
        throw error
    }

    suspend fun isNewUser(email: String): Boolean =
        suspendCoroutine { continuation ->
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val isNewUser: Boolean? = it.result?.signInMethods?.isEmpty()
                        if (isNewUser!!) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

    fun logOut(): Unit = auth.signOut()
}