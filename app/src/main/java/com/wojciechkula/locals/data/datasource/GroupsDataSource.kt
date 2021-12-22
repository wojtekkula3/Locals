package com.wojciechkula.locals.data.datasource

import android.annotation.SuppressLint
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
import com.wojciechkula.locals.data.entity.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GroupsDataSource @Inject constructor(private val fusedLocationClient: FusedLocationProviderClient) {

    private val db = Firebase.firestore

    suspend fun addGroup(group: Group): DocumentReference = suspendCoroutine { continuation ->
        db.collection("Groups")
            .add(group)
            .addOnSuccessListener { documentReference ->
                continuation.resume(documentReference)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

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
                        .orderBy("Location.geohash")
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
                                val lat = doc.getDouble("Location.latitude")!!
                                val lng = doc.getDouble("Location.longitude")!!

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                val docLocation = GeoLocation(lat, lng)
                                val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
//                                val members = doc.get("members") as ArrayList<Map<Member, Member>>
//                                val memb = members.map{ member -> member.}

                                val membersHashMap = doc.get("members") as List<Map<String, Any>>
                                val members: ArrayList<Member> = arrayListOf()
                                for (member in membersHashMap) {

                                    val member = Member(
                                        name = member["name"] as String,
                                        surname = member["surname"] as String?,
                                        userId = member["userId"] as String,
                                        avatar = member["avatar"] as String
                                    )
                                    members.add(member)
                                }

                                val group = Group(
                                    id = doc.id,
                                    name = doc.get("name") as String,
                                    location = Location(
                                        doc.get("Location.geohash") as String,
                                        doc.get("Location.latitude") as Double,
                                        doc.get("Location.longitude") as Double,
                                    ),
                                    distance = distanceInM,
                                    avatar = null,
                                    hobbies = doc.get("hobbies") as ArrayList<String>,
                                    members = members
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
                                if (member.userId == user.documentId) {
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

    suspend fun joinGroup(id: String, user: User): Boolean = suspendCoroutine { continuation ->
        val reference = db.document("/Groups/$id")

        val userToSend = HashMap<String, Any>()
        userToSend["avatar"] = user.avatar.toString()
        userToSend["name"] = user.name
        userToSend["surname"] = user.surname.toString()
        userToSend["userId"] = user.documentId

        val addGroupToUser = db.collection("Users").document(user.documentId)
            .update("groups", FieldValue.arrayUnion(reference))

        val addMemberToGroup = db.collection("Groups").document(id)
            .update("members", FieldValue.arrayUnion(userToSend))

        Tasks.whenAllSuccess<Any>(addGroupToUser, addMemberToGroup)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}