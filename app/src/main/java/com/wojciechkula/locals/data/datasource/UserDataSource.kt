package com.wojciechkula.locals.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wojciechkula.locals.data.entity.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    suspend fun getUser(): User =
        suspendCoroutine { continuation ->
            db.collection("Users")
                .whereEqualTo("email", auth.currentUser?.email)
                .get()
                .addOnCompleteListener { task ->
                    var user: User = User()
                    for (document in task.result) {
                        user = document.toObject(User::class.java)
                    }
                    continuation.resume(user)
                }
        }

    suspend fun changeEmailVisibility(isVisible: Boolean, user: User): Boolean =
        suspendCoroutine { continuation ->
            db.collection("Users").document(user.documentId)
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
            db.collection("Users").document(user.documentId)
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
            db.collection("Users").document(user.documentId)
                .update("elementsVisibility.hobbies", isVisible)
                .addOnCompleteListener { task ->
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

}