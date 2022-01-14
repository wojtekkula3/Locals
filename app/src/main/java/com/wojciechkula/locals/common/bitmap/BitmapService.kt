package com.wojciechkula.locals.common.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class BitmapService @Inject constructor(
    private val bitmapConfig: BitmapConfig
) {

    fun compressBitmapStream(inputStream: InputStream): ByteArray {
        return compressBitmap(BitmapFactory.decodeStream(inputStream))
    }

    fun compressBitmap(bitmap: Bitmap): ByteArray {
        val width = 600
        val height = bitmap.height * width / bitmap.width
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val outputStream = ByteArrayOutputStream().compressBitmap(scaledBitmap)
        return outputStream.toByteArray()
    }

    private fun <T : OutputStream> T.compressBitmap(bitmap: Bitmap): T {
        bitmap.compress(bitmapConfig.compressFormat, bitmapConfig.quality, this)
        return this
    }
}

data class BitmapConfig(
    val compressFormat: Bitmap.CompressFormat,
    val quality: Int
)