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
import com.wojciechkula.locals.data.entity.Location
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
        distance: Int,
        selectedHobbies: List<Hobby>?,
        user: User
    ): ArrayList<Group> =
        suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val lat = task.result.latitude
                val lng = task.result.longitude

                val center = GeoLocation(lat, lng)
                val radiusInM = (distance * 1000).toDouble()

                // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
                // a separate query for each pair. There can be up to 9 pairs of bounds
                // depending on overlap, but in most cases there are 4.
                val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
                val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
                for (b in bounds) {
                    val q: Query = db.collection("Groups")
                        .orderBy("location.geohash")
                        .startAt(b.startHash)
                        .endAt(b.endHash)
                    tasks.add(q.get())
                }

                // Collect all the query results together into a single list
                Tasks.whenAllComplete(tasks)
                    .addOnCompleteListener {
                        val matchingGroups = ArrayList<Group>()
                        for (task in tasks) {
                            val snap = task.result
                            for (doc in snap.documents) {
                                val lat = doc.getDouble("location.latitude")!!
                                val lng = doc.getDouble("location.longitude")!!

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                val docLocation = GeoLocation(lat, lng)
                                val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
//                                val members = doc.get("members") as ArrayList<Map<Member, Member>>
//                                val memb = members.map{ member -> member.}

//                                val membersHashMap = doc.get("members") as List<Map<String, Any>>
//                                val members: ArrayList<Member> = arrayListOf()
//                                for (member in membersHashMap) {
//
//                                    val member = Member(
//                                        name = member["name"] as String,
//                                        surname = member["surname"] as String?,
//                                        userId = member["userId"] as String,
//                                        avatar = member["avatar"] as String
//                                    )
//                                    members.add(member)
//                                }

                                val group = Group(
                                    id = doc.id,
                                    name = doc.get("name") as String,
                                    description = doc.get("description") as String,
                                    location = Location(
                                        doc.get("location.geohash") as String,
                                        doc.get("location.latitude") as Double,
                                        doc.get("location.longitude") as Double,
                                    ),
                                    distance = distanceInM,
                                    avatar = doc.get("avatar") as String?,
                                    hobbies = doc.get("hobbies") as ArrayList<String>,
                                    members = doc.get("members") as ArrayList<String>,
                                )
//                                Timber.d(group.members.toString())
//                                Dla lokacji oddalonej o 75 600 m, po wybraniu odległości 75km
//                                75500 < (75000 + 1000)
                                if (distanceInM < (radiusInM + 1000)) {
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

                        var groupsThatUserIsAMember: ArrayList<Group> = arrayListOf()
                        for (group in matchingGroups) {
                            for (member in group.members) {
                                if (member == user.id) {
                                    groupsThatUserIsAMember.add(group)
                                }
                            }
                        }
                        matchingGroups.removeAll(groupsThatUserIsAMember)
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

    suspend fun leaveGroup(groupId: String, user: User): Boolean = suspendCoroutine { continuation ->
        val reference = db.document("/Groups/$groupId")

        val deleteGroupFromUser = db.collection("Users").document(user.id)
            .update("groups", FieldValue.arrayRemove(reference))

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