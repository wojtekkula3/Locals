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

    suspend fun addUserImage(imageBitmap: Bitmap, userId: String, userEmail: String): Uri? =
        suspendCoroutine { continuation ->
            val imageRef = storageRef.child("Users avatars/$userId.jpg")

            val data = bitmapService.compressBitmap(imageBitmap)
            imageRef.putBytes(data)
                .addOnSuccessListener { task ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        db.collection("Users").document(userId).update("avatarReference", downloadUrl.toString())
                            .addOnSuccessListener {

//                            val localFile = File.createTempFile("user_$userId", "jpg")
//                            imageRef.getFile(localFile).addOnSuccessListener {
//
//                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                                continuation.resume(bitmap)
//                              }
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