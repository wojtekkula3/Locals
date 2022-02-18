package com.wojciechkula.locals.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wojciechkula.locals.data.entity.User
import javax.inject.Inject


class RegisterDataSource @Inject constructor() {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

//    suspend fun registerUser(email: String, password: String) : AuthResult =
//        try {
//            val result = auth.createUserWithEmailAndPassword(email, password)
//        } catch (exception: FirebaseAuthException) {
//            Throw
//            Timber.d(exception, "Registration error: Exception while adding user to firebase")
//        }
//    suspend fun registerUserData(user: User) =
//        try {
//            db.collection("Users").add(user)
//        } catch (exception: FirebaseFirestoreException) {
//            Timber.d(exception, "Registration error: Exception while adding user data to firestore")
//        }

    suspend fun registerUser(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    suspend fun registerUserData(user: User): Task<DocumentReference> {
        val user = User(
            name = user.name,
            surname = user.surname,
            email = user.email,
            avatarReference = user.avatarReference,
            phoneNumber = user.phoneNumber,
            hobbies = user.hobbies,
            about = user.about,
            elementsVisibility = user.elementsVisibility,
            groups = user.groups
        )
        return db.collection("Users").add(user)
    }

}