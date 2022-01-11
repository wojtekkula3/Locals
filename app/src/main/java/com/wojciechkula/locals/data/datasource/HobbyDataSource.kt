package com.wojciechkula.locals.data.datasource

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wojciechkula.locals.data.entity.Hobby
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HobbyDataSource @Inject constructor() {

    private val db = Firebase.firestore
    private val batch = db.batch()

    suspend fun getHobbiesGeneral(): ArrayList<Hobby> =
        suspendCoroutine { continuation ->
            db.collection("Hobbies")
                .whereEqualTo("general", true)
                .orderBy("name")
                .get()
                .addOnCompleteListener { task ->
                    var hobbiesList = ArrayList<Hobby>()
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val hobby = document.toObject(Hobby::class.java)
                            hobbiesList.add(hobby)
                        }
                        continuation.resume(hobbiesList)
                    } else {
                        Timber.e(task.exception, "Error getting documents ")
                    }
                }
        }

    suspend fun getHobbies(name: String): ArrayList<Hobby> =
        suspendCoroutine { continuation ->
            db.collection("Hobbies")
                .whereGreaterThanOrEqualTo("name", name.lowercase())
                .whereLessThanOrEqualTo("name", name.lowercase() + "\uF7FF")
                .orderBy("name")
                .get()
                .addOnCompleteListener { task ->
                    var hobbiesList = ArrayList<Hobby>()
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val hobby = document.toObject(Hobby::class.java)
                            hobbiesList.add(hobby)
                        }
                        continuation.resume(hobbiesList)
                    } else {
                        Timber.e(task.exception, "Error getting documents: ")
                    }
                }
        }

    suspend fun createNewHobbies(newHobbies: List<Hobby>): Boolean =
        suspendCoroutine { continuation ->
            for (hobby in newHobbies) {
                val docRef = db.collection("Hobbies").document()
                batch.set(docRef, hobby)
            }

            batch.commit()
                .addOnCompleteListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
}