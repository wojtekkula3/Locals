package com.wojciechkula.locals.data.datasource

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wojciechkula.locals.common.bitmap.BitmapService
import com.wojciechkula.locals.data.entity.Group
import com.wojciechkula.locals.data.entity.Hobby
import com.wojciechkula.locals.data.entity.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GroupDataSource @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val bitmapService: BitmapService
) {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    @SuppressLint("MissingPermission")
    suspend fun getGroupsByDistanceAndHobbies(
        distance: Int, selectedHobbies: List<Hobby>?, user: User
    ): ArrayList<Group> =
        suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val lat = task.result.latitude
                val lng = task.result.longitude

                val myLocation = GeoLocation(lat, lng)
                val selectedRadiusInM = (distance * 1000).toDouble()

                // Posiadając punkt centralny oraz dystans w promieniu, „bounds” otrzymuje zestaw granic,
                // które można połączyć aby znaleźć wszystkie lokalizacje grup
                // w zasięgu podanej odległości od centrum. Może istnieć aż do 9 elementów "bounds"
                val bounds = GeoFireUtils.getGeoHashQueryBounds(myLocation, selectedRadiusInM)
                val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
                for (b in bounds) {
                    val q: Query = db.collection("Groups")
                        .orderBy("location.geohash")
                        .startAt(b.startHash)
                        .endAt(b.endHash)
                    tasks.add(q.get())
                }

                // Zierz wszystkie wyniki i zwróć pojedynczą listę znalezionych grup
                Tasks.whenAllComplete(tasks)
                    .addOnCompleteListener {
                        val matchingGroups = ArrayList<Group>()
                        for (task in tasks) {
                            val snap = task.result

                            // Wykonaj operacje na każdym znalezionym obiekcie
                            for (doc in snap.documents) {
                                val lat = doc.getDouble("location.latitude")!!
                                val lng = doc.getDouble("location.longitude")!!

                                // Filtrowanie, które pozwala wykryć wadliwe wyniki,
                                // ich ilość jest znikoma, jednak warta wyodrębnienia
                                val docLocation = GeoLocation(lat, lng)
                                val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, myLocation)

                                val group = doc.toObject(Group::class.java)!!
                                group.distance = distanceInM

                                if (distanceInM < selectedRadiusInM) {
                                    if (selectedHobbies.isNullOrEmpty()) {
                                        matchingGroups.add(group)
                                    } else {
                                        for (selectedHobby in selectedHobbies) {
                                            for (groupHobby in group.hobbies) {
                                                if (selectedHobby.name == groupHobby) {
                                                    if (!matchingGroups.contains(group)) {
                                                        matchingGroups.add(group)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Wyklucz te grupy, których użytkownik jest członkiem
                        val groupsThatUserIsAMember: ArrayList<Group> = arrayListOf()
                        for (group in matchingGroups) {
                            for (member in group.members) {
                                if (member == user.id) {
                                    groupsThatUserIsAMember.add(group)
                                }
                            }
                        }
                        matchingGroups.removeAll(groupsThatUserIsAMember)

                        matchingGroups.sortBy { group -> group.distance }
                        continuation.resume(matchingGroups)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

    suspend fun createGroup(imageBitmap: Bitmap?, group: Group): DocumentReference = suspendCoroutine { continuation ->
        db.collection("Groups")
            .add(group)
            .addOnSuccessListener { documentReference ->
                if (imageBitmap != null) {
                    val imageRef = storageRef.child("Groups avatars/${documentReference.id}.jpg")

                    val data = bitmapService.compressBitmap(imageBitmap)
                    imageRef.putBytes(data)
                        .addOnSuccessListener { task ->
                            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                db.collection("Groups").document(documentReference.id)
                                    .update("avatar", downloadUrl.toString())
                                    .addOnSuccessListener {
                                        continuation.resume(documentReference)
                                    }
                                    .addOnFailureListener { exception ->
                                        continuation.resumeWithException(exception)
                                    }
                            }
                                .addOnFailureListener { exception ->
                                    continuation.resumeWithException(exception)
                                }
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                } else {
                    continuation.resume(documentReference)
                }
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    suspend fun getUserGroups(user: User): Flow<List<Group>> = callbackFlow {
        val query = db.collection("Groups")
            .whereArrayContains("members", user.id)
            .orderBy("latestMessage.sentAt", Query.Direction.DESCENDING)

        val snapshotListener = query.addSnapshotListener { snapshot, error ->
            if (error == null) {
                if (snapshot != null) {
                    this.trySend(snapshot.toObjects(Group::class.java)).isSuccess
                } else {
                    this.trySend(emptyList())
                }
            } else {
                cancel(message = "Error while getting users chats.", cause = error)
            }
        }
        awaitClose {
            snapshotListener.remove()
            cancel()
        }
    }

    suspend fun getGroup(groupId: String): Flow<Group> = callbackFlow {
        val query = db.collection("Groups")
            .document(groupId)

        val snapshotListener = query.addSnapshotListener { snapshot, error ->
            if (error == null) {
                if (snapshot != null) {
                    snapshot.toObject(Group::class.java)?.let { this.trySend(it).isSuccess }
                }
            } else {
                cancel(message = "Error while getting users chats.", cause = error)
            }
        }
        awaitClose {
            snapshotListener.remove()
            cancel()
        }
    }

    suspend fun joinGroup(id: String, user: User): Boolean = suspendCoroutine { continuation ->
        val reference = db.document("/Groups/$id")

        val addGroupToUser = db.collection("Users").document(user.id)
            .update("groups", FieldValue.arrayUnion(reference))

        val addMemberToGroup = db.collection("Groups").document(id)
            .update("members", FieldValue.arrayUnion(user.id))

        Tasks.whenAllSuccess<Any>(addGroupToUser, addMemberToGroup)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    suspend fun leaveGroup(groupId: String, groupSize: Int, user: User): Boolean = suspendCoroutine { continuation ->
        val reference = db.document("/Groups/$groupId")

        val deleteGroupFromUser = db.collection("Users").document(user.id)
            .update("groups", FieldValue.arrayRemove(reference))

        if (groupSize == 1) {
            val deleteGroup = db.collection("Groups").document(groupId)
                .delete()

            Tasks.whenAllSuccess<Any>(deleteGroupFromUser, deleteGroup)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }

        } else {
            val deleteMemberFromGroup = db.collection("Groups").document(groupId)
                .update("members", FieldValue.arrayRemove(user.id))

            Tasks.whenAllSuccess<Any>(deleteGroupFromUser, deleteMemberFromGroup)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}