package com.wojciechkula.locals.data.datasource

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wojciechkula.locals.data.entity.LatestMessage
import com.wojciechkula.locals.data.entity.Member
import com.wojciechkula.locals.data.entity.Message
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MessageDataSource @Inject constructor() {

    private val db = Firebase.firestore

    suspend fun getMessages(groupId: String, members: List<Member>): Flow<List<Message>> = callbackFlow {
        val query = db.collection("Groups")
            .document(groupId)
            .collection("Messages")
            .orderBy("sentAt", Query.Direction.ASCENDING)

        val snapshotListener = query.addSnapshotListener { snapshot, error ->
            if (error == null) {
                if (snapshot != null) {
                    val messages = snapshot.toObjects(Message::class.java)
                    for (member in members) {
                        for (message in messages) {
                            if (member.userId == message.authorId) {
                                message.authorAvatar = member.avatar
                            }
                        }
                    }
                    this.trySend(messages).isSuccess
                } else {
                    this.trySend(emptyList())
                }
            } else {
                cancel(message = "Error while getting messages.", cause = error)
            }
        }
        awaitClose {
            snapshotListener.remove()
            cancel()
        }
    }

    suspend fun sendMessage(groupId: String, message: Message): Boolean = suspendCoroutine { continuation ->

        val latestMessage = LatestMessage(
            authorId = message.authorId,
            authorName = message.authorName,
            message = message.message,
            sentAt = message.sentAt
        )

        val addNewMessage = db.collection("Groups")
            .document(groupId)
            .collection("Messages")
            .add(message)

        val changeMessage = db.collection("Groups").document(groupId)
            .update("latestMessage", latestMessage)

        Tasks.whenAllSuccess<Any>(addNewMessage, changeMessage)
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

}