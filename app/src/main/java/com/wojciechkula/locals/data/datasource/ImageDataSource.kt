package com.wojciechkula.locals.data.datasource

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.wojciechkula.locals.common.bitmap.BitmapService
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ImageDataSource @Inject constructor(
    private val bitmapService: BitmapService
) {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun addUserImage(imageBitmap: Bitmap, userId: String): Uri? =
        suspendCoroutine { continuation ->
            val imageRef = storageRef.child("Users avatars/$userId.jpg")

            val data = bitmapService.compressBitmap(imageBitmap)
            imageRef.putBytes(data)
                .addOnSuccessListener { task ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        db.collection("Users").document(userId).update("avatarReference", downloadUrl.toString())
                            .addOnSuccessListener {
                                continuation.resume(downloadUrl)
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
        }


    suspend fun addGroupImage(imageBitmap: Bitmap, groupId: String): Uri? =
        suspendCoroutine { continuation ->
            val imageRef = storageRef.child("Groups avatars/$groupId.jpg")

            val data = bitmapService.compressBitmap(imageBitmap)
            imageRef.putBytes(data)
                .addOnSuccessListener { task ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        db.collection("Groups").document(groupId).update("avatar", downloadUrl.toString())
                            .addOnSuccessListener {
                                continuation.resume(downloadUrl)
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
        }
}