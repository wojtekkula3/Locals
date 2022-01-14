package com.wojciechkula.locals.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wojciechkula.locals.data.entity.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    suspend fun getUser(): User = suspendCoroutine { continuation ->
        db.collection("Users")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                var user: User = User()
                for (document in task.result) {
                    user = document.toObject(User::class.java)
                    Timber.d(user.toString())
                }
                continuation.resume(user)
            }
    }

    suspend fun getUserFlow(): Flow<User> = callbackFlow {
        val query = db.collection("Users")
            .whereEqualTo("email", auth.currentUser?.email)

        val snapshotListener = query.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val documents = snapshot.documents
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        this.trySend(user).isSuccess
                    }
                }
            } else {
                cancel(message = "Error while getting user from Firestore", cause = error)
            }
        }
        awaitClose {
            snapshotListener.remove()
            cancel()
        }
    }

    suspend fun changeEmailVisibility(isVisible: Boolean, user: User): Boolean =
        suspendCoroutine { continuation ->
            db.collection("Users").document(user.id)
                .update("elementsVisibility.email", isVisible)
                .addOnCompleteListener { task ->
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun changePhoneNumberVisibility(isVisible: Boolean, user: User): Boolean =
        suspendCoroutine { continuation ->
            db.collection("Users").document(user.id)
                .update("elementsVisibility.phoneNumber", isVisible)
                .addOnCompleteListener { task ->
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun changeHobbiesVisibility(isVisible: Boolean, user: User): Boolean =
        suspendCoroutine { continuation ->
            db.collection("Users").document(user.id)
                .update("elementsVisibility.hobbies", isVisible)
                .addOnCompleteListener { task ->
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

}