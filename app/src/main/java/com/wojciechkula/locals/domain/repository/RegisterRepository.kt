package com.wojciechkula.locals.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference
import com.wojciechkula.locals.domain.model.UserModel

interface RegisterRepository {

    suspend fun registerUser(email: String, password: String): Task<AuthResult>
    suspend fun registerUserData(user: UserModel): Task<DocumentReference>
}